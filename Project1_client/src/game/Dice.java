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
 * Zar
 *
 * @author iremayvaz
 */
public class Dice implements java.io.Serializable{
    
    public static int roll() { // Zarı at
        return new Random().nextInt(6) + 1;
    }

    public static ArrayList<Integer> rollMultiple(int troopsCount) { // Birlik sayısına göre zar at
        ArrayList<Integer> results = new ArrayList<>(); // zar sonuçlarını tutmak için arrayList
        for (int i = 0; i < troopsCount; i++) { 
            results.add(roll());
        }
        Collections.sort(results); // arrayList'i küçükten büyüğe sıralar
        
        // arrayList'i büyükten küçüğe çevir
        for (int i = 0; i < results.size() / 2; i++) {
            int temp = results.get(i);
            results.set(i, results.get(results.size() - 1 - i));
            results.set(results.size() - 1 - i, temp);
        }
        return results;
    }

    public static ArrayList<Boolean> compareDiceResults(ArrayList<Integer> gamer_results, ArrayList<Integer> opponent_results) { // Zar sonuçlarını karşılaştır
        ArrayList<Boolean> diceResults = new ArrayList<>();
        
        // Her iki liste de boş olabilir, bu durumu kontrol et
        if (gamer_results == null || gamer_results.isEmpty() || 
            opponent_results == null || opponent_results.isEmpty()) {
            return diceResults; // Boş liste döndür
        }
        
        if (gamer_results.size() >= opponent_results.size()) { // gamer eşit veya daha fazla zar attıysa
            for (int i = 0; i < opponent_results.size(); i++) { // sonuçları sırayla karşılaştır
                if (opponent_results.get(i) >= gamer_results.get(i)) { // opponent'in sonucu daha fazlaysa veya eşitse
                    diceResults.add(false); // set yerine add kullanıyoruz - kaybettik
                } else {
                    diceResults.add(true); // set yerine add kullanıyoruz - kazandik
                }
            }
        } else { // opponent daha fazla zar attıysa
            for (int i = 0; i < gamer_results.size(); i++) { // sonuçları sırayla karşılaştır
                if (opponent_results.get(i) < gamer_results.get(i)) { // gamer'ın sonucu daha fazlaysa
                    diceResults.add(true);  // set yerine add kullanıyoruz - kazandik
                } else {
                    diceResults.add(false); // set yerine add kullanıyoruz - kaybettik
                }
            }
        }
        return diceResults;
    }

    public static void updateTroops(ArrayList<Boolean> dice_results, Territory sal, Territory sav) { // Zar sonuçlarının karşılaştırmalarına göre asker sayılarını güncelle
        // Null veya boş liste kontrolü
        if (dice_results == null || dice_results.isEmpty()) {
            return;
        }
        
        for (int i = 0; i < dice_results.size(); i++) {
            if (sal.totalTroop != 0 && sav.totalTroop != 0) {
                if (dice_results.get(i)) { // true ise
                    sav.totalTroop--;
                } else { // false ise
                    sal.totalTroop--;
                }
            }
        }
    }
}