/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

public interface TAD_Stack {
    public Card push(Card x);
    
    public Card pop();
    
    public boolean isEmpty();
    
    public boolean isFull();
    
    public Card top();
    
    @Override
    public String toString();
}
