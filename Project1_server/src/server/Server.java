/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import game.Map;
import game.Message;
import static game.Message.Type.*;
import game.Territory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author iremayvaz
 */
public class Server {

    public static int gamerID;
    public static ServerSocket ssocket; // Gelen her bağlantı için kapı (soket)
    public static NewClientListener newClient; // Her yeni bağlantıyı karşıla (Thread başlatır)
    public static ArrayList<SClient> players; // Oyuncuların bağlantı listesi
    
    public static Map newMap;

    public static void Start(int port) throws IOException {
        Server.gamerID = 0;
        Server.ssocket = new ServerSocket(port);
        Server.newClient = new NewClientListener();
        Server.newClient.start();
        Server.players = new ArrayList<>();
        // yeni harita oluştur
        Server.newMap = new Map();
        
        // Harita bölgelerini oluştur.
        Territory afrika = new Territory("Afrika");
        Territory asya = new Territory("Asya");
        Territory avrupa = new Territory("Avrupa");
        Territory avustralya = new Territory("Avustralya");
        Territory guney_amerika = new Territory("Güney Amerika");
        Territory kuzey_amerika = new Territory("Kuzey Amerika");
        
        // Her bölgenin komşularını belirle
        // ATTACK ve DEPLOY için lazım
        // AFRİKA
        afrika.addNeighbours(avrupa);
        afrika.addNeighbours(asya);
        afrika.addNeighbours(kuzey_amerika);
        afrika.addNeighbours(guney_amerika);
        afrika.addNeighbours(avustralya);
        // ASYA
        asya.addNeighbours(afrika);
        asya.addNeighbours(avrupa);
        asya.addNeighbours(avustralya);
        // AVRUPA
        avrupa.addNeighbours(afrika);
        avrupa.addNeighbours(asya);
        avrupa.addNeighbours(kuzey_amerika);
        avrupa.addNeighbours(guney_amerika);
        // AVUSTRALYA
        avustralya.addNeighbours(asya);
        avustralya.addNeighbours(afrika);
        // GÜNEY AMERİKA
        guney_amerika.addNeighbours(kuzey_amerika);
        guney_amerika.addNeighbours(avrupa);
        guney_amerika.addNeighbours(afrika);
        // KUZEY AMERİKA
        kuzey_amerika.addNeighbours(guney_amerika);
        kuzey_amerika.addNeighbours(avrupa);
        kuzey_amerika.addNeighbours(afrika);
        
        // Her territory'yi haritanın bölge listesine ekle
        Server.newMap.addTerritory(afrika);
        Server.newMap.addTerritory(asya);
        Server.newMap.addTerritory(avrupa);
        Server.newMap.addTerritory(avustralya);
        Server.newMap.addTerritory(guney_amerika);
        Server.newMap.addTerritory(kuzey_amerika);
    }

    // players listesini kontrol et ve rakip oyuncu bul
    public static void isOpponentFound(SClient candidate_rival) throws IOException {
        for (SClient player : players) {
            if (player.rival == null && !player.equals(candidate_rival)) {
                player.rival = candidate_rival;
                candidate_rival.rival = player;

                // Rakip adlarını gönder
                String info = candidate_rival.rival.id +"#" + candidate_rival.rival.name;
                Server.sendTo(candidate_rival.id, new Message(Message.Type.OPPONENT_FOUND, info));
                System.out.println(candidate_rival.name + " rakibi " + candidate_rival.rival.name);
                
                String info2 = player.rival.id +"#" + player.rival.name;
                Server.sendTo(player.id, new Message(Message.Type.OPPONENT_FOUND, info2));
                System.out.println(player.name + " rakibi " + player.rival.name);
                
                // Harita oluştur
                Server.createMap(player, candidate_rival);
                
                // send the state
                Server.sendTo(candidate_rival.id, new Message(YOUR_TURN, "your turn")); // Sıra rakipte
                Server.sendTo(player.id, new Message(OPPONENTS_TURN, candidate_rival.name + "'s turn"));
                break;
            }
        }
    }
    
    // rakip bulunduysa harita oluştur.
    public static void createMap(SClient player, SClient rival) throws IOException{
        // oyuncu bilgileri
        Server.newMap.id = player.id;
        Server.newMap.name = player.name;
        // rakip bilgileri
        Server.newMap.rivalId = rival.id;
        Server.newMap.rivalName = rival.name;
        // Toplam territory'ler karışık bir şekilde oyunculara atanır.
        Server.newMap.defaultTerritories(player.id, rival.id);
        // Oyuncuların askerleri bölgelerine rastgele dağıtılır.
        Server.newMap.defaultTroops();
        
        Message mapCreated = new Message(Message.Type.MAP, Server.newMap);
        Server.sendTo(player.id, mapCreated);
        Server.sendTo(rival.id, mapCreated);
    } 

    public static void sendAll(Message message) throws IOException { // Tüm oyunculara bildirim
        for (SClient player : players) {
            player.SendMessage(message);
        }
    }

    public static void sendTo(int id, Message message) throws IOException { // Belirli bir oyuncuya bildirim
        Server.players.get(id).SendMessage(message);
    }
}
