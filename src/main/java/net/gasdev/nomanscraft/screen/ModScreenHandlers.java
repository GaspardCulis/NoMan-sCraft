package net.gasdev.nomanscraft.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<AdvancedWorkbenchScreenHandler> ADVANCED_WORKBENCH_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(AdvancedWorkbenchScreenHandler::new);

    public static ScreenHandlerType<CompressorScreenHandler> COMPRESSOR_SCREEN_HANDLER =
            new ExtendedScreenHandlerType<>(CompressorScreenHandler::new);

    public static void registerScreenHandlers() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(NoMansCraft.MOD_ID, "advanced_workbench"), ADVANCED_WORKBENCH_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(NoMansCraft.MOD_ID, "compressor"), COMPRESSOR_SCREEN_HANDLER);
    }
}
