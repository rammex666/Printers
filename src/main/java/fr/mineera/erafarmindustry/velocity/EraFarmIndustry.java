package fr.mineera.erafarmindustry.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "erafarmindustry",
        name = "EraFarmIndustry",
        version = "1.0.0",
        description = "Plugin Base for Velocity",
        authors = {"Mineera"},
        url = "https://mineera.fr"
)

public class EraFarmIndustry {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public EraFarmIndustry(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("has been enabled!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
