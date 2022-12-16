package net.gasdev.nomanscraft.recipes;

import net.gasdev.nomanscraft.NoMansCraft;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(NoMansCraft.MOD_ID, BlueprintRecipe.BlueprintRecipeSerializer.ID),
                BlueprintRecipe.BlueprintRecipeSerializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(NoMansCraft.MOD_ID, BlueprintRecipe.BlueprintRecipeType.ID),
                BlueprintRecipe.BlueprintRecipeType.INSTANCE);
    }
}
