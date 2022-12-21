package net.gasdev.nomanscraft.screen;

import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.gasdev.nomanscraft.item.ModItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AdvancedWorkbenchScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final AdvancedWorkbenchBlockEntity blockEntity;

    public AdvancedWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(2));
    }

    public AdvancedWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.ADVANCED_WORKBENCH_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), AdvancedWorkbenchBlockEntity.INVENTORY_SIZE);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);

        checkDataCount(propertyDelegate, 2);
        this.propertyDelegate = propertyDelegate;

        this.blockEntity = (AdvancedWorkbenchBlockEntity) blockEntity;

        initSlots(inventory);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public float getProgressRatio() {
        return (float) propertyDelegate.get(0) / (float) propertyDelegate.get(1);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            // Check if the item is blueprint
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() == ModItems.BLUEPRINT) {
                // If the item is blueprint, try to move it to the blueprint slot
                if (!insertItem(originalStack, AdvancedWorkbenchBlockEntity.CRAFTING_BLUEPRINT_SLOT, AdvancedWorkbenchBlockEntity.CRAFTING_BLUEPRINT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void initSlots(Inventory inventory) {
        // Ingredient slots
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new Slot(inventory, j + i * 4, 8 + j * 18, 26 + i * 18));
            }
        }
        // Blueprint slot
        this.addSlot(new BlueprintSlot(inventory, 8, 91, 58));
        // Result slot
        this.addSlot(new ResultSlot(inventory, 9, 128, 35));
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private class BlueprintSlot extends Slot {
        public BlueprintSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(ModItems.BLUEPRINT);
        }

        public int getMaxItemCount() {
            return 1;
        }
    }

    private class ResultSlot extends Slot {
        public ResultSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
