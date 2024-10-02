package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class WitchCauldronBlockEntityRenderer implements BlockEntityRenderer<WitchCauldronBlockEntity>
{
    private static final int idleFrames = 32;

    public WitchCauldronBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(WitchCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        // 1. Render water level
        boolean isMoving = entity.mode == 1;
        float baseWaterHeight = entity.waterAmount * 0.2f + 0.2f;

        matrices.push();
        matrices.translate(0.5, baseWaterHeight, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(Identifier.of(CraftyConcoctions.MOD_ID, "textures/block/fluid/still/fluid_still_" + (entity.ticksSinceModeSwitch / 2 % idleFrames + 1) + ".png")));
        Matrix4f matrixPos = matrices.peek().getPositionMatrix();
        MatrixStack.Entry entry = matrices.peek();

        vertexConsumer.vertex(matrixPos, -0.5f, 0.5f, 0.0f).color(WitchCauldronSettings.WATER_COLOR.RED, WitchCauldronSettings.WATER_COLOR.GREEN, WitchCauldronSettings.WATER_COLOR.BLUE, WitchCauldronSettings.WATER_COLOR.ALPHA).texture(0.0f, 0.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, 0.5f, 0.5f, 0.0f).color(WitchCauldronSettings.WATER_COLOR.RED, WitchCauldronSettings.WATER_COLOR.GREEN, WitchCauldronSettings.WATER_COLOR.BLUE, WitchCauldronSettings.WATER_COLOR.ALPHA).texture(1.0f, 0.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, 0.5f, -0.5f, 0.0f).color(WitchCauldronSettings.WATER_COLOR.RED, WitchCauldronSettings.WATER_COLOR.GREEN, WitchCauldronSettings.WATER_COLOR.BLUE, WitchCauldronSettings.WATER_COLOR.ALPHA).texture(1.0f, 1.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, -0.5f, -0.5f, 0.0f).color(WitchCauldronSettings.WATER_COLOR.RED, WitchCauldronSettings.WATER_COLOR.GREEN, WitchCauldronSettings.WATER_COLOR.BLUE, WitchCauldronSettings.WATER_COLOR.ALPHA).texture(0.0f, 1.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);

        matrices.pop();
    }
}
