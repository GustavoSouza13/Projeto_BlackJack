/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bots;

import profiles.Bot;

public class Bot_Impulsive extends Bot{
    public Bot_Impulsive(String na, int amo) {
        super(na, amo);
    }
    
    @Override
    public int analiseAction(){
        if (this.getSumCards() < 21)
            return 2;
        else
            return 1;
    }
}
