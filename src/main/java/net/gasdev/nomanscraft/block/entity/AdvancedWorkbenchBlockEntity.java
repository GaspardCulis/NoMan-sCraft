package net.gasdev.nomanscraft.block.entity;

import net.gasdev.nomanscraft.screen.AdvancedWorkbenchScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AdvancedWorkbenchBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

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

        if (hasRecipe(blockEntity)) {
            blockEntity.progress++;
            markDirty(world, pos, state);
            if(blockEntity.progress >= MAX_PROGRESS) {
                craftItem(blockEntity);
            }
        } else {
            blockEntity.progress = 0;
            markDirty(world, pos, state);
        }
    }

    private static void craftItem(AdvancedWorkbenchBlockEntity blockEntity) {
        blockEntity.inventory.set(10, new ItemStack(Items.DIAMOND));
        for (int i = 0; i < 10; i++) {
            blockEntity.inventory.set(i, ItemStack.EMPTY);
        }
        blockEntity.progress = 0;
    }

    private static boolean hasRecipe(AdvancedWorkbenchBlockEntity blockEntity) {
        // Test recipe
        boolean hasTestCraft = blockEntity.getStack(0).getItem() == Items.SAND;

        return hasTestCraft && blockEntity.canInsertIntoOutputSlot(new ItemStack(Items.DIAMOND_BLOCK), null);
    }

    private boolean canInsertIntoOutputSlot(ItemStack stack, @Nullable Direction dir) {
        ItemStack itemStack = this.inventory.get(10);
        if (itemStack.isEmpty()) return true;
        if (!itemStack.isItemEqualIgnoreDamage(stack)) return false;
        return itemStack.getCount() + stack.getCount() <= stack.getMaxCount();
    }
}
