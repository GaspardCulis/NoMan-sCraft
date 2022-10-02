package net.gasdev.nomanscraft.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup NO_MANS_CRAFT = FabricItemGroupBuilder.build(
            new Identifier(NoMansCraft.MOD_ID, "nomanscraft"), () -> new ItemStack(ModItems.STEEL_INGOT));
}
