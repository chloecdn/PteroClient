package de.chloecdn.pteroclient;

import de.chloecdn.pteroclient.io.FileConfiguration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

    private static Client instance;
    private FileConfiguration config;

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    public void initialize() {
        this.config = new FileConfiguration("./", "pteroclient_config");
        this.config.addDefault("userKey", "<user-key>");
        this.config.addDefault("panelKey", "<panel-key>");
        this.config.addDefault("panelUrl", "https://panel.yourdomain.com");
        this.config.addDefault("sftpServerAddress", "127.0.0.1");
        this.config.addDefault("sftpPort", "22");
        this.config.addDefault("sftpUsername", "<sftp-username>");
        this.config.addDefault("sftpPassword", "<sftp-password>");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void onInitializeClient() {
    }
}
