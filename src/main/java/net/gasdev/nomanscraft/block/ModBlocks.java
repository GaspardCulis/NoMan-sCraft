package net.gasdev.nomanscraft.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block MONEY_BLOCK = registerBlock("money_block",
            new Block(FabricBlockSettings.of(Material.COBWEB).requiresTool().slipperiness(0.1f).collidable(false)), ItemGroup.DECORATIONS);

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(NoMansCraft.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(NoMansCraft.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        NoMansCraft.LOGGER.info("Registering No Man's Craft blocks");
    }

}
