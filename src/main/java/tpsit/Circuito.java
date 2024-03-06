package tpsit;

/**
 * Classe che rappresenta un circuito della gara.
 * @author Matteo Bagnoletti Tini
 */
public class Circuito {
    /**
     * Nome del circuito.
     */
    private String nome;
    /**
     * Lunghezza del circuito in metri.
     */
    private int lunghezza;
    /**
     * Numero di PitStop permessi del circuito.
     */
    private int numeroPitStop;
    /**
     * Numero di giri del circuito.
     */
    private int numeroGiri;
    /**
     * Lunghezza di un giro del circuito calcolato matematicamente.
     */
    private int lunghezzaGiro;
    /**
     * Variabile booleana che indica se la safety car è attiva.
     */
    private boolean safetyCar;

    /**
     * Costruttore della classe Circuito.
     * @param nome nome del circuito
     * @param lunghezza lunghezza del circuito in metri
     * @param numeroPitStop numero di PitStop permessi del circuito
     * @param numeroGiri numero di giri del circuito
     */
    public Circuito(String nome, int lunghezza, int numeroPitStop, int numeroGiri) {
        this.nome = nome;
        this.lunghezza = lunghezza;
        this.numeroPitStop = numeroPitStop;
        this.numeroGiri = numeroGiri;
        this.safetyCar = false;
        this.lunghezzaGiro = lunghezza / numeroGiri;
    }

    /**
     * Metodo che restituisce il nome del circuito.
     * @return nome del circuito
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo che restituisce la lunghezza del circuito.
     * @return lunghezza del circuito
     */
    public int getLunghezza() {
        return lunghezza;
    }

    /**
     * Metodo che restituisce il numero di PitSto permessi del circuito.
     * @return numeroPitStop del circuito
     */
    public int getNumeroPitStop() {
        return numeroPitStop;
    }

    /**
     * Metodo che restituisce il numero di giri del circuito.
     * @return numero di giri del circuito
     */
    public int getNumeroGiri() {
        return numeroGiri;
    }

    /**
     * Metodo che restituisce se la safety car è attiva.
     * @param safetyCar true se la safety car è attiva, false altrimenti.
     */
    public void setSafetyCar(boolean safetyCar) {
        this.safetyCar = safetyCar;
    }

    /**
     * Metodo che restituisce la lunghezza di un singolo giro in metri.
     * @return lunghezzaGiro
     */
    public int getLunghezzaGiro() {
        return lunghezzaGiro;
    }
}
