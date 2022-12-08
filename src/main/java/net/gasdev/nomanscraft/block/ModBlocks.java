package net.gasdev.nomanscraft.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.block.custom.AdvancedWorkbench;
import net.gasdev.nomanscraft.item.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block CHEESE_BLOCK = registerBlock("cheese_block",
            new Block(FabricBlockSettings.of(Material.STONE)
                    .strength(0.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.SLIME)
            ), ModItemGroup.NO_MANS_CRAFT);

    public static final Block ADVANCED_WORKBENCH = registerBlock("advanced_workbench",
            new AdvancedWorkbench(FabricBlockSettings.of(Material.STONE)
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.NETHERITE)
            ), ModItemGroup.NO_MANS_CRAFT);

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
