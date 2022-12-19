package net.gasdev.nomanscraft.item.custom;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
        tag.putFloat("capacity", capacity);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        setCapacity(stack, 100f);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of(""));
        float capacity = getCapacity(stack);

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
        tooltip.add(Text.translatable("tank.nomanscraft.capacity").formatted(Formatting.GRAY).append(Text.literal(((int)getCapacity(stack)) + "%").formatted(color)));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            float capacity = getCapacity(user.getStackInHand(hand));
            if (capacity < 100f) {
                setCapacity(user.getStackInHand(hand), Math.min(capacity + 10f, 100f));
            }
        }
        return super.use(world, user, hand);
    }
}
