package net.gasdev.nomanscraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.item.custom.Blueprint;
import net.gasdev.nomanscraft.recipes.BlueprintRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item STEEL_INGOT = registerItem("steel_ingot",
            new Item(new FabricItemSettings()));

    public static final Item STEEL_PLATE = registerItem("steel_plate",
            new Item(new FabricItemSettings()));

    public static final Item CPU = registerItem("cpu",
            new Item(new FabricItemSettings()));

    public static final Item ARC_REACTOR = registerItem("arc_reactor",
            new Item(new FabricItemSettings().maxCount(16)));

    public static final Item BLUEPRINT = registerItem("blueprint",
            new Blueprint(new FabricItemSettings()));


    private static Item registerItem(String name, Item item) {
        Item registeredItem = Registry.register(Registries.ITEM, new Identifier(NoMansCraft.MOD_ID, name), item);
        ModItemGroup.register(new ItemStack(registeredItem));
        return registeredItem;
    }

    private static void registerModBlueprints() {
        ItemStack steelPlateBlueprint = new ItemStack(BLUEPRINT);
        Blueprint.setStoredRecipe(steelPlateBlueprint, "nomanscraft:steel_plate");
        ModItemGroup.register(steelPlateBlueprint);

        ItemStack cpuBlueprint = new ItemStack(BLUEPRINT);
        Blueprint.setStoredRecipe(cpuBlueprint, "nomanscraft:cpu");
        ModItemGroup.register(cpuBlueprint);
    }

    public static void registerModItems() {
        registerModBlueprints();
        NoMansCraft.LOGGER.info("Registering No Man's Craft items");
    }

}
