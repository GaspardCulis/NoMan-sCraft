package net.gasdev.nomanscraft.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.gasdev.nomanscraft.networking.ModMessages;
import net.gasdev.nomanscraft.screen.ElectrolyserScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class ElectrolyserBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    public static final int INVENTORY_SIZE = 2;
    protected final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    public final long MAX_ENERGY = 30000;
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(MAX_ENERGY, 128, 32) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(amount);
                data.writeBlockPos(pos);

                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC_S2C_ID, data);
                }
            }
        }
    };

    public ElectrolyserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTROLYSER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return true;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.nomanscraft.compressor");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ElectrolyserScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    public void setEnergyLevel(long energy) {
        energyStorage.amount = energy;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public static <E extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, E e) {

    }
}
