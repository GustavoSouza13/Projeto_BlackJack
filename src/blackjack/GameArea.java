/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import structures.Queue;
import profiles.*;
import bots.*;
import javax.swing.JOptionPane;

public class GameArea extends javax.swing.JFrame {
    private Menu mn;
    private Queue queue;
    private Dealer dealer;
    private int alreadyPlayed;
    /**
     * Creates new form GameArea
     * @param mn
     */
    
    /* Tipos de jogadas/movimentos:
     * -2 == Optou por não fazer mais jogadas.
     * 2 == Hit.
     * Tipos de situações com a soma das cartas:
     * -1 == Estourou.
     * 0 == Menos de 21.
     * 1 == Igual a 21.
     * Nomenclaturas:
     * Jogador == Player e Bot.
     * Player == Usuário.
     * Bot == Robot.
     */
    
    public GameArea(Menu mn) {
        initComponents();
        this.mn = mn;
    }
    
    /* Inicializa as variáveis necessárias, também configura: a quantidade de 
     * bots, o perfil de cada bot, o Player e o Dealer.
     */
    public void prepareGame(){
        String qtd = JOptionPane.showInputDialog(null, "Quantos bots vão jogar?\n");
        int qtdBots = (qtd.equals("")) ? 0 : Integer.parseInt(qtd);
        
        // Se houver menos de 5 bots no jogo, sempre terá um jogador.
        if(qtdBots < 5){
            if (qtdBots < 0) qtdBots = 0;
            this.queue = new Queue(qtdBots+1);
            Player player = new Player("Player", 7000);
            player.setButton(true);
            this.queue.enqueue(player);
        }
        else
            this.queue = new Queue(qtdBots);
        
        this.dealer = new Dealer("Japa");
        
        
        
        int i = 0;
        while(i != qtdBots){
            /* (Math.random()*x) --> x é a quantidade de perfis de bots 
             *existentes.
             */
            
            /* SEMPRE ATUALIZAR O 'x' SE CRIAR NOVOS PERFIS DE BOTS */
            switch((int)((Math.random()*2))){
                case 0:
                    this.queue.enqueue(new Bot_Impulsive("Bot "+(++i), 5000));
                    break;
                case 1:
                    this.queue.enqueue(new Bot_Seguro("Bot "+(++i), 5000));
                    break;
            }
        }
        
        this.alreadyPlayed = this.queue.size();
    }
    
    public void prepareMatch(){
        int numPlayers = 0;
        boolean trade = false;
        
        while(!this.queue.isEmpty() && numPlayers++ < this.queue.size()){
            Basic_Profile bP = ((Player)this.queue.peek());
            
            if (bP.getAmount() == 0){
                Basic_Profile bPLost = (Player)this.queue.dequeue();
                
                System.out.println(bPLost.getName() + " foi eliminado por falta"
                        + "de fichas.");
                if (!this.queue.isEmpty() && bP.isButton()){
                    ((Player)this.queue.peek()).setButton(true);
                    trade = true;
                    System.out.println("Novo botão: " + ((Player)this.queue
                            .peek()).getName() + " IF");
                }
            }
            else if (!this.queue.isEmpty() && bP.isButton() && !trade){
                bP.setButton(false);
                this.queue.enqueue(this.queue.dequeue());
                ((Player)this.queue.peek()).setButton(true);
                trade = true;
            }
        }
    }
    
    public void startMatch(){
        if (this.queue.size() > 0){
            /* A variável alreadyPlayed grava a quantidade de jogadores que já 
             * fizeram seus movimentos/escolhas na partida.
             */
            giveCards();
            this.dealer.hit(this.dealer.withdraw());
            giveCards();
            this.dealer.hitHidden(this.dealer.withdraw());

            next();
            
            // Irá ser verdadeiro quando houver somente Bots na partida.
            if(this.alreadyPlayed == this.queue.size())
                dealerPlay();
        }
        else{
            System.out.println("Jogo finalizado pois não há mais jogadores na mesa.");
        }
    }
    
