package net.gasdev.nomanscraft.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<AdvancedWorkbenchBlockEntity> ADVANCED_WORKBENCH;

    public static void registerBlockEntities() {
        ADVANCED_WORKBENCH = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(NoMansCraft.MOD_ID, "advanced_workbench"),
                FabricBlockEntityTypeBuilder.create(AdvancedWorkbenchBlockEntity::new,
                        ModBlocks.ADVANCED_WORKBENCH).build(null));
    }
}
