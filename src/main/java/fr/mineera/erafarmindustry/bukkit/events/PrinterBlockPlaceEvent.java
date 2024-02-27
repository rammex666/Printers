package fr.mineera.erafarmindustry.bukkit.events;

import fr.mineera.erafarmindustry.bukkit.EraFarmIndustry;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PrinterBlockPlaceEvent implements Listener {
    private final EraFarmIndustry plugin;

    public PrinterBlockPlaceEvent(EraFarmIndustry plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        ItemStack itemBeforeUse = event.getPlayer().getItemInHand();
        String name = itemBeforeUse.getItemMeta().getDisplayName()
                .replace("&6","")
                .replace("&n","");
        if (!isPrinterExist(name))
            return;
        if (event.getBlock().getState() instanceof TileState) {
            TileState tileState = (TileState) event.getBlockPlaced().getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(getPlugin(), "printer-name");
            container.set(key, PersistentDataType.STRING, name);
            tileState.update();
        }
    }

    public boolean isPrinterExist(String arg){
        return getPlugin().getPrinter().getConfigurationSection("printers").getKeys(false).contains(arg);
    }

    public EraFarmIndustry getPlugin() {
        return plugin;
    }
}