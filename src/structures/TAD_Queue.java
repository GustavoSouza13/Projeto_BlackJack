/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

public interface TAD_Queue { //Tipo Abstrato de Dado que descreve uma Fila
    // Retornam se a fila está vazia ou cheia
    public boolean isEmpty();
    public boolean isFull();
    
    // Insere um elemento no final da fila
    public Object enqueue(Object x);
    
    // Remove um elemento do inicio da fila
    public Object dequeue();
    
    // Retorna o objeto do inicio da fila, sem eliminar
    public Object peek();
    
    // Retorna o conteudo (todos os elementos) da Queue
    @Override
    public String toString();
}
