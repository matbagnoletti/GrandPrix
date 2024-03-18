package tpsit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe che rappresenta il giudice di gara. Si occupa di gestire la gara, i piloti e le auto, gli eventi e di stabilire la classifica finale.
 * @author Matteo Bagnoletti Tini
 */
public class Giudice {
    private final Gara gara;

    /**
     * Classifica finale dei piloti esclusi quelli incidentati.
     */
    private CopyOnWriteArrayList<Pilota> classifica;

    /**
     * Elenco dei piloti incidentati.
     */
    private CopyOnWriteArrayList<Pilota> incidentati;

    /**
     * Variabile utilizzata per tener traccia dei piloti durante il giro controllato da safety car.
     */
    private int contatoreGiroSicuro;

    /**
     * Variabile che indica se la safety car è nel circuito
     */
    private boolean giroSicuro;

    public Giudice(Giocatore giocatore) {
        this.classifica = new CopyOnWriteArrayList<>();
        this.incidentati = new CopyOnWriteArrayList<>();
        this.gara = new Gara(this);
        this.giroSicuro = false;
        gara.inizializza(giocatore.getUsername());
        gara.scegliPilota(giocatore);
        gara.trucca();
    }

    /**
     * Metodo di avvio dei thread piloti in gara.
     */
    public void avviaGara() {
        System.out.println("\n\033[33m-------------------< Inizio Gara >-------------------\033[0m\n");

        for(Pilota pilota : gara.getPiloti()){
            pilota.start();
        }

        for(Pilota pilota : gara.getPiloti()){
            try {
                pilota.join();
            } catch (InterruptedException e) {
                System.out.println("\033[31mSi è verificato un errore nell'attenere il pilota " + pilota.getNome() + "!\033[0m");
            }
        }

        System.out.println("\033[33m--------------------< Fine Gara >--------------------\033[0m\n");
        System.out.println("\033[33m-------------------< Classifica >--------------------\033[0m\n");
        stampaClassifica();
    }

    /**
     * Metodo per segnalare un incidente. Il pilota viene rimosso dalla gara e aggiunto alla lista degli incidentati.
     * @param pilotaIncidentato Pilota che ha avuto l'incidente.
     * @see Pilota
     */
    public synchronized void segnalaIncidente(Pilota pilotaIncidentato) {
        stampa(pilotaIncidentato.getNome() + " ha avuto un incidente!", "errore");
        stampa("SAFETY CAR NEL CIRCUITO", "avviso");
        giroSicuro = true;
        contatoreGiroSicuro = 0;
        incidentati.add(pilotaIncidentato);
        gara.rimuoviPilota(pilotaIncidentato);
        for(Pilota pilota : gara.getPiloti()){
            pilota.setSafetyCar(true);
        }
    }

    /**
     * Metodo per segnalare la fine del giro controllato dalla safety car.
     * @see Pilota
     */
    public void fineGiroSicuro() {
        if(giroSicuro){
            for(Pilota pilota : gara.getPiloti()){
                pilota.setSafetyCar(false);
            }
            stampa("SAFETY CAR DISATTIVATA", "avviso");
            giroSicuro = false;
        }
    }

    /**
     * Metodo per segnalare un pit stop effettuato da un pilota.
     * @param pilota il pilota che ha effettuato il pit stop.
     * @see Pilota
     */
    public synchronized void segnalaPitStop(Pilota pilota) {
        stampa(pilota.getNome() + " ha effettuato un pit stop!", "avviso");
    }

    /**
     * Metodo per segnalare i progressi di un pilota durante la gara. Viene invocato ad ogni giro.
     * @param pilota il pilota che ha completato il giro.
     * @param giro il numero del giro completato.
     * @param spazioPercorso metri percorsi dal pilota.
     * @param velocita ultima velocità generata dall'auto.
     */
    public synchronized void segnalaProgressi(Pilota pilota, int giro, int spazioPercorso, int velocita){
        stampa(pilota.getNome() + " ha completato il " + giro + "º giro!\n    - Velocità: " + velocita  + " m/s\n    - Metri percorsi: " + spazioPercorso, "giro");
        if(giroSicuro){
            contatoreGiroSicuro ++;
        }
        if(contatoreGiroSicuro == gara.getPiloti().size()){
            fineGiroSicuro();
        }
    }

    /**
     * Metodo per aggiungere un pilota alla classifica. Viene invocato a fine gara.
     * @param pilota il pilota da aggiungere alla classifica.
     */
    public synchronized void aggiungiInClassifica(Pilota pilota){
        if(!classifica.contains(pilota)){
            classifica.add(pilota);
        }
    }

    /**
     * Metodo per stampare la classifica finale della gara.
     */
    private void stampaClassifica() {
        String elenco = "";
        int contatore = 1;
        for(Pilota pilota : classifica){
            elenco += contatore + "º) " + pilota.getNome() + " [" + pilota.getSquadra() + "]\n";
            contatore++;
        }

        for(Pilota pilota : incidentati){
            elenco += "NC) " + pilota.getNome() + " [" + pilota.getSquadra() + "]\n";
        }

        classifica.get(0).setVincitore();

        stampa(elenco, "");
    }

    /**
     * Metodo per ottenere il vincitore della gara e confrontarlo con il pilota scelto dal'utente.
     * @return il pilota vincitore.
     * @see Pilota
     */
    public Pilota getVincitore() {
        for(Pilota pilota : gara.getPiloti()){
            if(pilota.isVincitore()){
                return pilota;
            }
        }
        return null;
    }

    /**
     * Metodo per salvare i progressi dei piloti e delle auto in un file di testo.
     */
    public void salvaProgressi(String username) {
        try (BufferedWriter scrittore = new BufferedWriter(new FileWriter("giocatori/" + username + ".gare"))) {
            for(Pilota pilota : gara.getPiloti()){
                scrittore.write(pilota.getNome() + ";" + pilota.getSquadra() + ";" + pilota.getVittorie() + ";" + pilota.getGareGiocate());
                scrittore.newLine();
            }
            for(Pilota pilota : incidentati) {
                scrittore.write(pilota.getNome() + ";" + pilota.getSquadra() + ";" + pilota.getVittorie() + ";" + pilota.getGareGiocate());
                scrittore.newLine();
            }
        } catch (IOException e) {
            System.out.println("\033[31mSi è verificato un errore nel salvare i progressi!\033[0m");
        }
    }

    /**
     * Metodo per stampare un messaggio a schermo. Utilizzato per segnalare avvisi, errori e progressi.
     * @param messaggio il messaggio da stampare.
     * @param tipoMsg il tipo di messaggio da stampare.
     */
    private synchronized void stampa(String messaggio, String tipoMsg){
        switch (tipoMsg) {
            case "errore" -> System.out.println("[\033[31mAVVISO\033[0m] " + messaggio);
            case "avviso" -> System.out.println("[\033[33mAVVISO\033[0m] " + messaggio);
            case "giro" -> System.out.println("\033[32m>>\033[0m " + messaggio);
            default -> System.out.println(messaggio);
        }
        System.out.println();
    }
} 