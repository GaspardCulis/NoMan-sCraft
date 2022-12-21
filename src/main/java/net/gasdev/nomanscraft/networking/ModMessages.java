package net.gasdev.nomanscraft.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.networking.packet.EnergySyncS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier ENERGY_SYNC_S2C_ID = new Identifier(NoMansCraft.MOD_ID, "energy_sync_s2c");

    public static void registerC2SPackets() {
        // No C2S packets
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_SYNC_S2C_ID, EnergySyncS2CPacket::receive);
    }
}
