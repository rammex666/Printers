package fr.mineera.erafarmindustry.bukkit;

import fr.mineera.erafarmindustry.bukkit.commands.PrinterCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class EraFarmIndustry extends JavaPlugin {
    File dir = getDataFolder();
    File printers = new File(dir, "printers.yml");
    public FileConfiguration printersconf;
    File messages = new File(dir, "messages.yml");
    public FileConfiguration messagesconf;

    @Override
    public void onEnable() {
        getLogger().info("has been enabled!");
        // load config files
        loadfiles();
        // register commands
        this.getCommand("printer").setExecutor(new PrinterCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled!");
    }


    void loadfiles() {
        if (!printers.exists()) {
            printers.getParentFile().mkdirs();
            saveResource("printers.yml", false);
        }

        printersconf = new YamlConfiguration();
        try {
            printersconf.load(printers);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (!messages.exists()) {
            messages.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesconf = new YamlConfiguration();
        try {
            messagesconf.load(messages);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void savePrinter() {
        try {
            printersconf.save(printers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration getPrinter() {
        return printersconf;
    }

    public File getPrintersFile() {
        return printers;
    }

    public File getMessagesFile() {
        return messages;
    }

    public FileConfiguration getMessages(){
        return messagesconf;
    }
}
