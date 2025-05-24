/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import static game.Dice.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Oyuncu
 *
 * @author iremayvaz
 */
public class Player implements java.io.Serializable {

    public int id;
    public String name;

    public ArrayList<Territory> territories; // topraklar(ülkeler)
    public ArrayList<Integer> zarSonuclari; // her zar atımı sonrası sonuçlar tutulur

    public boolean willAttack; // kim saldıran kim savunan

    public Player() {
        this.territories = new ArrayList<>();
        this.zarSonuclari = new ArrayList<>();
        this.willAttack = false;
    }

    public void addTerritory(Territory takenTerritory) { // If we win the roll-a-dice session,
        this.territories.add(takenTerritory);       // then add the territory to our all_territories
    }

    public void removeTerritory(Territory takenTerritory) { // If we lose the roll-a-dice session,
        this.territories.remove(takenTerritory);       // then remove the territory from our all_territories
    }
}
