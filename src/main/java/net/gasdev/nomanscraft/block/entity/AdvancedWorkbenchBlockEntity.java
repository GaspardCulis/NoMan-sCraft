package net.gasdev.nomanscraft.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.block.custom.AdvancedWorkbench;
import net.gasdev.nomanscraft.item.ModItems;
import net.gasdev.nomanscraft.item.custom.Blueprint;
import net.gasdev.nomanscraft.networking.ModMessages;
import net.gasdev.nomanscraft.recipes.BlueprintRecipe;
import net.gasdev.nomanscraft.screen.AdvancedWorkbenchScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class AdvancedWorkbenchBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, RecipeInputProvider {
    public static final int INVENTORY_SIZE = 10;
    public static final int CRAFTING_INPUT_START = 0;
    public static final int CRAFTING_INPUT_END = 8;
    public static final int CRAFTING_INPUT_SIZE = CRAFTING_INPUT_END - CRAFTING_INPUT_START;
    public static final int CRAFTING_BLUEPRINT_SLOT = 8;
    public static final int CRAFTING_RESULT_SLOT = 9;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public final long MAX_ENERGY = 30000;
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(MAX_ENERGY, 128, 32) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(amount);
                data.writeBlockPos(pos);

                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC_S2C_ID, data);
                }
            }
        }
    };

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 0;

    public AdvancedWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_WORKBENCH, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return progress;
                    case 1: return maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:  progress = value; break;
                    case 1: maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void setEnergyLevel(long energy) {
        energyStorage.amount = energy;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (slot == CRAFTING_BLUEPRINT_SLOT && !stack.getItem().equals(ModItems.BLUEPRINT)) return false;
        if (slot == CRAFTING_RESULT_SLOT) return false;

        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        if (slot == CRAFTING_RESULT_SLOT) return true;

        return false;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.nomanscraft.advanced_workbench");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AdvancedWorkbenchScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("advanced_workbench.progress", progress);
        nbt.putLong("advanced_workbench.energy",energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("advanced_workbench.progress");
        energyStorage.amount = nbt.getLong("advanced_workbench.energy");
    }

    public void addEnergy(long amount) {
        try(Transaction transaction = Transaction.openOuter()) {
            energyStorage.insert(amount, transaction);
            transaction.commit();
        }
    }

    public void extractEnergy(long amount) {
        try(Transaction transaction = Transaction.openOuter()) {
            energyStorage.extract(amount, transaction);
            transaction.commit();
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, AdvancedWorkbenchBlockEntity blockEntity) {
        if (world.isClient()) return;

        Optional<BlueprintRecipe> recipe = hasRecipe(blockEntity);
        int runningAnimationFrame = state.get(AdvancedWorkbench.RUNNING_ANIMATION);
        if (recipe.isPresent()) {
            if (blockEntity.energyStorage.amount >= 32) {
                blockEntity.progress++;
                blockEntity.maxProgress = recipe.get().getCraftingTime();
                blockEntity.extractEnergy(32);
                markDirty(world, pos, state);
                if (blockEntity.progress >= blockEntity.maxProgress) {
                    craftItem(blockEntity, recipe.get());
                }
                // Block animation
                if (runningAnimationFrame < AdvancedWorkbench.RUNNING_ANIMATION_MAX) {
                    world.setBlockState(pos, state.with(AdvancedWorkbench.RUNNING_ANIMATION, runningAnimationFrame + 1));
                }
            }
        } else {
            blockEntity.progress = 0;
            markDirty(world, pos, state);
            // Block animation
            if (runningAnimationFrame > 0) {
                world.setBlockState(pos, state.with(AdvancedWorkbench.RUNNING_ANIMATION, runningAnimationFrame - 1));
            }
        }
    }

    private static void craftItem(AdvancedWorkbenchBlockEntity blockEntity, BlueprintRecipe recipe) {
        // Remove used ingredients
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (int i = 0; i < blockEntity.inventory.size(); i++) {
                ItemStack stack = blockEntity.inventory.get(i);
                if (ingredient.test(stack)) {
                    stack.decrement(1);
                    break;
                }
            }
        }
        // Add crafted item
        blockEntity.setStack(9, new ItemStack(recipe.getOutput().getItem(),
                blockEntity.getStack(9).getCount() + recipe.getOutput().getCount()));

        blockEntity.progress = 0;
    }

    private static Optional<BlueprintRecipe> hasRecipe(AdvancedWorkbenchBlockEntity blockEntity) {
        SimpleInventory inventory = new SimpleInventory(blockEntity.size());
        for (int i = 0; i < blockEntity.size(); i++) {
            inventory.setStack(i, blockEntity.getStack(i));
        }

        Optional<BlueprintRecipe> match = blockEntity.getWorld().getRecipeManager().
                getFirstMatch(BlueprintRecipe.BlueprintRecipeType.INSTANCE, inventory, blockEntity.getWorld());



        if (match.isPresent() && blockEntity.canInsertIntoOutputSlot(match.get().getOutput())) {
            return match;
        }
        return Optional.empty();
    }

    private boolean canInsertIntoOutputSlot(ItemStack stack) {
        ItemStack itemStack = this.inventory.get(9);
        if (itemStack.isEmpty()) return true;
        if (!itemStack.isItemEqual(stack)) return false;
        return itemStack.getCount() + stack.getCount() <= stack.getMaxCount();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (int i = 0; i < 9; i++) {
            finder.addInput(this.inventory.get(i));
        }
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}
