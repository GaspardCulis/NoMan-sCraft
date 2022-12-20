package net.gasdev.nomanscraft;

import net.fabricmc.api.ModInitializer;
import net.gasdev.nomanscraft.block.ModBlocks;
import net.gasdev.nomanscraft.block.entity.ModBlockEntities;
import net.gasdev.nomanscraft.item.ModItems;
import net.gasdev.nomanscraft.recipes.ModRecipes;
import net.gasdev.nomanscraft.screen.ModScreenHandlers;
import net.gasdev.nomanscraft.sounds.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoMansCraft implements ModInitializer {

	public static final String MOD_ID = "nomanscraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading No Man's Craft");

		ModBlocks.registerModBlocks();
		ModItems.registerModItems();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();
		ModSounds.registerSoundEvents();

		LOGGER.info("No Man's Craft loaded !");
	}
}
