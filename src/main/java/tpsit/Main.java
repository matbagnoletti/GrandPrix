package tpsit;

/**
 * Classe di avvio del programma.
 * @author Matteo Bagnoletti Tini
 */
public class Main {
    public static void main(String[] args){
        Giocatore giocatore = new Giocatore();
        giocatore.inizializza();
        giocatore.autentica();

        Giudice giudice = new Giudice(giocatore);
        giudice.avviaGara();
        if(giudice.getVincitore().getNome().equals(giocatore.getPilotaScelto())){
            System.out.println("\033[32mHai vinto! :)\033[0m");
        } else {
            System.out.println("\033[31mHai perso! :(\033[0m");
        }
        giudice.salvaProgressi(giocatore.getUsername());
    }
}
