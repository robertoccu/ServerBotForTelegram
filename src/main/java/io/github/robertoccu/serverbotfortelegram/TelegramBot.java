package io.github.robertoccu.serverbotfortelegram;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TelegramBot {

    private static String token = null;
    private static String priorityChatID = null;
    private static String logChatID = null;
    private static String log2ChatID = null;
    private static String publicChatID = null;
    private static String urlModel = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

    protected enum MessageType {
            PRIORITY, LOG, LOG2, PUBLIC
    }

    protected static boolean setupBot(FileConfiguration config) {
        token = config.getString("bot.token");
        priorityChatID = config.getString("bot.chatID.priority");
        logChatID = config.getString("bot.chatID.log");
        log2ChatID = config.getString("bot.chatID.log2");
        publicChatID = config.getString("bot.chatID.public");

        if (token == null || priorityChatID == null || logChatID == null || publicChatID == null) {
            return false;
        }

        return true;
    }

    protected static boolean sendMessage(String message, MessageType type) {
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

        String urlString = urlModel.format(urlModel, token, chatID, message);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String response = sb.toString();

            if (response.contains("\"ok\":false")) {
                System.err.println("La respuesta al envio del mensaje ha sido erronea.\n" +
                        "chatID: " + chatID + "\n" +
                        "Mensaje: " + message + "\n" +
                        "Respuesta: " + response + ".");
                return false;
            }

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
