package net.gasdev.nomanscraft;

import net.fabricmc.api.ClientModInitializer;
import net.gasdev.nomanscraft.screen.AdvancedWorkbenchScreen;
import net.gasdev.nomanscraft.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class NoMansCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.ADVANCED_WORKBENCH_SCREEN_HANDLER, AdvancedWorkbenchScreen::new);
    }
}
