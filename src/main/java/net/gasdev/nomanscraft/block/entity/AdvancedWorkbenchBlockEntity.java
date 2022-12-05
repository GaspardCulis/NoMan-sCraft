package net.gasdev.nomanscraft.block.entity;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AdvancedWorkbenchBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
                DefaultedList.ofSize(11, ItemStack.EMPTY);

    public AdvancedWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_WORKBENCH, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("displayName."+NoMansCraft.MOD_ID+".advanced_workbench");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, AdvancedWorkbenchBlockEntity e) {
        if (world.isClient) return;

        if (hasRecipe(e)) {
            //craftItem(e);
        }
    }

    private static boolean hasRecipe(AdvancedWorkbenchBlockEntity e) {
        return false;
    }
}
