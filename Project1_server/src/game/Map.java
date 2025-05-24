/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Harita
 *
 * @author iremayvaz
 */
public class Map implements java.io.Serializable {
    
    public int defaultPlayerTroop = 9;
    public int defaultOpponentTroop = 9;

    public ArrayList<Territory> all_territories; // Haritadaki fethedilecek bölgeler
    public ArrayList<Territory> player_territories; // oyuncunun bölgeleri
    public ArrayList<Territory> opponent_territories; // rakibin bölgeleri
    
    public int id;
    public int rivalId;
    public String name;
    public String rivalName;

    public Map() {
        this.all_territories = new ArrayList<>();
        this.player_territories = new ArrayList<>();
        this.opponent_territories = new ArrayList<>();
    }

    public void addTerritory(Territory territory) {
        this.all_territories.add(territory);
    }

    public void defaultTerritories(int playerID, int rivalID) { // Oyun başlamadan oyunculara yer dağıt.
        Collections.shuffle(all_territories); // Bölgeler listesini karıştır

        for (int i = 0; i < all_territories.size(); i++) { // karıştırılan listenin 
            if (i < 3) { // ilk 3'ü player'a
                all_territories.get(i).playerID = playerID;
                player_territories.add(all_territories.get(i)); // bölgenin oyuncusuna hem oyuncuyu ekler hem de oyuncunun bölge listesine ekler
            } else {
                all_territories.get(i).playerID = rivalID;
                opponent_territories.add(all_territories.get(i));
            }

        }
    }
    
    public void defaultTroops(){ // Oyun başlamadan oyuncuların askerlerini dağıt
        for(int i = 0; i < 3; i++){
            player_territories.get(i).addTroops(1); // Her bölgede en az 1 asker olsun
            this.defaultPlayerTroop--; // olması gereken toplam asker sayısı azaltılır.
            opponent_territories.get(i).addTroops(1); // Her bölgede en az 1 asker olsun
            this.defaultOpponentTroop--;
        }
        
        Random rand = new Random();
        
        while (this.defaultPlayerTroop > 0) { // Player'ın askerlerini bölgelerine dağıt
            int i = rand.nextInt(3); // 3 bölgeye rastgele birer tane asker ekler
            player_territories.get(i).addTroops(1);
            this.defaultPlayerTroop--;
        }
        
        while (this.defaultOpponentTroop > 0) { // Opponent'in askerlerini bölgelerine dağıt
            int i = rand.nextInt(3); 
            opponent_territories.get(i).addTroops(1);
            this.defaultOpponentTroop--;
        }
    }
}
