/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

public class Queue implements TAD_Queue{
    private int total = 0; // Total de elementos (Convenção: 0 se a FILA estiver vazia)
    private int head = -1; // Começo da queue (Convenção: -1 se a FILA estiver vazia)
    private int tail = -1; // Fim da queue (Convenção: 0 se a FILA estiver vazia)
    private Object[] memo; // Vetor para armazenar os elementos
    private int MAX; // Capacidade máxima do vetor, diferente do atributo total

    // Construtor
    public Queue(int MAX){
        this.MAX = MAX;
        memo = new Object[MAX];
    }
    
    // Verifica se a FILA está vazia
    @Override
    public boolean isEmpty(){
        return (total == 0);
    }
    
    // Verifica se a FILA está cheia
    @Override
    public boolean isFull() {
        return (total == MAX);
    }
    
    // Insere um elemento no final (tail) da FILA
    @Override
    public Object enqueue(Object x) {
        if (!isFull() && x != null){ // Pré-condição
            if (++tail >= MAX){
                tail = 0; // MAX-1 é a ultima posição do vetor
            }
            
            if (head == -1){
                head = tail;
            }
            
            memo[tail] = x;
            total++;
            return x;
        }
        else{ // Não pode inserir elemento nulo (x == null)
            return null; // Ou se a FILA estiver cheia
        }
    }
    
    // Remove um elemento do inicio (head) da FILA
    @Override
    public Object dequeue() {
        Object objeto;
        if (!isEmpty()){ // Pré-condição
            objeto = memo[head];
            if (++head >= MAX){
               head = 0; // MAX-1 é a ultima posição do vetor
            }
            
            total--;
            if (total == 0){
                head = -1;
                tail = -1;
            }
            
            return objeto; // Retor o objeto que estava na head
        }
        else{
            return null; // Não se retira elemento de FILA vazia
        }
    }
    
    // Retorna o objeto que está no inicio da FILA (o primeiro da FILA), mas sem elimina-lo
    @Override
    public Object peek() {
        if (!isEmpty())
            return memo[head];
        else
            return null; // Retorna caso FILA vazia
    }
    
    // Imprime FILA no console
    @Override
    public String toString() {
        if (!isEmpty()){
            String saida = "";
            int pos = head;

            for(int i = 1;i <= total;i++){
                saida += memo[pos].toString();
                
                if (i != total)
                    saida += ", ";
                
                pos++;
                if (pos >= MAX){
                    pos = 0;
                }
            }
            return ("F: [ " + saida + " ]");
        }
        else{
            return ("F: []");
        }
    }
    
    public Object walk(){
        if (!isEmpty())
            return enqueue(dequeue());
        else
            return null;
    }

    public int size() {
        return total;
    }
}
