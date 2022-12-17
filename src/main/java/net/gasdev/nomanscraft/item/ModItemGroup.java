package net.gasdev.nomanscraft.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup NO_MANS_CRAFT = FabricItemGroup.builder(
            new Identifier(NoMansCraft.MOD_ID, "nomanscraft"))
                    .icon(() -> new ItemStack(ModItems.ARC_REACTOR))
                    .build();

    public static void register(ItemStack item) {
        ItemGroupEvents.modifyEntriesEvent(NO_MANS_CRAFT).register(content -> {
            content.add(item);
        });
    }
}
