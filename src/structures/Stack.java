/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

public class Stack implements TAD_Stack{
    private int topo;
    private int MAX;
    private Card memo[];
    
    public Stack(int qtde){
        topo = -1;
        MAX = qtde;
        memo = new Card[MAX];
    }
    
    @Override
    public Card push(Card x){
        if (!isFull() && x != null){
            memo[++topo] = x;
            return x;
        }
        else{
            return null;
        }
    }
    
    @Override
    public Card pop(){
        if (!isEmpty()){
            return memo[topo--];
        }
        else{
            return null;
        }
    }
    
    @Override
    public boolean isEmpty(){
        return (topo == -1);
    }
    
    @Override
    public boolean isFull(){
        return (topo == MAX - 1);
    }
    
    @Override
    public Card top(){
        if (!isEmpty()){
            return memo[topo];
        }
        else{
            return null;
        }
    }
    
    @Override
    public String toString(){
        if (!isEmpty()){
            String msg = "";
            for(int i = topo;i >= 0;i--){
                msg += memo[i].getNome();
                if (i != 0) msg += " | ";
            }
            return (msg);
        }
        else return("Pilha vazia!");
    }

    public int containsAs(){
        Stack temp = new Stack(this.size());
        int numOfAs = 0;
        
        for (int i = 0;i < this.size();i++){
            Card c = this.pop();
            
            if (c.getNome().equals("Ás"))
                numOfAs++;
            
            temp.push(c);
        }
        
        for (int i = 0;i < temp.size();i++){
            this.push(temp.pop());
        }
        temp = null;
        
        return numOfAs;
    }
    
    public int size() {
        return topo+1;
    }
    
}
