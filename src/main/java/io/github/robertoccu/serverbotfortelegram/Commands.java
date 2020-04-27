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
            case "avisargm":
                if (sender instanceof Player && sender.hasPermission("serverBotForTelegram.ayuda") && args.length != 0) {
                    Player player = (Player)sender;
                    String primaryGroup;
                    try {
                        primaryGroup = ServerBotForTelegram.perms.getPrimaryGroup(player);
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                        primaryGroup = " _ungrouped_ ";
                    }
                    String message = "*¡Un usuario pide ayuda!* " +
                            player.getName() + " [" + primaryGroup + "] (" + player.getLocation().getBlockX() + ", "
                            + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "): "
                            + String.join(" ", args);
                    TelegramBot.sendMessageAsync(sender, message, TelegramBot.MessageType.PRIORITY);
                } else if (!(sender instanceof Player)){
                    sender.sendMessage(MESSAGE_SENDER_NOT_PLAYER);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "priorityTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args);
                    TelegramBot.sendMessageAsync(sender, messageUrl, TelegramBot.MessageType.PRIORITY);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "logTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args);
                    TelegramBot.sendMessageAsync(sender, messageUrl, TelegramBot.MessageType.LOG);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "log2Telegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args);
                    TelegramBot.sendMessageAsync(sender, messageUrl, TelegramBot.MessageType.LOG2);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
            case "publicTelegram":
                if (sender.hasPermission("serverBotForTelegram.sendRawMessages")) {
                    messageUrl = String.join(" ", args);
                    TelegramBot.sendMessageAsync(sender, messageUrl, TelegramBot.MessageType.PUBLIC);
                } else {
                    sender.sendMessage(MESSAGE_ERROR_PERMISSIONS);
                }
                return true;
        }
        return false;
    }
}
