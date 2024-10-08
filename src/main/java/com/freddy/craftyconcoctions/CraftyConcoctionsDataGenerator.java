package com.freddy.craftyconcoctions;

import com.freddy.craftyconcoctions.datagen.ModBlockTagProvider;
import com.freddy.craftyconcoctions.datagen.ModItemTagProvider;
import com.freddy.craftyconcoctions.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CraftyConcoctionsDataGenerator implements DataGeneratorEntrypoint
{
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModModelProvider::new);
	}
}
