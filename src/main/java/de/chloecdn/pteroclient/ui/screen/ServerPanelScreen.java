package de.chloecdn.pteroclient.ui.screen;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import com.mattmalec.pterodactyl4j.entities.Allocation;
import com.mojang.blaze3d.systems.RenderSystem;
import de.chloecdn.pteroclient.ui.widget.TextButtonWidget;
import de.chloecdn.pteroclient.util.RGBColor;
import de.chloecdn.pteroclient.util.TextRendering;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ServerPanelScreen extends Screen {

    private final ClientServer server;
    private String ipAddress = "retrieving...";
    private String ipPort = "retrieving...";
    private String cpuUtilization = "retrieving...";
    private String ramUtilization = "retrieving...";
    private String diskUtilization = "retrieving...";
    private int updatedInSeconds = 0;

    public ServerPanelScreen(ClientServer server) {
        super(Text.of("Server: §d" + server.getName()));
        this.server = server;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new TextButtonWidget(10, 31, Text.of("General"), button -> {
            System.out.println("UwU");
        }));
    }

    @Override
    public void close() {
        client.setScreen(new MainPanelScreen());
    }

    private void update() {
        Allocation allocation = this.server.getPrimaryAllocation();
        Utilization utilization = this.server.retrieveUtilization().execute();
        this.ipAddress = allocation.getIP();
        this.ipPort = allocation.getPort();
        this.cpuUtilization = utilization.getCPU() + "";
        this.ramUtilization = utilization.getMemoryFormatted(DataType.GB);
        this.diskUtilization = utilization.getDiskFormatted(DataType.GB);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        this.updatedInSeconds++;
        if (this.updatedInSeconds >= 100) {
            this.updatedInSeconds = 0;
            this.update();
        }
        DrawableHelper.fill(matrices, 0, 0, this.width, this.height, new RGBColor(25, 25, 25).getValue());
        DrawableHelper.fill(matrices, 0, 0, this.width, 20, new RGBColor(5, 5, 5).getValue());
        DrawableHelper.fill(matrices, 5, 25, this.width - 5, 45, new RGBColor(35, 35, 35).getValue());
        TextRendering.drawText(5, 5, "CDN-Panel - Server: " + this.server.getName() + " [" + this.server.getIdentifier() + "]", new RGBColor(255, 255, 255));
        DrawableHelper.fill(matrices, this.width - 130, 50, this.width - 5, 80, new RGBColor(35, 35, 35).getValue());
        DrawableHelper.fill(matrices, this.width - 130, 85, this.width - 5, 115, new RGBColor(35, 35, 35).getValue());
        DrawableHelper.fill(matrices, this.width - 130, 120, this.width - 5, 150, new RGBColor(35, 35, 35).getValue());
        DrawableHelper.fill(matrices, this.width - 130, 155, this.width - 5, 185, new RGBColor(35, 35, 35).getValue());
        RenderSystem.setShaderTexture(0, new Identifier("pteroclient", "textures/panel_widgets.png"));
        DrawableHelper.drawTexture(matrices, this.width - 125, 55, 20, 20, 0, 0, 64, 64, 256, 256);
        DrawableHelper.drawTexture(matrices, this.width - 125, 90, 20, 20, 64, 0, 64, 64, 256, 256);
        DrawableHelper.drawTexture(matrices, this.width - 125, 125, 20, 20, 128, 0, 64, 64, 256, 256);
        DrawableHelper.drawTexture(matrices, this.width - 125, 160, 20, 20, 192, 0, 64, 64, 256, 256);
        textRenderer.draw(matrices, "IP: " + this.ipAddress, this.width - 102, 56, new RGBColor(255, 255, 255).getValue());
        textRenderer.draw(matrices, "Port: " + this.ipPort, this.width - 102, 66, new RGBColor(255, 255, 255).getValue());
        textRenderer.draw(matrices, "CPU: " + (this.cpuUtilization.equals("retrieving...") ? this.cpuUtilization : String.format("%.0f", Float.parseFloat(this.cpuUtilization)) + "%"), this.width - 102, 96, new RGBColor(255, 255, 255).getValue());
        textRenderer.draw(matrices, "RAM: " + this.ramUtilization, this.width - 102, 131, new RGBColor(255, 255, 255).getValue());
        textRenderer.draw(matrices, "Disk: " + this.diskUtilization, this.width - 102, 166, new RGBColor(255, 255, 255).getValue());
        super.render(matrices, mouseX, mouseY, delta);
    }
}