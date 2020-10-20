/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package profiles;

public abstract class Bot extends Player{
    private boolean stay;
    
    public Bot(String name, int amo){
        super(name, amo);
        this.stay = false;
    }
    
    public abstract int analiseAction();
    
    public void setStay(boolean stay) {
        this.stay = stay;
    }
    
    public boolean getStay(){
        return this.stay;
    }

    public void setSumCards(int sumCards) {
        this.sumCards = sumCards;
    }

    public void setNumATrades(int numATrades) {
        this.numATrades = numATrades;
    }
}
