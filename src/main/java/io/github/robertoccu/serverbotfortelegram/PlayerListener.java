package io.github.robertoccu.serverbotfortelegram;

import static org.bukkit.Bukkit.getLogger;

import io.github.robertoccu.serverbotfortelegram.TelegramBot.MessageType;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (ServerBotForTelegram.perms.playerInGroup(event.getPlayer(), "invitado")) {
            TelegramBot.sendMessageAsync(null, "Se ha <b>marchado</b> el invitado " + event.getPlayer().getName()
                    + " del servidor. ¿Volverá?", TelegramBot.MessageType.LOG);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            TelegramBot.sendMessageAsync(null,"¡Hay un <b>nuevo jugador</b>! Acaba de entrar " + event.getPlayer().getName()
                + " al servidor. ¿Quién va a ir a ayudarle?", TelegramBot.MessageType.LOG);
        } else if (ServerBotForTelegram.perms.playerInGroup(event.getPlayer(), "invitado")) {
            TelegramBot.sendMessageAsync(null,"El jugador invitado " + event.getPlayer().getName()
                    + " <b>ha vuelto</b>. Es posible que necesite una ayudita.", TelegramBot.MessageType.LOG);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();

        if (advancement.getKey().toString().equals("minecraft:story/mine_diamond")) {
            TelegramBot.sendMessageAsync(null,
                "El jugador " + event.getPlayer().getName() + " ha conseguido el logro "
                    + "<b>\"Diamonds!\"</b> en las coordenadas "
                    + "<a href=\""
                    + ServerBotForTelegram.dynmapLink(
                        player.getWorld().getName(), player.getLocation())
                    + "\">" + player.getLocation().getBlockX() + ", "
                    + player.getLocation().getBlockY() + ", "
                    + player.getLocation().getBlockZ() + ", "
                    + player.getWorld().getName() + "</a>.",
                MessageType.PRIORITY);

        } else if (advancement.getKey().toString().equals("minecraft:story/shiny_gear")) {
            TelegramBot.sendMessageAsync(null,
                "El jugador " + event.getPlayer().getName() + " ha conseguido el logro "
                    + "<b>\"Cover me with Diamonds\"</b> en las coordenadas "
                    + "<a href=\""
                    + ServerBotForTelegram.dynmapLink(
                    player.getWorld().getName(), player.getLocation())
                    + "\">" + player.getLocation().getBlockX() + ", "
                    + player.getLocation().getBlockY() + ", "
                    + player.getLocation().getBlockZ() + ", "
                    + player.getWorld().getName() + "</a>.",
                MessageType.PRIORITY);
        }
    }

}
