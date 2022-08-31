package de.chloecdn.pteroclient.ui.screen;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.application.entities.ApplicationAllocation;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import de.chloecdn.pteroclient.ui.widget.TextFieldWidget;
import de.chloecdn.pteroclient.util.RGBColor;
import de.chloecdn.pteroclient.util.TextRendering;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CreateServerScreen extends Screen {

    private final PteroClient pteroClient;
    private final PteroApplication pteroApp;

    private TextFieldWidget serverName;
    private TextFieldWidget serverOwner;
    private TextFieldWidget maxMemory;
    private TextFieldWidget maxStorage;
    private TextFieldWidget egg;
    private TextFieldWidget startupCommand;


    public CreateServerScreen(PteroClient client, PteroApplication app) {
        super(Text.of("Create Server"));
        this.pteroClient = client;
        this.pteroApp = app;
    }

    @Override
    protected void init() {
        int i = 32;
        this.serverName = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter Server-Name"), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, List.of(Text.of("The Name of the Server to create."), Text.of("Note: This cannot be empty or blank.")), x, y);
        });
        i += 24;
        this.serverOwner = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter Email of the Owner"), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, List.of(Text.of("The Email of the Server-Owner to use."), Text.of("Note: If empty, this will Assign the default Admin-User.")), x, y);
        });
        i += 24;
        this.maxMemory = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter amount of max Memory (MB)"), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, List.of(Text.of("The amount of max Memory to use."), Text.of("Note: This cannot be empty or blank.")), x, y);
        });
        i += 24;
        this.maxStorage = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter amount of max Disk-Space (MB)"), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, List.of(Text.of("The amount of max Disk-Space to use."), Text.of("Note: This cannot be empty or blank.")), x, y);
        });
        i += 24;
        this.egg = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter the ID of the egg."), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, List.of(Text.of("The ID of the egg to use."), Text.of("Note: This cannot be empty or blank."), Text.of("Note: By default, Eggs go from 1-5, from 'Vanilla Minecraft' up to 'Sponge (SpongeVanilla)'."), Text.of("This may have changed in your Panel when you modified/edited any eggs.")), x, y);
        });
        i += 24;
        this.startupCommand = new TextFieldWidget(this.textRenderer, this.width - 110, i, 100, 20, Text.of("Enter a custom startup command, if needed."), (textField, matrices, x, y) -> {
            this.renderTooltip(matrices, Text.of("The startup command used to start the server."), x, y);
        });
        i += 24;
        this.startupCommand.setMaxLength(512);
        this.startupCommand.setText("java -Xms128M -XX:MaxRAMPercentage=95.0 -jar {{SERVER_JARFILE}}");

        this.addDrawableChild(serverName);
        this.addDrawableChild(serverOwner);
        this.addDrawableChild(maxMemory);
        this.addDrawableChild(maxStorage);
        this.addDrawableChild(egg);
        this.addDrawableChild(startupCommand);

        this.addDrawableChild(new ButtonWidget(this.width - 110, this.height - 30, 100, 20, Text.of("Create"), var -> {
            List<String> failures = new ArrayList<>();
            failures.add(this.validateOption(() -> !this.serverName.getText().isBlank(), "Server-Name empty."));
            failures.add(this.validateOption(() -> this.pteroApp.retrieveUsersByEmail(this.serverOwner.getText(), true).execute().get(0).getEmail(), "Server-Owner cannot be found."));
            failures.add(this.validateOption(() -> !this.maxMemory.getText().isBlank() || Long.parseLong(this.maxMemory.getText()) >= 1000, "Max-Memory empty or smaller than 1000."));
            failures.add(this.validateOption(() -> !this.maxStorage.getText().isBlank() || Long.parseLong(this.maxStorage.getText()) >= 1000, "Max-Storage empty or smaller than 1000."));
            failures.add(this.validateOption(() -> this.pteroApp.retrieveEggById(this.pteroApp.retrieveNestById(1).execute(), Integer.parseInt(this.egg.getText())).execute(), "Egg empty or cannot be found."));
            failures.add(this.validateOption(() -> !this.startupCommand.getText().isBlank(), "Startup Commmand empty."));
            ApplicationAllocation appAllocation = null;
            for (ApplicationAllocation allocation : this.pteroApp.retrieveAllocations().execute()) {
                if (allocation.isAssigned()) {
                    continue;
                }
                appAllocation = allocation;
                break;
            }
            ApplicationAllocation alloc = appAllocation;
            failures.add(this.validateOption(() -> alloc != null, "No available or unassigned Allocation found."));
            failures.removeIf(s -> s.equals("none"));
            if (failures.isEmpty()) {
                this.pteroApp.createServer().setName(
                                this.serverName.getText())
                        .setOwner(this.pteroApp.retrieveUsersByEmail(this.serverOwner.getText(), true).execute().get(0))
                        .setMemory(Long.parseLong(this.maxMemory.getText()), DataType.MB)
                        .setDisk(Long.parseLong(this.maxStorage.getText()), DataType.MB)
                        .setEgg(this.pteroApp.retrieveEggById(this.pteroApp.retrieveNestById(1).execute(), Integer.parseInt(this.egg.getText())).execute())
                        .setStartupCommand(this.startupCommand.getText())
                        .setAllocation(alloc)
                        .execute();
                System.out.println("Successfully Created Server.");
            } else {
                this.client.setScreen(new CreateServerFailureScreen(failures.toArray(new String[]{})));
            }
        }));
    }

    @Override
    public void close() {
        this.client.setScreen(new MainPanelScreen());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        int i = 38;
        String text;
        text = "Server-Name";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
        text = "Server-Owner";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
        text = "Max Memory";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
        text = "Max Storage";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
        text = "Egg";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
        text = "Startup-Command";
        TextRendering.drawText(this.width - (textRenderer.getWidth(text) + 120), i, text, new RGBColor(100, 100, 100));
        i += 24;
    }

    public String validateOption(Validation v, String failMessage) {
        try {
            if (v.validate() instanceof Boolean)
                return (boolean) v.validate() ? "none" : failMessage;
            return v.validate() != null ? "none" : failMessage;
        } catch (Exception e) {
            return failMessage;
        }
    }

    public interface Validation {
        Object validate();
    }

    public class CreateServerFailureScreen extends Screen {

        private String[] failures;

        public CreateServerFailureScreen(String... failures) {
            super(Text.of("Failed to create Server."));
            this.failures = failures;
        }

        @Override
        protected void init() {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 200, this.height - 32, 200, 20, Text.of("Try again"), var -> {
                this.client.setScreen(new CreateServerScreen(pteroClient, pteroApp));
            }));
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.renderBackground(matrices);
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
            super.render(matrices, mouseX, mouseY, delta);
            DrawableHelper.drawCenteredText(matrices, this.textRenderer, "Failed to create Server. The Following errors occured: ", this.width / 2, this.height / 4, new RGBColor(255, 0, 0).getValue());
            int i = (this.height / 4) + 13;
            for (String failure : this.failures) {
                DrawableHelper.drawCenteredText(matrices, this.textRenderer, failure, this.width / 2, i, new RGBColor(255, 255, 0).getValue());
                i += 13;
            }
        }
    }
}
