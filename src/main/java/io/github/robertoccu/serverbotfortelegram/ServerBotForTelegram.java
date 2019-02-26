package io.github.robertoccu.serverbotfortelegram;

import io.github.robertoccu.serverbotfortelegram.TelegramBot.MessageType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.permission.Permission;

public class ServerBotForTelegram extends JavaPlugin {

  protected static Permission perms = null;
  protected static FileConfiguration config = null;

  public void onEnable() {
    this.saveDefaultConfig();
    config = this.getConfig();
    if (!TelegramBot.setupBot(config)) { // If lack of properly filled config.yml
      getLogger().info("ServerBotForTelegram need configuration. See plugin documentation.");
    } else {
      if (setupPermissions()) { // If Permission Service is provided.
        PlayerListener playerListener = new PlayerListener();
        getServer().getPluginManager().registerEvents(playerListener, this);
        getCommand("ayuda").setExecutor(new Commands());
        getCommand("priorityTelegram").setExecutor(new Commands());
        getCommand("logTelegram").setExecutor(new Commands());
        getCommand("log2Telegram").setExecutor(new Commands());
        getCommand("publicTelegram").setExecutor(new Commands());
        TelegramBot.sendMessage("Acaba de abrirse el servidor o se ha habilitado ServerBot. ¡Server funcionado!", MessageType.LOG);
      } else {
        getLogger().info("ServerBotForTelegram can't found Permissions. Is Vault installed?");
      }
      getLogger().info("ServerBotForTelegram is ready!");
    }
  }

  public void onDisable() {
    TelegramBot.sendMessage("El servidor se ha cerrado o ServerBot ha sido deshabilitado. ¿Se ha caido?", MessageType.PRIORITY);
  }

  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    perms = rsp.getProvider();
    return perms != null;
  }
}
