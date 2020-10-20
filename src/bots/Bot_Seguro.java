/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bots;

import profiles.Bot;

public class Bot_Seguro extends Bot{
    public Bot_Seguro(String name, int amo){
        super(name, amo);
    }

    @Override
    public int analiseAction() {
        if (this.getSumCards() < 16)
            return 2;
        else
            return 0;
    }
    
}
