package io.github.robertoccu.serverbotfortelegram;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (ServerBotForTelegram.perms.playerInGroup(event.getPlayer(), "invitado")) {
            TelegramBot.sendMessage("Se ha marchado el invitado " + event.getPlayer().getName()
                    + " del servidor. ¿Volverá?", TelegramBot.MessageType.LOG);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            TelegramBot.sendMessage("¡Hay un nuevo jugador! Acaba de entrar " + event.getPlayer().getName()
                + " al servidor. ¿Quién va a ir a ayudarle?", TelegramBot.MessageType.PRIORITY);
        } else if (ServerBotForTelegram.perms.playerInGroup(event.getPlayer(), "invitado")) {
            TelegramBot.sendMessage("El jugador invitado " + event.getPlayer().getName()
                    + " ha vuelto. Es posible que necesite una ayudita.", TelegramBot.MessageType.LOG);
        }

    }

}