    // Faz *todas* as apostas (por enquanto).
    public void placeBets(){
        while(this.alreadyPlayed > 0){
            /* Todo Bot é um Player, mas nem todo Player é um Bot.
             * Logo, se dizer (casting) que Bot é do tipo Player, irá aceitar, 
             * pois Bot extende Player.
             */
            Basic_Profile bP;
            if (!(((Player)this.queue.peek()) instanceof Bot) && spnBet.isEnabled()){
                JOptionPane.showMessageDialog(null, "Sua vez de apostar.");
                break;
            }
            else
                bP = (Player)this.queue.dequeue();
            
            if(bP.getAmount() >= 300){
                int aposta;
                
                if (bP instanceof Bot)
                    // Sorteia um número entre 300 e 500.
                    aposta = 500;//((int)(Math.random()*201))+300;
                else{
                    // Pega o valor da caixa de apostas do Player.
                    aposta = (int)spnBet.getValue();
                }
                bP.setAmount(bP.getAmount() - aposta);
                bP.setBet(aposta);
                this.queue.enqueue(bP);
                System.out.println(bP.getName() + " apostou " + aposta);
            }
            // Apostará tudo quando o amount do jogador for menor que 300.
            else if (bP.getAmount() > 0){
                bP.setBet(bP.getAmount());
                bP.setAmount(0);
                this.queue.enqueue(bP);
                System.out.println(bP.getName() + " apostou " + bP.getBet());
            }
            else{
                System.out.println(bP.getName() + " deixou a mesa por falta de fichas.");
                bP = null;
            }
            
            this.alreadyPlayed--;
        }
        System.out.println("");
    }
    
    // Distribui as cartas (uma por vez) para os jogadores.
    public void giveCards(){
        for (int i = 0;i < this.queue.size();i++){
            Basic_Profile bP = (Player)this.queue.dequeue();
            
            bP.hit(dealer.withdraw());
            this.queue.enqueue(bP);
            
            /* Quando o Player recebe seu carta, atualiza no lblSumCards a soma
             * atual das cartas na mão do dele.
             */
            if(!(bP instanceof Bot)){
                lblSumCards.setText("Valor na mão: " + bP.getSumCards());
            }
        }
    }
    
    /* Verifica qual o tipo jogador que estava na fila para poder recolocá-lo
     * novamente.
     */
    public Player checkTypeBot(Basic_Profile bP){
        if (bP instanceof Bot_Impulsive)
            return new Bot_Impulsive(bP.getName(), bP.getAmount());
        else if (bP instanceof  Bot_Seguro)
            return new Bot_Seguro(bP.getName(), bP.getAmount());
        else
            return new Player(bP.getName(), bP.getAmount());
    }
    
    // Executa as jogadas/movimentos dos bots.
    public void next(){
        /* Se o atual jogador for um bot e ainda não tiver jogado, irá executar.
         * this.alreadyPlayed < this.queue.getTotal() = 
         * = quantos já jogaram / quantos tem na mesa.
         */ 
        while(this.queue.peek() instanceof Bot && this.alreadyPlayed < this.queue.size()){
            playBot();
            this.alreadyPlayed++;
        }
    }
    
    /* Cria uma nova instância dos jogadores na fila para facilitar a questão de
     * ter que apagar certos dados a cada partida, ex: cartas da mão, aposta, 
     * etc. 
     * Só está retornando o "novo" Basic_Profile para ajudar na programação no
     * btnStay.
     */
    public Basic_Profile removeFromMatch(Basic_Profile bP){
        Basic_Profile bPL;
        bPL = (Player)this.queue.dequeue();
        bPL = null; // Tentativa de dispensar memória.
        bPL = checkTypeBot(bP);
        this.queue.enqueue(bPL);
        return bPL;
    }
    
