package tpsit;

/**
 * Classe che rappresenta un pilota.
 * @author Matteo Bagnoletti Tini
 */
public class Pilota extends Thread {
    private String nome;
    private String squadra;
    private int vittorie;
    private int gareGiocate;
    /**
     * Variabile che indica se il pilota ha vinto la gara
     */
    private boolean vincitore = false;
    private Giudice giudice;
    private volatile boolean safetyCar = false;
    private int prossimoGiro;
    /**
     * Variabile che indica se il pilota ha avuto un incidente
     */
    private boolean incidentato = false;
    /**
     * Auto del pilota.
     */
    public Auto auto;
    
    /**
     * Costruttore della classe Pilota.
     * @param nome Nome del pilota.
     * @param squadra Squadra del pilota.
     * @param vittorie Vittorie del pilota.
     * @param gareGiocate Gare giocate in totale dal pilota.
     */
    public Pilota(String nome, String squadra, int vittorie, int gareGiocate, Circuito circuito, Giudice giudice) {
        this.nome = nome;
        this.squadra = squadra;
        this.vittorie = vittorie;
        this.gareGiocate = gareGiocate;
        this.auto = new Auto(squadra, circuito);
        this.giudice = giudice;
        this.prossimoGiro = circuito.getLunghezzaGiro();
    }

    public String getNome() {
        return nome;
    }

    public String getSquadra() {
        return squadra;
    }

    public int getVittorie() {
        return vittorie;
    }

    public int getGareGiocate() {
        return gareGiocate;
    }

    public boolean isVincitore() {
        return vincitore;
    }

    public void setVincitore() {
        this.vittorie++;
        this.vincitore = true;
    }

    public void setSafetyCar(boolean safetyCar) {
        this.safetyCar = safetyCar;
    }

    public void setSabotato() {
        this.auto.setSabotata();
    }

    /**
     * Metodo principale di ogni pilota (Thread) per cui, finché lo spazio rimanente del circuito non sarà pari a zero, esso potrà avere incidenti, effettuare pit stop o avanzare.
     */
    @Override
    public void run() {
        this.gareGiocate++;
        int contaGiri = 0;
        int velocita = 0;

        while(auto.getSpazioRimanente() > 0){
            if(auto.incidente(contaGiri)){
                giudice.segnalaIncidente(this);
                incidentato = true;
                break;
            } else if(auto.pitstop(contaGiri)){
                giudice.segnalaPitStop(this);
            } else {
                velocita = auto.percorri(safetyCar);
                if(auto.getSpazioPercorso() >= prossimoGiro){
                    contaGiri++;
                    prossimoGiro += prossimoGiro;
                    giudice.segnalaProgressi(this, contaGiri, auto.getSpazioPercorso(), velocita);
                }
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                System.out.println("\033[31mSi è verificato un errore nel sospendere " + getNome() + "!\033[0m");
            }
        }

        if(!incidentato){
            giudice.aggiungiInClassifica(this);
        }
    }
}
