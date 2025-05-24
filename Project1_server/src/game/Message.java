/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * Neden Serializable tabanlı da String tabanlı değil? Çünkü içerik her şey
 * olabilir. Projemiz 2 kişilik bir "oyun" projesi Oyun strateji oyunu ve çok
 * veri iletimi gerekebilir.
 *
 * @author iremayvaz
 */
public class Message implements java.io.Serializable {

    public enum Type // Mesaj türleri
    {

        JOIN_SERVER, // Sunucuya katılma isteği 
        YOUR_ID, // Oyuncuya id'sini gönderir
        OPPONENT_FOUND, // Rakip bulundu, oyun başlasın
        MAP,
        NAME,
        
        SKIP_TURN, // Sıra pas geçme
        YOUR_TURN, // oyuncunun sırası
        OPPONENTS_TURN, // rakibin sırası
        ATTACK, // Saldırı
        DEPLOY, // Konuşlandırma
        
        GAME_OVER, // Oyun sonu
        DISCONNECT, // Bağlantı koptu
    }

    public Type message_type;
    public Object content;

    public Message(Type tur, Object mesaj) {
        this.message_type = tur;
        this.content = mesaj;
    }
}
