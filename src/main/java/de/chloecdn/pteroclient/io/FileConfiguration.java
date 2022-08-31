package de.chloecdn.pteroclient.io;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class FileConfiguration {

    private final YamlFile file;

    public FileConfiguration(String path, String name) {
        boolean b = path.endsWith("/");
        boolean b1 = name.endsWith(".yml") || name.endsWith(".yaml");
        this.file = new YamlFile(path + (b ? "" : "/") + name + (b1 ? "" : ".yml"));
        try {
            this.file.createNewFile();
            this.file.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        this.file.set(path, value);
        try {
            this.file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDefault(String path, Object value) {
        this.file.addDefault(path, value);
        try {
            this.file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <A> A get(Class<A> type, String path) {
        return (A) this.file.get(path);
    }

    public YamlFile getFile() {
        return file;
    }
}
