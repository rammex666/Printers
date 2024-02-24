package fr.mineera.erafarmindustry.bukkit.commands;

import fr.mineera.erafarmindustry.bukkit.EraFarmIndustry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrinterCommand implements CommandExecutor {
    EraFarmIndustry plugin;
    public PrinterCommand(EraFarmIndustry plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (args.length == 0) {
            player.sendMessage(plugin.getMessages().getString("administration.printer.printer-create-use"));
            return false;
        }
        if (args[0].equals("create")) {
            if (isPrinterExist(args[1])) {
                player.sendMessage(plugin.getMessages().getString("administration.printer.printer-exist"));
                return false;
            }
            if (isItemStack(itemInHand) && isItem(itemInHand)) {
                player.sendMessage(plugin.getMessages().getString("administration.printer.item-stack"));
                return false;
            } else {
                saveMetaData(itemInHand, args[1], player);
                createPrinter(player, args[1]);
            }
        }
        switch (args[1]) {
            case "delete" -> {
                if (isPrinterExist(args[0])) {
                    plugin.printersconf.set("printers." + args[0], null);
                    plugin.savePrinter();
                    player.sendMessage(plugin.getMessages().getString("administration.printer.printer-delete")
                            .replace("{printer}", args[0]));
                } else {
                    player.sendMessage(plugin.getMessages().getString("administration.printer.printer-not-exist")
                            .replace("{printer}", args[0]));
                }
            }
            case "list" -> {
                if (isPrinterListEmpty("printers")) {
                    player.sendMessage(plugin.getMessages().getString("administration.printer.printer-list-empty"));
                } else {
                    player.sendMessage(plugin.getMessages().getString("administration.printer.printer-list"));
                    for (String key : plugin.printersconf.getConfigurationSection("printers").getKeys(false)) {
                        player.sendMessage(key);
                    }
                }
            }
            case "set" -> {
                if(isPrinterExist(args[0])){
                    switch (args[2]) {
                        case "income" -> {
                            if(SettingsCondition(Integer.valueOf(args[3]))) {
                                plugin.printersconf.set("printers." + args[0] + ".settings.income", Integer.parseInt(args[3]));
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.income")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            } else {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.income")));
                                return false;
                            }
                        }
                        case "income-time" -> {
                            if(SettingsCondition(Integer.valueOf(args[3]))) {
                                plugin.printersconf.set("printers." + args[0] + ".settings.income-time", Integer.parseInt(args[3]));
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.income-time")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            } else {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.income-time")));
                                return false;
                            }
                        }
                        case "max-amount-money" -> {
                            if(SettingsCondition(Integer.valueOf(args[3]))) {
                                plugin.printersconf.set("printers." + args[0] + ".settings.max-amount-money", Integer.parseInt(args[3]));
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.max-amount-money")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            } else {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.max-amount-money")));
                                return false;
                            }
                        }
                        case "max-printer-limit" -> {
                            if(SettingsCondition(Integer.valueOf(args[3]))) {
                                plugin.printersconf.set("printers." + args[0] + ".settings.max-printer-limit", Integer.parseInt(args[3]));
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.max-printer-limit")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            } else {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.max-printer-limit")));
                                return false;
                            }
                        }
                        case "enabled" -> {
                            if(!IsBoolean(args[3])) {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.enabled")));
                                return false;
                            } else {
                                plugin.printersconf.set("printers." + args[0] + ".settings.enabled", Boolean.parseBoolean(args[3]));
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.enabled")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            }
                        }
                        case "structure" -> {
                            if(!IsString(args[3])) {
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.structure")));
                                return false;
                            } else {
                                plugin.printersconf.set("printers." + args[0] + ".settings.structure", args[3]);
                                plugin.savePrinter();
                                player.sendMessage(hex(plugin.getMessages().getString("administration.printer.printer-value.error.structure")
                                        .replace("{printer}", args[0])
                                        .replace("{value}", args[3])));
                            }
                        }
                }
            }
        }
    }
    return false;
    }


    public static String hex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }

    public static boolean IsBoolean(Object valeur) {
        return valeur instanceof Boolean;
    }

    public static boolean IsString(Object valeur) {
        return valeur instanceof String;
    }

    public static boolean IsInteger(Object valeur) {
        try {
            Integer.parseInt(valeur.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isItemStack(ItemStack item) {
        return item != null && item.getAmount() > 1;
    }

    public boolean isItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    public boolean SettingsCondition(Integer valeur) {
        try {
            IsInteger(valeur);
            boolean b = Integer.parseInt(String.valueOf(valeur)) >= 0;
            if (b) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void createPrinter(Player player,String arg){
        plugin.printersconf.set("printers." + arg + ".item", player.getInventory().getItemInMainHand().getType().name());
        plugin.printersconf.set("printers." + arg + ".settings.enabled", false);
        plugin.printersconf.set("printers." + arg + ".settings.income", 1);
        plugin.printersconf.set("printers." + arg + ".settings.income-time", 3600);
        plugin.printersconf.set("printers." + arg + ".settings.max-amount-money", 1000);
        plugin.printersconf.set("printers." + arg + ".settings.structure", "““");
        plugin.savePrinter();
        player.sendMessage(plugin.getMessages().getString("administration.printer.created"));
    }

    public void saveMetaData(ItemStack itemInHand,String arg,Player player ){
        PersistentDataContainer container = itemInHand.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, arg);
        container.set(key, PersistentDataType.STRING, player.getUniqueId().toString());
    }

    public boolean isPrinterExist(String arg){
        return plugin.printersconf.getConfigurationSection("printers").getKeys(false).contains(arg);
    }

    public boolean isPrinterListEmpty(String key){
        return plugin.printersconf.getConfigurationSection(key).getKeys(false).isEmpty();
    }
}


