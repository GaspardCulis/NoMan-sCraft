package net.gasdev.nomanscraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.item.custom.Blueprint;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item STEEL_INGOT = registerItem("steel_ingot",
            new Item(new FabricItemSettings().group(ModItemGroup.NO_MANS_CRAFT)));

    public static final Item BLUEPRINT_OP = registerItem("blueprint_op",
            new Blueprint(
                    new FabricItemSettings().group(ModItemGroup.NO_MANS_CRAFT),
                    new ItemStack[]{new ItemStack(Items.SAND)},
                    new ItemStack(Items.DIAMOND))
    );

    public static final Item BLUEPRINT_OP2 = registerItem("blueprint_op2",
            new Blueprint(
                    new FabricItemSettings().group(ModItemGroup.NO_MANS_CRAFT),
                    new ItemStack[]{new ItemStack(Items.DIRT, 3)},
                    new ItemStack(Items.DIAMOND))
    );


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(NoMansCraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        NoMansCraft.LOGGER.info("Registering No Man's Craft items");
    }

}
