package net.gasdev.nomanscraft.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.gasdev.nomanscraft.block.entity.ElectrolyserBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class EnergySyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        long energy = buf.readLong();
        BlockPos pos = buf.readBlockPos();

        if (client.world.getBlockEntity(pos) instanceof AdvancedWorkbenchBlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);

            /* IN TUTORIAL BUT SEEMS SILLY
            if (client.player.currentScreenHandler instanceof AdvancedWorkbenchScreenHandler screenHandler &&
                    screenHandler.blockEntity.getPos().equals(pos)) {
                blockEntity.setEnergyLevel(energy);
            }*/
        } else if (client.world.getBlockEntity(pos) instanceof ElectrolyserBlockEntity blockEntity) {
            blockEntity.setEnergyLevel(energy);
        }
    }


}
