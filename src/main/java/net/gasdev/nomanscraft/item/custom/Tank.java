package net.gasdev.nomanscraft.item.custom;

import net.gasdev.nomanscraft.sounds.ModSounds;
import net.gasdev.nomanscraft.utils.GasType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.List;

public class Tank extends Item {

    public Tank(Item.Settings settings) {
        super(settings);
        setCapacity(this.getDefaultStack(), 100f);
    }

    public static float getCapacity(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (!tag.contains("capacity")) {
            tag.putFloat("capacity", 0);
        }
        return tag.getFloat("capacity");
    }

    public static void setCapacity(ItemStack stack, float capacity) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.putFloat("capacity", Math.min(capacity, 100f));
        if (capacity == 0) {
            tag.remove("gasType");
        }
    }

    public static void incrementCapacity(ItemStack stack, float increment) {
        setCapacity(stack, getCapacity(stack) + increment);
    }

    public static GasType getGasType(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (!tag.contains("gasType")) {
            tag.putString("gasType", "none");
        }
        return GasType.valueOf(tag.getString("gasType"));
    }

    public static void setGasType(ItemStack stack, GasType gasType) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.putString("gasType", gasType.getName());
    }

    public static boolean canBeFilled(ItemStack stack, GasType gasType) {
        return GasType.isGasTypeCompatible(getGasType(stack), gasType) && getCapacity(stack) < 100f;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        setCapacity(stack, 100f);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        GasType gasType = getGasType(stack);
        float capacity = getCapacity(stack);

        tooltip.add(gasType.getDisplayName().formatted(Formatting.GRAY));

        Formatting color;
        if (capacity > 70f) {
            color = Formatting.GREEN;
        } else if (capacity > 20f) {
            color = Formatting.YELLOW;
        } else if (capacity > 0f) {
            color = Formatting.RED;
        } else {
            color = Formatting.DARK_GRAY;
            tooltip.add(Text.translatable("tank.nomanscraft.empty").formatted(color));
            return;
        }
        tooltip.add(Text.translatable("tank.nomanscraft.capacity", NumberFormat.getNumberInstance().format(Math.round(capacity * 10.0) / 10.0)).formatted(color));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            float capacity = getCapacity(user.getStackInHand(hand));
            if (capacity > 0f) {
                setCapacity(user.getStackInHand(hand), Math.max(capacity - 10f, 0f));
                world.playSound(
                        null,
                        user.getBlockPos(),
                        ModSounds.TANK_REFILL,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );
            }
        }
        return super.use(world, user, hand);
    }
}
