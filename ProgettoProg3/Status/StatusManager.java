package Progetto_prog_3.Status;
import Progetto_prog_3.entities.Entity;
import Progetto_prog_3.entities.Player;

import static Progetto_prog_3.utils.Constants.GRAVITY;
import static Progetto_prog_3.utils.HelpMetods.*;
import java.lang.Thread;


public class StatusManager {

    public void applySlow(Entity entity, int duration, float slowValue){

        //Si conserva lo stato attuale della velocità di movimento della entità
        float startingWalkSpeed = entity.getWalkSpeed();
        //Viene creato un nuovo thread che gestisce il debuff
        Thread slowThread = new Thread(() -> {
            //Semplicemente viene settata una debuff speed al valore passato come parametro
            entity.setWalkSpeed(slowValue);
            //Si effettua uno sleep del thread della durata del debuff scelta
            try {
                Thread.sleep(duration * 1000);
            } catch (Exception e) {
                System.out.println("Qualcosa è andato storto nella funzione di slow");
                e.printStackTrace();
            }
            //Quando la sleep è finita viene ripsistinata la velocità di movimento
            entity.setWalkSpeed(startingWalkSpeed);
        });

        //Una volta creato il thread questo viene eseguito lanciando le istruzioni
        slowThread.start();

    }


    //Questa funzione setta l'invulnerabilità per un determinato periodo ad una entità
    public void giveInvulnerability(Entity entity, float duration){

        //Si crea un nuovo thread che effettua una sleep sulla invulnerabilità della entità
        //dopo averla setata a true, per poi riportarla a false
        Thread invulnerability= new Thread(() -> {

            entity.setInvulnerability(true);

            try {
                Thread.sleep((int)(duration * 1000));
            } catch (Exception e) {
                System.out.println("Qualcosa è andato storto nello status invulnerability");
                e.printStackTrace();
            }

            //Finita la sleep la caratteristica viene resettata a falsa
            entity.setInvulnerability(false);

        });

        invulnerability.start();
        
    }

    //Funzione che gestisce il knockback
    public void knockBack(Entity entity, int[][] levelData, int direction, float strenght){

        //Viene creato un nuovo thread che lo gestisce
        Thread knockBack = new Thread(() -> {

            //La variabile go down indica che il movimento del knockback verso l'alto non è più permesso
            boolean goDown = false;
            //La jumpForce l'abiamo giò incontrata e serve ad aggiornare la posizione aerea ad ognio iterazione
            float jumpForce = strenght;
            //Si seta lo stato di knockback nella entità nel caso servisse a dei controlli, 
            //Il player ad esempio non potrà muoversi se si trova in questo stato
            entity.setGainingKnockback(true);

            //Un while infinito simula quello del nodo game
            while(true) {

                //Se la forza è negativa l'entità si sta spostando in alto e vengono fatti i controlli del caso 
                //Per controllare che il movimento sia permesso
                if (jumpForce <= 0 && !goDown) {
                    //Se il movimento è permesso viene aggiornata la posizione aerea ed aggiornata la variabile di spostamento aereo in funzione della gravità
                    if (canMoveHere((float)entity.getHitbox().getX(), (float)entity.getHitbox().getY() + jumpForce, (float)entity.getHitbox().getWidth(), (float)entity.getHitbox().getHeight(), levelData)) {
                        
                        jumpForce += GRAVITY;
                        entity.getHitbox().y += jumpForce;
                        //Viene aggiorata in contemporanea la posizione orizzontale
                        if (canMoveHere((float)entity.getHitbox().getX() + strenght*direction, (float)entity.getHitbox().getY(), (float)entity.getHitbox().getWidth(), (float)entity.getHitbox().getHeight(), levelData)) {
                            entity.getHitbox().x += strenght * direction;   
                        }

                        //Viene effettuata una sleep per pochi millisecondi per simulate il game loop
                        try {
                            Thread.sleep(6);
                        } catch (Exception e) {
                            System.out.println("Qualcosa è andato storto nello status Knockback");
                            e.printStackTrace();
                        }

                        //Questo "continue" ci permette di skippare il blocco di codice successivo se il movimento verso l'alto viene permesso
                        continue;
   
                    }
                
                //Se la forza di salto è verso il basso viene impostata la flag di movimento verticale verso il basso
                //La parte di codice superiore non verrà più eseguita a differenza della prossima fino ad ora skippata
                } else if(!goDown) {
                    jumpForce = 0.01f;
                    goDown = true;
                }

                //La logica è molto simile al blocco precedente
                if (goDown) {
                    if (canMoveHere((float)entity.getHitbox().getX(), (float)entity.getHitbox().getY() + jumpForce, (float)entity.getHitbox().getWidth(), (float)entity.getHitbox().getHeight(), levelData)) {
        
                        jumpForce += GRAVITY;
                        entity.getHitbox().y += jumpForce;

                        if (canMoveHere((float)entity.getHitbox().getX() + strenght*direction, (float)entity.getHitbox().getY(), (float)entity.getHitbox().getWidth(), (float)entity.getHitbox().getHeight(), levelData)) {
                            entity.getHitbox().x += strenght * direction;   
                        }

                        try {
                            Thread.sleep(6);
                        } catch (Exception e) {
                            System.out.println("Qualcosa è andato storto nello status Knockback");
                            e.printStackTrace();
                        }

                        continue;

                    //Se il movimento verso l'alto non è permesso viene gestita la posizione in y per riposizionare l'entità in relazione al terreno
                    } else {
                        entity.getHitbox().y = getEntityYPosFloorRoofRelative(entity.getHitbox(), jumpForce);
                        break;
                    }
                }
            }


            //viene risettata la variabile a faso
            entity.setGainingKnockback(false);

        });


        knockBack.start();
        
    }

    //Funzione che gestisce il burn del player
    public void burn(Player player, float duration){

        Thread burn = new Thread(() -> {
            //Viene inizializzata una variabile che tiene conto del tempo che passa
            float timePassed = 0;
            //Un ciclo while toglie un puntoi hp ogni iterazione ed effettua una sleep prima della prossima
            while (true) {
                player.burn(-1);

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Qualcosa è andato storto nello status Burn");
                    e.printStackTrace();
                }
                
                //viene incrementata la variabile che memorizza il tempo
                timePassed += 0.1f;
                //Se questa variabile supera la durata il ciclo viene rotto e si esce dalla funzione
                if (timePassed >= duration) {
                    break;
                }
            }
        });

        burn.start();

    }

    //Funzione che gestisce la ricarica dell'energia del player in modo più rapido
    //Aggiungendo alla rigenerazione standard del thread principale, un altro thread parallelo che
    //Aumenta l'energia con la stessa frequenza, l'effetto che si ottiene è un raddoppiamento della velocità
    //i ricarica dell'energia
    public void fastEnergyRecharge(Player player, float duration){

        Thread fastEnergyRecharge = new Thread(() -> {
            //Viene inizializzata una variabile che tiene conto del tempo che passa
            float timePassed = 0;
            //Un ciclo while toglie un puntoi hp ogni iterazione ed effettua una sleep prima della prossima
            while (true) {
                player.changePower(1);;

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Qualcosa è andato storto nello status Burn");
                    e.printStackTrace();
                }
                
                //viene incrementata la variabile che memorizza il tempo
                timePassed += 0.1f;
                //Se questa variabile supera la durata il ciclo viene rotto e si esce dalla funzione
                if (timePassed >= duration) {
                    break;
                }
            }
        });

        fastEnergyRecharge.start();

    }







}
