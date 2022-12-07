package net.gasdev.nomanscraft.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<AdvancedWorkbenchScreenHandler> ADVANCED_WORKBENCH_SCREEN_HANDLER;

    public static void registerScreenHandlers() {
        ADVANCED_WORKBENCH_SCREEN_HANDLER = new ScreenHandlerType<>(AdvancedWorkbenchScreenHandler::new);
    }
}
