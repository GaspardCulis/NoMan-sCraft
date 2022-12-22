package net.gasdev.nomanscraft.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gasdev.nomanscraft.block.entity.AdvancedWorkbenchBlockEntity;
import net.gasdev.nomanscraft.item.ModItems;
import net.gasdev.nomanscraft.item.custom.Blueprint;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlueprintRecipe implements Recipe<SimpleInventory> {
    public static final int DEFAULT_CRAFTING_TIME = 60;
    public static final int DEFAULT_ENERGY_COST = 16;
    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack output;

    private final int craftingTime;
    private final int energyCost;

    public BlueprintRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack output, int craftingTime, int energyCost) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        this.craftingTime = craftingTime;
        this.energyCost = energyCost;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;
        // Test if the ingredients required are contained in the 8 first slots of the inventory
        // Create a copy of the inventory
        DefaultedList<ItemStack> inventoryCopy = DefaultedList.ofSize(AdvancedWorkbenchBlockEntity.INVENTORY_SIZE, ItemStack.EMPTY);
        for (int i = AdvancedWorkbenchBlockEntity.CRAFTING_INPUT_START; i < AdvancedWorkbenchBlockEntity.INVENTORY_SIZE; i++) {
            inventoryCopy.set(i, inventory.getStack(i).copy());
        }

        // Check if has the right blueprint
        if (!inventoryCopy.get(AdvancedWorkbenchBlockEntity.CRAFTING_BLUEPRINT_SLOT).getItem().equals(ModItems.BLUEPRINT))
            return false;
        if (Blueprint.getStoredRecipe(inventoryCopy.get(AdvancedWorkbenchBlockEntity.CRAFTING_BLUEPRINT_SLOT)).compareTo(this.id.toString()) != 0)
            return false;
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

    public int getCraftingTime() { return craftingTime; }

    public int getEnergyCost() { return energyCost; }

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
            ArrayList<Ingredient> inputs = new ArrayList<>();

            for (int i = 0; i < ingredients.size(); i++) {
                int count = 1;
                if (ingredients.get(i).isJsonObject()) {
                    JsonObject ingredient = ingredients.get(i).getAsJsonObject();
                    if (ingredient.has("count")) {
                        count = ingredient.get("count").getAsInt();
                    }
                }

                for (int j = 0; j < count; j++) {
                    inputs.add(Ingredient.fromJson(ingredients.get(i)));
                }
            }

            int craftingTime;
            if (JsonHelper.hasNumber(json, "craftingTime")) {
                craftingTime = JsonHelper.getInt(json, "craftingTime");
            } else {
                craftingTime = DEFAULT_CRAFTING_TIME;
            }

            int energyCost;
            if (JsonHelper.hasNumber(json, "energyCost")) {
                energyCost = JsonHelper.getInt(json, "energyCost");
            } else {
                energyCost = DEFAULT_ENERGY_COST;
            }

            DefaultedList<Ingredient> ingredientsList = DefaultedList.ofSize(inputs.size(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                ingredientsList.set(i, inputs.get(i));
            }

            return new BlueprintRecipe(id, ingredientsList, output, craftingTime, energyCost);
        }

        @Override
        public BlueprintRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();

            int craftingTime = buf.readInt();

            int energyCost = buf.readInt();

            return new BlueprintRecipe(id, inputs, output, craftingTime, energyCost);
        }

        @Override
        public void write(PacketByteBuf buf, BlueprintRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.getCraftingTime());
            buf.writeInt(recipe.getEnergyCost());
        }
    }
}