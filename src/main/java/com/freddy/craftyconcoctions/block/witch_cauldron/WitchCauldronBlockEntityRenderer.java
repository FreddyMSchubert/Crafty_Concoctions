package com.freddy.craftyconcoctions.block.witch_cauldron;

import com.freddy.craftyconcoctions.CraftyConcoctions;
import com.freddy.craftyconcoctions.util.ItemDisplayProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class WitchCauldronBlockEntityRenderer implements BlockEntityRenderer<WitchCauldronBlockEntity>
{
    private static final float pixel = 1f / 16f;

    private static final int idleFrames = 32;

    private static final float itemScale = 0.25f;

    private static final ItemDisplayProperties[] ITEM_DISPLAY_PROPERTIES = new ItemDisplayProperties[]
    {
            new ItemDisplayProperties(0.5f, 0f, 0.125f + pixel, -22.5f, 0f, 0f), // North side
            new ItemDisplayProperties(0.875f - pixel, 0f, 0.5f, -22.5f, 90f, 0f), // East side
            new ItemDisplayProperties(0.5f, 0f, 0.875f - pixel, -22.5f, 180f, 0f), // South side
            new ItemDisplayProperties(0.125f + pixel, 0f, 0.5f, -22.5f, 270f, 0f), // West side
    };

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

        if (!entity.currColor.equals(entity.goalColor))
            entity.currColor.shiftColorTowardsColor(entity.goalColor, WitchCauldronSettings.COLOR_SHIFTING_SPEED);

        vertexConsumer.vertex(matrixPos, -0.5f, 0.5f, 0.0f).color(entity.currColor.RED, entity.currColor.GREEN, entity.currColor.BLUE, entity.currColor.ALPHA).texture(0.0f, 0.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, 0.5f, 0.5f, 0.0f).color(entity.currColor.RED, entity.currColor.GREEN, entity.currColor.BLUE, entity.currColor.ALPHA).texture(1.0f, 0.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, 0.5f, -0.5f, 0.0f).color(entity.currColor.RED, entity.currColor.GREEN, entity.currColor.BLUE, entity.currColor.ALPHA).texture(1.0f, 1.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);
        vertexConsumer.vertex(matrixPos, -0.5f, -0.5f, 0.0f).color(entity.currColor.RED, entity.currColor.GREEN, entity.currColor.BLUE, entity.currColor.ALPHA).texture(0.0f, 1.0f).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0f, 1.0f, 0.0f);

        matrices.pop();


        // 2. Render ingredients
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        Direction facing = entity.getCachedState().get(WitchCauldronBlock.FACING);

        for (int i = 0; i < entity.ingredients.size(); i++)
        {
            ItemDisplayProperties props = ITEM_DISPLAY_PROPERTIES[i];
            ItemStack itemStack = entity.ingredients.get(i).getDefaultStack();
            if (itemStack.isEmpty()) continue;

            matrices.push();

            float adjustedPosX = props.posX;
            float adjustedPosY = props.posY;
            float adjustedPosZ = props.posZ;
            float adjustedRotY = props.rotY;

            adjustedPosY = adjustedPosY + 0.2f + entity.waterAmount * 0.2f;

            switch(facing)
            {
                case EAST:
                    adjustedPosX = props.posZ;
                    adjustedPosZ = 1.0f - props.posX;
                    adjustedRotY = props.rotY - 90f;
                    break;
                case SOUTH:
                    adjustedPosX = 1.0f - props.posX;
                    adjustedPosZ = 1.0f - props.posZ;
                    adjustedRotY = props.rotY - 180f;
                    break;
                case WEST:
                    adjustedPosX = 1.0f - props.posZ;
                    adjustedPosZ = props.posX;
                    adjustedRotY = props.rotY + 90f;
                    break;
                case NORTH:
                default:
                    break;
            }

            matrices.translate(adjustedPosX, adjustedPosY, adjustedPosZ);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(adjustedRotY));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(props.rotX));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(props.rotZ));
            matrices.scale(itemScale, itemScale, itemScale);

            itemRenderer.renderItem(itemStack, ModelTransformationMode.GUI, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);

            matrices.pop();
        }
    }
}
