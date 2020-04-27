package io.github.robertoccu.serverbotfortelegram;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

import java.time.Duration;
import java.time.Instant;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class TelegramBot {

    private static String token = null;
    private static String priorityChatID = null;
    private static String logChatID = null;
    private static String log2ChatID = null;
    private static String publicChatID = null;

    protected enum MessageType {
        PRIORITY, LOG, LOG2, PUBLIC
    }

    protected static boolean setupBot(FileConfiguration config) {
        token = config.getString("bot.token");
        priorityChatID = config.getString("bot.chatID.priority");
        logChatID = config.getString("bot.chatID.log");
        log2ChatID = config.getString("bot.chatID.log2");
        publicChatID = config.getString("bot.chatID.public");

        return token != null && priorityChatID != null && logChatID != null && publicChatID != null;
    }

    protected static void sendMessageAsync(final CommandSender sender, final String message, final MessageType type) {
        getServer().getScheduler().runTaskAsynchronously(ServerBotForTelegram.getPlugin(ServerBotForTelegram.class),
            () -> sendMessage(sender, message, type));

    }

    protected static void sendMessage(final CommandSender sender, final String message, MessageType type) {
        Instant start = Instant.now();
        String chatID = "";
        switch (type) {
            case PRIORITY:
                chatID = priorityChatID;
                break;
            case LOG:
                chatID = logChatID;
                break;
            case LOG2:
                chatID = log2ChatID;
                break;
            case PUBLIC:
                chatID = publicChatID;
                break;
        }

        String fail_message = "";
        try { // [POST] sendMessage
            // Request
            HttpResponse<String> response = Unirest.post(String.format("https://api.telegram.org/bot%s/sendMessage",token))
                .header("accept", "application/json")
                .field("chat_id", chatID)
                .field("text", message)
                .field("parse_mode", "HTML")
                .asString();

            // Response
            if (!response.getBody().contains("\"ok\":true")) {
                fail_message = "La respuesta al envio del mensaje ha sido erronea.\n" +
                    "chatID: " + chatID + "\n" +
                    "Mensaje: " + message + "\n" +
                    "Respuesta: " + response + ".";
            }

        } catch (Exception e) {
            fail_message = "Se ha producido un error desconocido.";
            e.printStackTrace();
        }

        // Result notification to user
        if (fail_message.equals("") && (sender != null)) {
            sender.sendMessage(ChatColor.AQUA+"Mensaje enviado correctamente: "
                +ChatColor.RESET+message);
        } else if (sender != null) {
            sender.sendMessage(ChatColor.RED+"Lo sentimos, no se ha podido enviar el mensaje: "
                +ChatColor.RESET+message);
            System.err.println(fail_message);
        }

        // Time elapsed
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        getLogger().info("Message sent. Time elapsed: "+ timeElapsed.toMillis() +" milliseconds");
    }
}