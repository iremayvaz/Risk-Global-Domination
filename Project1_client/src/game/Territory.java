/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 * Haritadaki bölgeler
 *
 * @author iremayvaz
 */
public class Territory implements java.io.Serializable {

    public String name; // bolge ismi
    public int totalTroop; // bolgedeki asker sayısı
    public ArrayList<Territory> neighbours; // komsulari

    public JButton bolge_butonu;
    public Player owner; // zar sonuclarina ulasalim
    public int playerID;

    public Territory(String name) {
        this.name = name;
        this.totalTroop = 0;
        this.neighbours = new ArrayList<>();
    }

    // JToggleButton set et
    public void setButton(JButton btn) {
        this.bolge_butonu = btn;
    }

    // Komşuluk ilişkileri
    public void addNeighbours(Territory newNeighbour) {
        this.neighbours.add(newNeighbour);
    }

    public boolean isNeighbour(Territory other) {
        return this.neighbours.contains(other);
    }

    // Yeniden asker konuşlandırma için
    public void addTroops(int count) { // Different territories brings different number of troops
        this.totalTroop += count; // Bölgedeki toplam asker sayısı güncellenir
    }

    public void removeTroops(int count) { // Different territories loses different number of troops
        this.totalTroop -= count;
    }

    public int howManyDices(boolean willAttack) { // O bölgedeki total asker sayısına göre zar sayısı belirle
        int diceCount = 0;

        if (!willAttack) { // saldırmıyoruz, savunuyoruz

            if (this.totalTroop == 1) {
                diceCount = 1;
            } else {
                diceCount = 2;
            }

        } else { // saldırıyoruz
            if (this.totalTroop == 1) {
                diceCount = 0;
            } else if (this.totalTroop == 2) {
                diceCount = 1;
            } else if (this.totalTroop == 3) {
                diceCount = 2;
            } else {
                diceCount = 3;
            }
        }
        return diceCount;
    }

    public boolean moveTroops(Territory to) { // askerleri bölgelere dağıt
        if (this.totalTroop > 1
                && // Savunma için 1 kişiyi bölgesinde bırakmalı
                this.playerID == to.playerID
                && // Asker konuşlandırma, aynı bölgelerde yapılır. Rakibin bölgesini fethetmeden oraya asker konuşlandıramam.
                this.isNeighbour(to)) { // Asker konuşlandırma komşu bölgeler arasında yapılabilir.
            this.totalTroop--;
            to.totalTroop++;
            return true;
        } else {
            return false;
        }
    }

    public boolean attack(Territory to) {
        if (this.totalTroop <= 1
                || this.playerID == to.playerID
                || !this.isNeighbour(to)) {
            System.out.println("Attack conditions not met");
            return false;
        }
        // Null check ve initialization
        if (this.owner == null || this.owner.zarSonuclari == null) {
            System.out.println("HATA: Attacker owner null!");
            return false;
        }
        if (to.owner == null || to.owner.zarSonuclari == null) {
            System.out.println("HATA: Defender owner null!");
            return false;
        }

        // Initialize dice results if null
        if (this.owner.zarSonuclari == null) {
            this.owner.zarSonuclari = new ArrayList<>();
        }
        if (to.owner.zarSonuclari == null) {
            to.owner.zarSonuclari = new ArrayList<>();
        }

        // Attack until one side has no troops left or attacker gives up
        //while (this.totalTroop > 1 && to.totalTroop > 0) {
        // Attacker rolls dice
        System.out.println("==============SALDIRI ÖNCESİ================");
        int attackerDiceCount = this.howManyDices(true);
        this.owner.zarSonuclari = Dice.rollMultiple(attackerDiceCount);
        System.out.println("Attacker " + this.name + " troops: " + this.totalTroop
                + " rolled: " + this.owner.zarSonuclari);

        // Defender rolls dice
        int defenderDiceCount = to.howManyDices(false);
        to.owner.zarSonuclari = Dice.rollMultiple(defenderDiceCount);
        System.out.println("Defender " + to.name + " troops: " + to.totalTroop
                + " rolled: " + to.owner.zarSonuclari);

        // Compare results
        ArrayList<Boolean> results = Dice.compareDiceResults(
                this.owner.zarSonuclari, to.owner.zarSonuclari);
        System.out.println("Results: " + results);

        // Update troops based on results
        Dice.updateTroops(results, this, to);

        System.out.println("==============SALDIRI SONRASI================");
        System.out.println("After battle - Attacker: " + this.totalTroop
                + " Defender: " + to.totalTroop);

        // Clear dice results for next round
        this.owner.zarSonuclari.clear();
        to.owner.zarSonuclari.clear();
        //}

        // If defender is conquered
        if (to.totalTroop == 0) {
            // Transfer ownership
            to.owner = this.owner;
            to.playerID = this.playerID;
            // Remove from original owner
            to.owner.territories.remove(to);
            this.owner.territories.add(to);

            // Move troops (leave at least 1 in original territory)
            int movingTroops = this.totalTroop / 2;
            to.totalTroop = movingTroops;
            this.totalTroop -= movingTroops;

            System.out.println(to.name + " conquered by " + this.owner.name);
        } else if (this.totalTroop == 0) {
            // Transfer ownership
            this.owner = to.owner;
            this.playerID = to.playerID;
            // Remove from original owner
            this.owner.territories.remove(to);
            to.owner.territories.add(this);

            // Move troops (leave at least 1 in original territory)
            int movingTroops = to.totalTroop / 2;
            this.totalTroop = movingTroops;
            to.totalTroop -= movingTroops;

            System.out.println(to.name + " conquered by " + to.owner.name);
        }

        return true;
    }
}
