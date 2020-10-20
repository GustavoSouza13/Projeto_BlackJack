/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package profiles;

import structures.Card;
import structures.Stack;

public class Player extends Basic_Profile{
    private Stack handSplit;
    private int betSplit;
    
    public Player(String na, int amo){
        this.name = na;
        this.amount = amo;
        this.bet = 0;
        this.hand = new Stack(11);
        this.sumCards = 0;
        this.numATrades = 0;
        this.button = false;
    }

    @Override
    public void hit(Card card) {
        super.hit(card);
    }

    @Override
    public void valueCard(String nome) {
        super.valueCard(nome);
    }

    @Override
    public int checkBurst() {
        return super.checkBurst();
    }

    @Override
    public boolean blackJack() {
        return super.blackJack();
    }
    
    public void doubling(Card card){
        hit(card);
        this.amount -= this.bet;
        this.bet *= 2;
    }
    
    public void split(){
        this.handSplit = new Stack(11);
        this.handSplit.push(hand.pop());
        this.betSplit = bet;
        this.amount -= this.bet;
    }

    @Override
    public void setBet(int bet) {
        this.bet = bet;
    }

    public void setHand(Stack hand) {
        this.hand = hand;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAmount() {
        return amount;
    }
    
    @Override
    public int getBet() {
        return bet;
    }
    
    @Override
    public int getSumCards() {
        return sumCards;
    }
}
