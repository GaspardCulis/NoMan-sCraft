package net.gasdev.nomanscraft.mixin;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class NoMansCraftMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		NoMansCraft.LOGGER.info("This line is printed by an example mod mixin!");
	}
}
