/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package profiles;

import structures.Card;
import structures.Stack;

public abstract class Basic_Profile {
    String name;
    int amount;
    int bet;
    Stack hand;
    int sumCards;
    int numATrades;
    boolean button;
    
    public void hit(Card card){
        hand.push(card);
        valueCard(card.getNome());
    }
    
    public void valueCard(String nome){
        // Atribui o valor da carta há variável
        if(Character.isLetter(nome.charAt(0)))
            if(nome.equals("Ás")){
                // Faz a verificação se o "Ás" valerá 1 ou 11
                this.sumCards += 11;
                checkBurst();
            }
            else
                this.sumCards += 10;
        else
            this.sumCards += Integer.parseInt(nome); 
    }
    
    public int checkBurst(){
        // Verifica se a soma é igual a 21 ou se já ultrapassou ou não
        if (this.sumCards > 21){
            // Se ainda houver a possibilidade de trocar o valor de um "Ás",
            // será feito aqui
            if (this.hand.containsAs() > this.numATrades){
                this.sumCards -= 10;
                this.numATrades++;
                
                return checkBurst();
            }
            
            return -1;
        }
        else if (this.sumCards < 21)
            return 0;
        else
            return 1;
    }
    
    public boolean blackJack(){
        return this.hand.size() == 2 && this.sumCards == 21;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setButton(boolean button) {
        this.button = button;
    }
    
    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getSumCards() {
        return sumCards;
    }

    public int getBet() {
        return bet;
    }
    
    public boolean isButton() {
        return button;
    }
}
