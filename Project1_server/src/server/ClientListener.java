/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import game.Message;
import static game.Message.Type.*;
import game.Territory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Client'tan gelen mesajları dinler SClient tarafı
 *
 * @author iremayvaz
 */
public class ClientListener extends Thread {

    SClient client;

    public ClientListener(SClient c) {
        this.client = c;
    }

    @Override
    public void run() {

        while (!this.client.csocket.isClosed()) {
            try {
                Message in_message = (Message) client.cinput.readObject();
                this.handleIncomingMessage(in_message);
            } catch (IOException ex) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Client'ın işlemleri
    public void handleIncomingMessage(Message message) throws IOException {
        switch (message.message_type) {
            case JOIN_SERVER: // Oyuna bağlanıp ismi girdik. Login sayfasında "GİRİŞ" butonuna bastığımızda gelen mesaj
                client.name = message.content.toString(); // client'ın ismini oyuncu ismiyle eşitle
                System.out.println(client.name + " için rakip aranıyor...");
                Server.sendTo(client.id, new Message(Message.Type.YOUR_ID, client.id));

                // check the opponent
                Server.isOpponentFound(client);
                break;

            case SKIP_TURN:
                Server.sendTo(client.rival.id, new Message(Message.Type.YOUR_TURN, "your turn"));
                Server.sendTo(client.id, new Message(Message.Type.OPPONENTS_TURN, client.rival.name + "'s turn"));
                break;
            case ATTACK:
                ArrayList<Territory> gelenGuncelleme = (ArrayList<Territory>) message.content;
                Server.sendTo(client.rival.id, new Message(Message.Type.ATTACK, gelenGuncelleme));
                Server.sendTo(client.id, new Message(Message.Type.ATTACK, gelenGuncelleme));
                break;
            case DEPLOY:
                ArrayList<Territory> updated = (ArrayList<Territory>) message.content;
                Server.sendTo(client.rival.id, new Message(Message.Type.ATTACK, updated));
                Server.sendTo(client.id, new Message(Message.Type.ATTACK, updated));
                break;
        }
    }
}
