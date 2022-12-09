package net.gasdev.nomanscraft.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class Blueprint extends Item {

    private ItemStack ingredients[];
    private ItemStack result;

    public Blueprint(Item.Settings settings, ItemStack ingredients[], ItemStack result) {
        super(settings);
        this.ingredients = ingredients;
        this.result = result;
    }
}
