package de.chloecdn.pteroclient.ui.widget;

import de.chloecdn.pteroclient.util.RGBColor;
import de.chloecdn.pteroclient.util.TextRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextButtonWidget extends ButtonWidget {

    private int cProg = 0;

    public TextButtonWidget(int x, int y, Text message, PressAction onPress) {
        super(x, y, MinecraftClient.getInstance().textRenderer.getWidth(message), MinecraftClient.getInstance().textRenderer.fontHeight, message, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isHovered()) {
            if (this.cProg < 50) {
                this.cProg++;
            }
        } else {
            this.cProg = 0;
        }
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        TextRendering.drawText(this.x, this.y, this.getMessage().getString(), this.isHovered() ? new RGBColor(255, 255, 255) : new RGBColor(150, 150, 150));
    }
}
