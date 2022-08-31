package de.chloecdn.pteroclient.ui.screen;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.exceptions.LoginException;
import de.chloecdn.pteroclient.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class MainPanelScreen extends Screen {

    private PteroClient pteroClient;
    private PteroApplication pteroApp;

    public MainPanelScreen() {
        super(Text.of("Pterodactyl-Panel"));
        try {
            this.pteroClient = PteroBuilder.createClient(Client.getInstance().getConfig().get(String.class, "panelUrl"), Client.getInstance().getConfig().get(String.class, "userKey"));
            this.pteroApp = PteroBuilder.createApplication(Client.getInstance().getConfig().get(String.class, "panelUrl"), Client.getInstance().getConfig().get(String.class, "panelKey"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            this.pteroClient.retrieveAccount();
            this.pteroApp.retrieveAllocations();
        } catch (LoginException e) {
            e.printStackTrace();
            MinecraftClient.getInstance().stop();
        }
    }

    @Override
    protected void init() {
        List<ClientServer> servers = pteroClient.retrieveServers().execute();
        int y = 32;
        boolean b = false;
        for (ClientServer server : servers) {
            this.addDrawableChild(new ButtonWidget(16, y, 90, 20, Text.of(server.getName()), var -> {
                this.client.setScreen(new ServerPanelScreen(server));
            }));
            y += 24;
            if (!b) b = true;
        }
        if (!b) {
            ButtonWidget button_no_servers = this.addDrawableChild(new ButtonWidget(16, 32, 90, 20, Text.of("Nothing found."), var -> {
            }));
            button_no_servers.active = false;
        }

        this.addDrawableChild(new ButtonWidget(this.width - 100, this.height - 54, 90, 20, Text.of("Create Server"), var -> {
            this.client.setScreen(new CreateServerScreen(this.pteroClient, this.pteroApp));
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
