package net.gasdev.nomanscraft.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class Blueprint extends Item {

    private final ItemStack ingredients[];

    public Blueprint(Item.Settings settings, ItemStack ingredients[]) {
        super(settings);
        this.ingredients = ingredients;
    }
}
