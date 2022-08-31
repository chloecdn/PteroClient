package de.chloecdn.pteroclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextRendering {

    public static void drawText(int x, int y, CharSequence text, RGBColor color) {
        MatrixStack matrices = new MatrixStack();
        matrices.push();
        MinecraftClient.getInstance().textRenderer.draw(matrices, Text.of(text.toString()), x, y, color.getValue());
        matrices.pop();
    }

    public static void drawTextScaled(int x, int y, CharSequence text, RGBColor color, float scale) {
        MatrixStack matrices = new MatrixStack();
        matrices.push();
        matrices.scale(scale, scale, 1.0F);
        MinecraftClient.getInstance().textRenderer.draw(matrices, Text.of(text.toString()), x, y, color.getValue());
        matrices.pop();
    }
}