    // Faz todas as jogadas/movimentos de um bot na rodada.
    public void playBot(){
        while(true){
            Bot bot = (Bot)queue.peek();
            
            int keepInGame = 0;
            switch(bot.analiseAction()){
                case 2:
                    bot.hit(this.dealer.withdraw());
                    break;
                default:
                    keepInGame = -2;
                    break;
            }
            
            /* Se a variável 'keepInGame' for igual a 0 quer dizer que o bot
             * jogou, logo, precisa ser verificado se houve ultrapassagem, se
             * não, 'keepInGame' será igual a -2 e isso irá retirar o bot da 
             * fila.
             */
            if (keepInGame == 0){
                /* Verifica se a soma estourou
                 * Caso tenha estourado, haverá a verificação de se há "Ás" na mão 
                 * e se o valor poderá ser trocado.
                 */
                if (bot.checkBurst() == -1){
                    this.dealer.setAmount(this.dealer.getAmount() + bot.getBet());
                    removeFromMatch(bot);
                    break;
                }
            }
            else{
                /* Foi escolhido ficar (stay), logo, será passada a vez para o 
                 * próximo jogador.
                 */
                this.queue.enqueue(this.queue.dequeue());
                break;
            }
        }
    }
    
    // Faz todos os movimentos do Dealer.
    // Faz as verificações de quem ganhou, perdeu e empatou.
    public void dealerPlay(){
        this.dealer.hit(this.dealer.getHiddenCard());
        this.dealer.hitHidden(null);
        
        while(this.dealer.analiseAction() == 2){
            this.dealer.hit(this.dealer.withdraw());
            /* Esse checkburst() garante que se antes foi tirado 'Ás', ele seja
             * trocado para a decisão do dealer ser correta.
             */
            this.dealer.checkBurst();
        }
        
        Basic_Profile bP;
        // Esse checkburst() só verifica se estourou, não traz mudanças.
        if (this.dealer.checkBurst() == -1){
            for(int i = 0;i < this.queue.size();i++){
                bP = (Player)this.queue.peek();
                if (bP.getSumCards() > 0){
                    if (bP.blackJack()){
                        bP.setAmount(bP.getAmount() + ((int)(bP.getBet()*2.5)));
                        System.out.println(bP.getName() + " ganhou. BlackJack contra estouro.");
                    }
                    else{
                        bP.setAmount(bP.getAmount() + (bP.getBet()*2));
                        System.out.println(bP.getName() + " ganhou. " + bP.getSumCards() + " contra estouro.");
                    }
                    
                    System.out.println(bP.getName() + " total: " + bP.getAmount());
                    removeFromMatch(bP);
                }
                else{
                    System.out.println(bP.getName() + " perdeu por ter estourado.");
                    System.out.println(bP.getName() + " total: " + bP.getAmount());
                    this.queue.enqueue(this.queue.dequeue());
                }
            }
        }
        else{
            int numBP = this.queue.size();
            while(numBP > 0){
                bP = (Player)this.queue.peek();
                // Verifica se o jogador foi retirado da fila
                if (bP.getSumCards() > 0){
                    if (bP.blackJack()){
                        if (this.dealer.blackJack()){
                            bP.setAmount(bP.getAmount() + bP.getBet());
                            System.out.println(bP.getName() + " empatou. BlackJack contra BlackJack.");
                        }
                        else{
                            bP.setAmount(bP.getAmount() + ((int)(bP.getBet()*2.5)));
                            System.out.println(bP.getName() + " ganhou. BlackJack contra " + this.dealer.getSumCards() + ".");
                        }
                    }
                    else if (this.dealer.blackJack()){
                        this.dealer.setAmount(this.dealer.getAmount() + bP.getBet());
                        System.out.println(bP.getName() + " perdeu. " + bP.getSumCards() + " contra BlackJack.");
                    }
                    else if (bP.getSumCards() > this.dealer.getSumCards()){
                        bP.setAmount(bP.getAmount() + (bP.getBet()*2));
                        System.out.println(bP.getName() + " ganhou. " + bP.getSumCards() + " contra " + this.dealer.getSumCards() + ".");
                    }
                    else if (bP.getSumCards() < this.dealer.getSumCards()){
                        this.dealer.setAmount(this.dealer.getAmount() + bP.getBet());
                        System.out.println(bP.getName() + " perdeu. " + bP.getSumCards() + " contra " + this.dealer.getSumCards() + ".");
                    }
                    else{ 
                        bP.setAmount(bP.getAmount() + bP.getBet());
                        System.out.println(bP.getName() + " empatou. " + bP.getSumCards() + " contra " + this.dealer.getSumCards() + ".");
                    }
                    
                    System.out.println(bP.getName() + " total: " + bP.getAmount());
                    removeFromMatch(bP);
                }
                else{
                    System.out.println(bP.getName() + " eliminado por ter estourado.");
                    System.out.println(bP.getName() + " total: " + bP.getAmount());
                    this.queue.enqueue(this.queue.dequeue());
                }
                
                numBP--;
            }
        }
        
        System.out.println("");
        // Limpa alguns dados do dealer para a próxima partida.
        this.dealer.clean();
        // Zera a quantidade de pessoas que já jogaram.
        this.alreadyPlayed = this.queue.size();
        /* Some com o texto da soma das cartas do Player (some porque se não
         * houver Player no jogo não faz sentido aparecer algo no lbl).
         */
        lblSumCards.setText("");
        // Faz a fila andar após cada rodada. (Passa o 'botão')
        this.queue.enqueue(this.queue.dequeue());
        //removeFromMatch((Player)this.queue.peek());
    }
            
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnHit = new javax.swing.JButton();
        btnStay = new javax.swing.JButton();
        btnDouble = new javax.swing.JButton();
        btnSplit = new javax.swing.JButton();
        lblResult = new javax.swing.JLabel();
        lblCardsP = new javax.swing.JLabel();
        lblCardD = new javax.swing.JLabel();
        spnBet = new javax.swing.JSpinner();
        btnBet = new javax.swing.JButton();
        lblSumCards = new javax.swing.JLabel();
        lblAllIn = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        btnHit.setText("Pedir");
        btnHit.setEnabled(false);
        btnHit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitActionPerformed(evt);
            }
        });

        btnStay.setText("Ficar");
        btnStay.setEnabled(false);
        btnStay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStayActionPerformed(evt);
            }
        });

        btnDouble.setText("Dobrar");
        btnDouble.setEnabled(false);

        btnSplit.setText("Dividir");
        btnSplit.setEnabled(false);

        lblResult.setForeground(new java.awt.Color(0, 0, 255));

        spnBet.setValue(50);
        spnBet.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnBetStateChanged(evt);
            }
        });

        btnBet.setText("Apostar");
        btnBet.setEnabled(false);
        btnBet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBetActionPerformed(evt);
            }
        });

        lblAllIn.setForeground(new java.awt.Color(255, 14, 12));
        lblAllIn.setText("All in!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblCardsP, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAllIn)
                                .addGap(132, 132, 132))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(spnBet)
                                    .addComponent(btnBet, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                                .addGap(105, 105, 105)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnHit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnStay, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnDouble, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblSumCards, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblResult, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 118, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(120, Short.MAX_VALUE)
                    .addComponent(lblCardD, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(117, 117, 117)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(164, Short.MAX_VALUE)
                        .addComponent(btnHit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnStay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDouble)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSplit))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblResult, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblSumCards, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCardsP, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAllIn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnBet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBet)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(82, 82, 82)
                    .addComponent(lblCardD, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(166, Short.MAX_VALUE)))
        );

        lblAllIn.setVisible(false);

        setSize(new java.awt.Dimension(416, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        mn.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Deixa não editavel a caixa de texto do Spinner (caixa de apostas).
        ((javax.swing.JSpinner.DefaultEditor) spnBet.getEditor()).getTextField()
                .setEditable(false);
        
        // Configura tudo necessário para o início do jogo.
        prepareGame();
        prepareMatch();
        
        // Verifica se o 'amount' do Player é maior que 0 para poder jogar.
        if (((Player)this.queue.peek()).getAmount() > 0)
            updateSpnBet();
        else
            this.queue.dequeue();
        
        // Faz as apostas iniciais
        placeBets();
        // Verifica se há somente Bots na fila para assim iniciar o jogo.
        onlyBotsPlay();
    }//GEN-LAST:event_formWindowOpened
    
    public void onlyBotsPlay(){
        if (this.queue.peek() instanceof Bot)
            while(this.queue.size() != 0){
                placeBets();
                startMatch();
            }
        else
            // Habilita o botão e o Spinner de apostas do Player.
            changeStatusPlaceBets();
    }
    
    // Troca o valor da propriedade 'enabled' dos botões de jogo do Player.
    public void changeStatusPlayBtns(){
        if(btnHit.isEnabled()){
            btnHit.setEnabled(false);
            btnStay.setEnabled(false);
        }
        else{
            btnHit.setEnabled(true);
            btnStay.setEnabled(true);
        }
    }
    /* Troca o valor da propriedade 'enabled' do botão e do Spinner de
     * apostas do Player.
     */
    public void changeStatusPlaceBets(){
        if (btnBet.isEnabled()){
            btnBet.setEnabled(false);
            spnBet.setEnabled(false);
        }else{
            btnBet.setEnabled(true);
            spnBet.setEnabled(true);
        }
    }
    
    private void btnHitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitActionPerformed
        Player p = (Player)this.queue.peek();
        p.hit(this.dealer.withdraw());
        
        /* Esse checkBurst() troca o valor do(s) "Ás" se houver e verifica se
         * estourou ou não.
        */
        if (p.checkBurst() == -1){
            this.dealer.setAmount(this.dealer.getAmount() + p.getBet());
            lblSumCards.setText("");
            btnStay.doClick();
        }else{
            lblSumCards.setText("Valor na mão: " + p.getSumCards());
        }
    }//GEN-LAST:event_btnHitActionPerformed

    private void btnStayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStayActionPerformed
        // Desabilita os botões de jogo do Player.
        changeStatusPlayBtns();
        
        /* Esse checkBurst() só verifica se a ação do 'click' veio do próprio
         * Player (não estourou) ou se veio da programação do 'btnHit'
         * (estourou).
         */
        Player player;
        if (((Player)this.queue.peek()).checkBurst() == -1)
            player = (Player)removeFromMatch((Player)this.queue.peek());
        else
            // Faz o player 'andar' na fila.
            player = (Player)this.queue.enqueue(this.queue.dequeue());
        
        // Se ainda houver alguém para jogar (bots), será feito neste next().
        if(++this.alreadyPlayed < this.queue.size())
            next();
        
        /* Chega a vez do Dealer jogar já que o Player e todos os Bots já
         * jogaram.
         */
        dealerPlay();
        
        prepareMatch();
        
        /* Se o Player zerar quer dizer que há somente Bots na fila, então
         * as partidas podem ser realizadas automaticamente.
         */
        if (!this.queue.isEmpty() && player.getAmount() == 0)
            onlyBotsPlay();
        else{
            // Para identificar no placeBets() que o Player ainda não apostou.
            spnBet.setEnabled(true);
            // Começa as apostas da próxima rodada.
            placeBets();
            /* Habilita o botão e o Spinner de apostas e atualiza o
             * valor do Spinner se o Player tiver menos que a última aposta
             * feita.
             */
            updateSpnBet();
            changeStatusPlaceBets();
        }
    }//GEN-LAST:event_btnStayActionPerformed

    public void updateSpnBet(){
        int value = (int)spnBet.getValue();
        int amountPlayer = ((Player)this.queue.peek()).getAmount();
        
        if (amountPlayer > value){
            if (value > 5000)
                spnBet.setValue(5000);
            lblAllIn.setVisible(false);
        }
        else if (amountPlayer < value){
            if (amountPlayer <= 50)
                spnBet.setValue(amountPlayer);
            else{
                spnBet.setValue(50);
                lblAllIn.setVisible(false);
            }
        }
        else
            lblAllIn.setVisible(true);
    }
    
    private void btnBetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBetActionPerformed
        // Desabilita o botão e o Spinner de apostas do Player.
        changeStatusPlaceBets();
        // Faz a aposta do Player e do restante dos Jogadores.
        placeBets();
        // Começa sempre uma nova partida
        startMatch();
        JOptionPane.showMessageDialog(null, "Sua vez de jogar.");
        // Habilita os botões de jogo do Player.
        changeStatusPlayBtns();
        
        /* Aposta está funcionando completamente, testar para verificar.
         * Pensar em outra lógica para saber se o Player já apostou ou não (uma
         * maneira pode ser criando um atributo em 'Basic_Profile').
         * Alterar a lógico de como se passa os Jogadores (botão) na fila,
         * adicionar um atributo em 'Basic_Profile' ou criar uma lógica para
         * saber sempre em qual posição começou a distriuir as cartas para na
         * proximo rodada começar pelo próximo na fila. Assim não mudando mais
         * os Jogadores de lugar na fila (muito chato ter que ficar procurando
         * no 'Output' onde está quem pq sempre ficam fora de ordem).
         * Botões Pedir e Ficar já estão programados e funcionando, inclusive
         * quando devem ficar desabilitados e quando devem ficar habilitados,
         * o mesmo vale para o botão e Spinner de apostas.
         * Programar os botões Dividir e Dobrar e deixá-los habilitados quando
         * puderem ser utilizados na partida.
         */
        
        
        /* URGENTE
         * Ficar testando até o momento em que apostar all-in e ficar e perder
         * ou estourar não execute o 'onlyBotsPlay' e ai descobrir o porque e
         * arrumar.
         * Retirar o 'removeFromMatch()' e criar um método para limpar os
         * atributos necessários de cada Jogador, será bem melhor e mais rápido.
         */
    }//GEN-LAST:event_btnBetActionPerformed

    private void spnBetStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnBetStateChanged
        String valueS = String.valueOf(spnBet.getValue());
        int amountPlayer = ((Player)this.queue.peek()).getAmount();
        
        /* Só entra neste 'if' quando o valor da 'bet' for o 'amountPlayer',
         * que é setado no 'default' do primeiro 'switch' e no segundo 
         * 'else if'.
         * Sempre que o 'spnBet.setValue(n)' é executado ele muda o estado do
         * Spinner, fazendo esse método ser chamado novamente. Então, se o valor
         * setado for tudo ('amountPlayer') que o Player possui, irá deixar
         * visível o lblAllIn).
         * Em todos os outros casos esse 'if' é falso.
         */
        
        if (Integer.parseInt(valueS) == amountPlayer){
            lblAllIn.setVisible(true);
        }
        else if (amountPlayer <= 50){
            spnBet.setValue(amountPlayer);
        }
        else if (amountPlayer == Integer.parseInt(valueS)-1 || amountPlayer == Integer.parseInt(valueS)+1){
            spnBet.setValue(50);
            lblAllIn.setVisible(false);
        }
        else if (valueS.charAt(valueS.length()-1) == '1'){
            int value = Integer.parseInt(valueS);
            int nextValue;
            
            switch(value){
                case 51:
                    nextValue = 100;
                    break;
                case 101:
                    nextValue = 200;
                    break;
                case 201:
                    nextValue = 500;
                    break;
                case 501:
                    nextValue = 1000;
                    break;
                case 1001:
                    nextValue = 2000;
                    break;
                case 2001:
                    nextValue = 5000;
                    break;
                default:
                    nextValue = amountPlayer;
                    break;
              }
            
            if (nextValue >= amountPlayer)
                spnBet.setValue(amountPlayer);
            else
                spnBet.setValue(nextValue);
          }
        // Se o Player está diminuindo o valor do Spinner, entra nesta condição.
        else{
            int value = Integer.parseInt(valueS);
            switch(value){
                case 49:
                    spnBet.setValue(50);
                    break;
                case 99:
                    spnBet.setValue(50);
                    break;
                case 199:
                    spnBet.setValue(100);
                    break;
                case 499:
                    spnBet.setValue(200);
                    break;
                case 999:
                    spnBet.setValue(500);
                    break;
                case 1999:
                    spnBet.setValue(1000);
                    break;
                case 4999:
                    spnBet.setValue(2000);
                    break;
              }
          }
    }//GEN-LAST:event_spnBetStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBet;
    private javax.swing.JButton btnDouble;
    private javax.swing.JButton btnHit;
    private javax.swing.JButton btnSplit;
    private javax.swing.JButton btnStay;
    private javax.swing.JLabel lblAllIn;
    private javax.swing.JLabel lblCardD;
    private javax.swing.JLabel lblCardsP;
    private javax.swing.JLabel lblResult;
    private javax.swing.JLabel lblSumCards;
    private javax.swing.JSpinner spnBet;
    // End of variables declaration//GEN-END:variables
}
