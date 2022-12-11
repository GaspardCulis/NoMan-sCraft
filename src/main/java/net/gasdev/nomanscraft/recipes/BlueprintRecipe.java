package net.gasdev.nomanscraft.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.logging.Logger;

public class BlueprintRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack output;

    public BlueprintRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        NoMansCraft.LOGGER.info("New BlueprintRecipe created: " + id.toString() + " with " + ingredients.size() + " ingredients and output " + output.toString());
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;
        // Test if the ingredients required are contained in the 8 first slots of the inventory
        // Create a copy of the inventory
        DefaultedList<ItemStack> inventoryCopy = DefaultedList.ofSize(AdvancedWorkbenchBlockEntity.CRAFTING_INPUT_SIZE, ItemStack.EMPTY);
        for (int i = AdvancedWorkbenchBlockEntity.CRAFTING_INPUT_START; i < AdvancedWorkbenchBlockEntity.CRAFTING_INPUT_END; i++) {
            inventoryCopy.set(i, inventory.getStack(i).copy());
        }

        // Test if the ingredients are contained in the inventory
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < inventoryCopy.size(); i++) {
                if (ingredient.test(inventoryCopy.get(i))) {
                    inventoryCopy.get(i).decrement(1);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BlueprintRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return BlueprintRecipeType.INSTANCE;
    }

    public static class BlueprintRecipeType implements RecipeType<BlueprintRecipe> {
        public static final BlueprintRecipeType INSTANCE = new BlueprintRecipeType();
        public static final String ID = "blueprint";
    }
    public static class BlueprintRecipeSerializer implements RecipeSerializer<BlueprintRecipe> {
        public static final BlueprintRecipeSerializer INSTANCE = new BlueprintRecipeSerializer();
        public static final String ID = "blueprint";

        @Override
        public BlueprintRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BlueprintRecipe(id, inputs, output);
        }

        @Override
        public BlueprintRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();

            return new BlueprintRecipe(id, inputs, output);
        }

        @Override
        public void write(PacketByteBuf buf, BlueprintRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            buf.writeItemStack(recipe.getOutput());
        }
    }
}
