/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;
import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final Stack deck;
    private final ArrayList deckList;
    final private String naipes[] = {"♣", "♥", "♠", "♦"};
    final private String nomeCartas[] = {"Ás", "Q", "J", "K"};
    
    public Deck(){
        this.deck = new Stack(52);
        this.deckList = new ArrayList();
        
        fillDeck();
    }
    
    public void fillDeck(){
        Card c;
        
        for(int i = 0;i < 4;i++){
            int cont = 1;
            for(int j = 0;j < 13;j++){
                c = new Card();
                if (j == 0){
                    c.setNome(nomeCartas[j]);
                    c.setNaipe(naipes[i]);
                    deckList.add(c);
                }else if (j < 10){
                    c.setNome(Integer.toString(j+1));
                    c.setNaipe(naipes[i]);
                    deckList.add(c);
                }else{
                    c.setNome(nomeCartas[cont]);
                    c.setNaipe(naipes[i]);
                    deckList.add(c);
                    cont++;
                }
            }
        }
        
        shuffle();
    }
    
    public void shuffle(){
        ArrayList listaTemp = new ArrayList();
        Random rdn = new Random();
        
        while(deckList.size() > 1){
            int pos = rdn.nextInt(deckList.size());
            listaTemp.add(deckList.get(pos));
            deckList.remove(pos);
        }
        
        listaTemp.add(deckList.get(0));
        deckList.clear();
        
        for(int i = 0;i < listaTemp.size();i++){
            deck.push((Card)listaTemp.get(i));
        }
    }
    
    public Card pop(){
        if (this.deck.isEmpty())
            fillDeck();
        return this.deck.pop();
    }
}
