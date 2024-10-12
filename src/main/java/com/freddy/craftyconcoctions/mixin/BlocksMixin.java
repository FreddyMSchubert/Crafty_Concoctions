package com.freddy.craftyconcoctions.mixin;

import com.freddy.craftyconcoctions.block.witch_cauldron.WitchCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin
{
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cauldron")), index = 1)
    private static Block changeEmptyCauldron(Block originalCauldron)
    {
        return new WitchCauldronBlock(originalCauldron.getSettings());
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lava_cauldron")), index = 1)
    private static Block changeLavaCauldron(Block originalCauldron)
    {
        return new WitchCauldronBlock(originalCauldron.getSettings());
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=water_cauldron")), index = 1)
    private static Block changeWaterCauldron(Block originalCauldron)
    {
        return new WitchCauldronBlock(originalCauldron.getSettings());
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=powder_snow_cauldron")), index = 1)
    private static Block changePowderSnowCauldron(Block originalCauldron)
    {
        return new WitchCauldronBlock(originalCauldron.getSettings());
    }
}
