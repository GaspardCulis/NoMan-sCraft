package net.gasdev.nomanscraft.screen;

import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AdvancedWorkbenchScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public AdvancedWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(AdvancedWorkbenchBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(1));
    }

    public AdvancedWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.ADVANCED_WORKBENCH_SCREEN_HANDLER, syncId);
        checkSize(inventory, AdvancedWorkbenchBlockEntity.INVENTORY_SIZE);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        checkDataCount(propertyDelegate, 1);
        this.propertyDelegate = propertyDelegate;

        initSlots(inventory);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public float getProgressRatio() {
        return (float) propertyDelegate.get(0) / (float) AdvancedWorkbenchBlockEntity.MAX_PROGRESS;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
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
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(inventory, j + i * 3, 12 + j * 18, 18 + i * 18));
            }
        }
        // Blueprint slot
        this.addSlot(new Slot(inventory, 9, 79, 58));
        // Result slot
        this.addSlot(new Slot(inventory, 10, 120, 35));
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
}
