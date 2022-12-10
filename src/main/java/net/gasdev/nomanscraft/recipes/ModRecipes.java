package net.gasdev.nomanscraft.recipes;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER,
                new Identifier(NoMansCraft.MOD_ID, BlueprintRecipe.BlueprintRecipeSerializer.ID),
                BlueprintRecipe.BlueprintRecipeSerializer.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE,
                new Identifier(NoMansCraft.MOD_ID, BlueprintRecipe.BlueprintRecipeType.ID),
                BlueprintRecipe.BlueprintRecipeType.INSTANCE);
    }
}
