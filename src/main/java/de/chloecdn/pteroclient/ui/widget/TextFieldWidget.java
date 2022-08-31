package de.chloecdn.pteroclient.ui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextFieldWidget extends net.minecraft.client.gui.widget.TextFieldWidget {

    private final TooltipSupplier tooltipSupplier;

    public TextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, TooltipSupplier tooltip) {
        super(textRenderer, x, y, width, height, text);
        this.tooltipSupplier = tooltip;
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public interface TooltipSupplier {
        void onTooltip(TextFieldWidget textField, MatrixStack matrices, int x, int y);
    }
}
