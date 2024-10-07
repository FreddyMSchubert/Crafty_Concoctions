package com.freddy.craftyconcoctions.screen;

import com.freddy.craftyconcoctions.networking.payload.C2SPotionNameEditPayload;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PotionNameEditScreen extends Screen
{
    private TextFieldWidget nameField;

    public PotionNameEditScreen()
    {
        super(Text.translatable("screen.craftyconcoctions.potion_name_edit"));
    }

    @Override
    protected void init()
    {
        super.init();

        int x = this.width / 2 - 100;
        int y = this.height / 2 - 10;
        this.nameField = new TextFieldWidget(this.textRenderer, x, y, 200, 20, Text.of("Potion Name"));
        this.nameField.setMaxLength(32);
        this.addSelectableChild(this.nameField);
        this.setFocused(this.nameField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.nameField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_ESCAPE) && this.nameField.isFocused())
        {
            C2SPotionNameEditPayload.send(this.nameField.getText());
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
