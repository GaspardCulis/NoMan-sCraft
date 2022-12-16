package net.gasdev.nomanscraft.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Blueprint extends Item {

    public static final String STORED_RECIPE_KEY = "StoredRecipe";

    public Blueprint(Item.Settings settings) {
        super(settings);
    }

    public void setStoredRecipe(ItemStack stack, String recipe) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString(STORED_RECIPE_KEY, recipe);
    }

    public static String getStoredRecipe(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getString(STORED_RECIPE_KEY);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            setStoredRecipe(stack, "nomanscraft:steel_plate");
            // Say something to the player to indicate that the recipe has been cleared
            user.sendMessage(Text.of("Helo"), false);
        }
        return super.use(world, user, hand);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (getStoredRecipe(stack).equals("")) {
            return Text.translatable("blueprint.nomanscraft.empty");
        } else {
            String split[] = getStoredRecipe(stack).split(":");
            if (split.length == 2) {
                return Text.translatable("blueprint.nomanscraft.blueprint_of").append(Text.translatable("item.nomanscraft." + split[1]));
            } else {
                return Text.translatable("blueprint.nomanscraft.invalid");
            }
        }
    }

}
