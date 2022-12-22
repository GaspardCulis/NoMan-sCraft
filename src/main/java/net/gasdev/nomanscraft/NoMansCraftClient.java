package net.gasdev.nomanscraft;

import net.fabricmc.api.ClientModInitializer;
import net.gasdev.nomanscraft.screen.AdvancedWorkbenchScreen;
import net.gasdev.nomanscraft.screen.CompressorScreen;
import net.gasdev.nomanscraft.screen.CompressorScreenHandler;
import net.gasdev.nomanscraft.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class NoMansCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.ADVANCED_WORKBENCH_SCREEN_HANDLER, AdvancedWorkbenchScreen::new);
        HandledScreens.register(ModScreenHandlers.COMPRESSOR_SCREEN_HANDLER, CompressorScreen::new);
    }
}
