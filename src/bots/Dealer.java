/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bots;

import structures.Deck;
import structures.Card;
import structures.Stack;
import profiles.Bot;
import profiles.Player;

public class Dealer extends Bot{
    private Card hiddenCard;
    private Deck deck;
    
    public Dealer(String name){
        super(name, 100000);
        this.deck = new Deck();
    }

    @Override
    public int analiseAction() {
        if (this.getSumCards() <= 17){
            return 2;
        }
        else{
            return 0;
        }
    }
    
    public void clean(){
        setHand(new Stack(11));
        setSumCards(0);
        setNumATrades(0);
    }

    @Override
    public void hit(Card card) {
        super.hit(card);
    }
    
    public void hitHidden(Card c){
        hiddenCard = c;
    }
    
    public Card withdraw(){
        return this.deck.pop();
    }
    
    @Override
    public void setHand(Stack hand) {
        super.setHand(hand);
    }

    @Override
    public void setSumCards(int sumCards) {
        super.setSumCards(sumCards);
    }

    @Override
    public void setNumATrades(int numATrades) {
        super.setNumATrades(numATrades);
    }
    
    public Card getHiddenCard() {
        return hiddenCard;
    }
}
