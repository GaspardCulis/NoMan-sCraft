package net.gasdev.nomanscraft.sounds;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent TANK_REFILL = registerSoundEvent("tank_refill");

    public static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(NoMansCraft.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSoundEvents() {
        NoMansCraft.LOGGER.info("Registering mod sounds");
    }
}
