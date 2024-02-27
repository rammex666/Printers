package fr.mineera.erafarmindustry.bukkit;

import fr.mineera.erafarmindustry.bukkit.commands.PrinterCommand;
import fr.mineera.erafarmindustry.bukkit.events.PrinterBlockPlaceEvent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class EraFarmIndustry extends JavaPlugin {
    private File dir;
    private File printers;
    private FileConfiguration printersConf;
    private File messages;
    private FileConfiguration messagesConf;

    @Override
    public void onEnable() {
        getLogger().info("has been enabled!");
        loadFiles();
        this.getCommand("printer").setExecutor(new PrinterCommand(this));
        this.getServer().getPluginManager().registerEvents(new PrinterBlockPlaceEvent(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled!");
    }

    private void loadFiles() {
        dir = getDataFolder();
        printers = new File(dir, "printers.yml");
        messages = new File(dir, "messages.yml");

        if (!printers.exists()) {
            printers.getParentFile().mkdirs();
            saveResource("printers.yml", false);
        }

        printersConf = loadConfiguration(printers);

        if (!messages.exists()) {
            messages.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConf = loadConfiguration(messages);
    }

    private FileConfiguration loadConfiguration(File file) {
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public void savePrinter() {
        try {
            printersConf.save(printers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration getPrinter() {
        return printersConf;
    }

    public File getPrintersFile() {
        return printers;
    }

    public File getMessagesFile() {
        return messages;
    }

    public FileConfiguration getMessages(){
        return messagesConf;
    }
}