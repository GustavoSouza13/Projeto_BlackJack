/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

public class Card {
    private String nome;
    private String naipe;
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    public void setNaipe(String naipe){
        this.naipe = naipe;
    }
    
    public String getNaipe(){
        return naipe;
    }
}
