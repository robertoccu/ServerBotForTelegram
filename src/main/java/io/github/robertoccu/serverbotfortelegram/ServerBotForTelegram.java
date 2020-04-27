package io.github.robertoccu.serverbotfortelegram;

import io.github.robertoccu.serverbotfortelegram.TelegramBot.MessageType;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerBotForTelegram extends JavaPlugin {

  protected static Permission perms = null;
  protected static FileConfiguration config = null;

  public void onEnable() {
    this.saveDefaultConfig();
    config = this.getConfig();

    if (!TelegramBot.setupBot(config)) { // If lack of properly filled config.yml
      getLogger().warning("ServerBotForTelegram need configuration. See plugin documentation.");
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    if (!setupPermissions()) { // If Permission Service is provided.
      getLogger().warning("ServerBotForTelegram can't found Permissions. Is Vault installed?");
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    // Event Listeners
    PlayerListener playerListener = new PlayerListener();
    getServer().getPluginManager().registerEvents(playerListener, this);

    try { // Command Errors
      getCommand("avisargm").setExecutor(new Commands());
      getCommand("priorityTelegram").setExecutor(new Commands());
      getCommand("logTelegram").setExecutor(new Commands());
      getCommand("log2Telegram").setExecutor(new Commands());
      getCommand("publicTelegram").setExecutor(new Commands());
    } catch (NullPointerException e) {
      getLogger().warning("ServerBotForTelegram encountered an error when loading commands and can't being enabled. Contact developer. Sorry.");
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    // All OK
    TelegramBot.sendMessageAsync(null,"Acaba de abrirse el servidor o se ha habilitado ServerBot. ¡Server funcionado!", MessageType.LOG);
    getLogger().info("ServerBotForTelegram is ready!");

  }

  public void onDisable() {
    TelegramBot.sendMessage(null,"El servidor se ha cerrado o ServerBot ha sido deshabilitado. ¿Se ha caido?", MessageType.PRIORITY);
  }

  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    try {
      perms = rsp.getProvider();
      return true;
    } catch (NullPointerException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static String dynmapLink(String worldName, Location playerLocation) {
    return String.format("http://vip.mcers.es:8123/index.html"
            + "?worldname=%s&mapname=surface&zoom=8&x=%s&y=%s&z=%s",
        worldName,
        playerLocation.getBlockX(),
        playerLocation.getBlockY(),
        playerLocation.getBlockZ());
  }
}