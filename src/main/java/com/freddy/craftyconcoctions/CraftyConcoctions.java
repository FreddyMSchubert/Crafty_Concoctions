package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.block.ModBlocks;
import com.freddy.craftyconcoctions.item.ModItemGroups;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftyConcoctions implements ModInitializer
{
	public static final String MOD_ID = "craftyconcoctions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Hello World! It's time to get crafty with some concoctions!");

		ModBlocks.register();
		ModItemGroups.register();
	}
}