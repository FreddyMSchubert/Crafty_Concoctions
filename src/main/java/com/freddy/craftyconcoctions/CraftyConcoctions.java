package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.block.ModBlockEntities;
import com.freddy.craftyconcoctions.block.ModBlocks;
import com.freddy.craftyconcoctions.command.ModCommands;
import com.freddy.craftyconcoctions.effect.ModEffects;
import com.freddy.craftyconcoctions.item.ModItemGroups;
import com.freddy.craftyconcoctions.item.ModItems;
import com.freddy.craftyconcoctions.networking.ModMessages;
import com.freddy.craftyconcoctions.util.ModDataComponentTypes;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftyConcoctions implements ModInitializer
{
	public static final String MOD_ID = "craftyconcoctions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Boolean DEBUG = true;

	@Override
	public void onInitialize()
	{
		LOGGER.info("Hello World! It's time to get crafty with some concoctions!");

		ModBlocks.register();
		ModItems.register();
		ModItemGroups.register();
		ModBlockEntities.register();
		ModMessages.registerS2Csend();
		ModMessages.registerC2Sreceive();
		ModEffects.register();
		ModDataComponentTypes.register();
		ModCommands.register();
	}
}