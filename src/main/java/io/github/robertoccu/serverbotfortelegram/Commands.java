package io.github.robertoccu.serverbotfortelegram;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private static final String MESSAGE_SENDER_NOT_PLAYER = ChatColor.RED+"Sender must be a player.";
    private static final String MESSAGE_ERROR_PERMISSIONS = ChatColor.RED+"You don't have permissions to use this command.";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String messageUrl;
        switch (cmd.getName()) {
            case "ayuda":
                if (sender instanceof Player && sender.hasPermission("serverBotForTelegram.ayuda") && args.length != 0) {
                    Player player = (Player)sender;
                    String message = "¡Un usuario pide ayuda! " +
                            player.getName() + " [" + ServerBotForTelegram.perms.getPrimaryGroup(player) + "] (" + player.getLocation().getBlockX() + ", "
                            + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "): "
                            + String.join(" ", args);
                    messageUrl = message.replace(" ", "%20");
                    if (TelegramBot.sendMessage(messageUrl, TelegramBot.MessageType.LOG)) {
                        player.sendMessage(ChatColor.GREEN+"Se ha enviado el mensaje. Los GM intentarán solventar tu problema lo antes posible pero no recibirás una respuesta. Abusar de este método de contacto puede provocar sanciones. Este es el mensaje que has enviado: " + String.join(" ", args));
                    } else {
                        player.sendMessage(ChatColor.RED+"El mensaje de ayuda no ha podido ser enviado. Ha sido un error nuestro, lo sentimos. Prueba a contactar a un GM por Discord (Información sobre Discord: https://goo.gl/o4vs2f )");
                    }
                } else if (!(sender instanceof Player)){
                    sender.sendMessage(MESSAGE_SENDER_NOT_PLAYER);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "priorityTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args).replace(" ", "%20");
                    if (TelegramBot.sendMessage(messageUrl, TelegramBot.MessageType.PRIORITY)) {
                        sender.sendMessage(ChatColor.AQUA+"Mensaje enviado: "+ChatColor.RESET+String.join(" ", args));
                    } else {
                        sender.sendMessage(ChatColor.RED+"No se ha enviado el mensaje: "+ChatColor.RESET+String.join(" ", args));
                    }
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "logTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args).replace(" ", "%20");
                    if (TelegramBot.sendMessage(messageUrl, TelegramBot.MessageType.LOG)) {
                        sender.sendMessage(ChatColor.AQUA+"Mensaje enviado: "+ChatColor.RESET+String.join(" ", args));
                    } else {
                        sender.sendMessage(ChatColor.RED+"No se ha enviado el mensaje: "+ChatColor.RESET+String.join(" ", args));
                    }
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "publicTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args).replace(" ", "%20");
                    if (TelegramBot.sendMessage(messageUrl, TelegramBot.MessageType.PUBLIC)) {
                        sender.sendMessage(ChatColor.AQUA+"Mensaje enviado: "+ChatColor.RESET+String.join(" ", args));
                    } else {
                        sender.sendMessage(ChatColor.RED+"No se ha enviado el mensaje: "+ChatColor.RESET+String.join(" ", args));
                    }
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
        }
        return false;
    }
}
