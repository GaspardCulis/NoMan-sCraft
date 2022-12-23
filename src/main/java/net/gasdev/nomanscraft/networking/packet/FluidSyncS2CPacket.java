package net.gasdev.nomanscraft.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.gasdev.nomanscraft.block.entity.ElectrolyserBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class FluidSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant variant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos pos = buf.readBlockPos();

        if (client.world.getBlockEntity(pos) instanceof ElectrolyserBlockEntity blockEntity) {
            blockEntity.setFluidLevel(variant, fluidLevel);
        }
    }


}
