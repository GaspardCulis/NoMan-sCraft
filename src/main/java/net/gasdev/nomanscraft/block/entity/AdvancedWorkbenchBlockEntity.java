package net.gasdev.nomanscraft.block.entity;

import net.gasdev.nomanscraft.NoMansCraft;
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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AdvancedWorkbenchBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, RecipeInputProvider {

    public static final int INVENTORY_SIZE = 11;
    public static final int MAX_PROGRESS = 40;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;

    public AdvancedWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_WORKBENCH, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return progress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:  progress = value;
                }
            }

            @Override
            public int size() {
                return 1;
            }
        };
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
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("advanced_workbench.progress");
    }

    public static void tick(World world, BlockPos pos, BlockState state, AdvancedWorkbenchBlockEntity blockEntity) {
        if (world.isClient()) return;

        Optional<BlueprintRecipe> recipe = hasRecipe(blockEntity);
        if (recipe.isPresent()) {
            blockEntity.progress++;
            markDirty(world, pos, state);
            if(blockEntity.progress >= MAX_PROGRESS) {
                craftItem(blockEntity, recipe.get());
            }
        } else {
            blockEntity.progress = 0;
            markDirty(world, pos, state);
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
        } else {
            return Optional.empty();
        }
    }

    private boolean canInsertIntoOutputSlot(ItemStack stack) {
        ItemStack itemStack = this.inventory.get(9);
        if (itemStack.isEmpty()) return true;
        if (!itemStack.isItemEqualIgnoreDamage(stack)) return false;
        return itemStack.getCount() + stack.getCount() <= stack.getMaxCount();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {

    }
}
