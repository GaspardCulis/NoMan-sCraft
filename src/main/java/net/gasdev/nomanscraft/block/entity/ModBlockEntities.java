package net.gasdev.nomanscraft.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {
    public static BlockEntityType<AdvancedWorkbenchBlockEntity> ADVANCED_WORKBENCH;

    public static BlockEntityType<ElectrolyserBlockEntity> ELECTROLYSER;

    public static void registerBlockEntities() {
        ADVANCED_WORKBENCH = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(NoMansCraft.MOD_ID, "advanced_workbench"),
                FabricBlockEntityTypeBuilder.create(AdvancedWorkbenchBlockEntity::new,
                        ModBlocks.ADVANCED_WORKBENCH).build(null));

        ELECTROLYSER = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(NoMansCraft.MOD_ID, "electrolyser"),
                FabricBlockEntityTypeBuilder.create(ElectrolyserBlockEntity::new,
                        ModBlocks.ELECTROLYSER).build(null));

        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyStorage, ADVANCED_WORKBENCH);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyStorage, ELECTROLYSER);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, ELECTROLYSER);
    }
}
