package tpsit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe che identifica una singola gara del GrandPrix nel suo complesso.
 * @author Matteo Bagnoletti Tini
 */
public class Gara {
    private Circuito circuito;
    private Giudice giudiceGara;
    private CopyOnWriteArrayList<Pilota> piloti = new CopyOnWriteArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Costruttore della classe Gara.
     * @param giudice il giudice della gara.
     * @see Giudice
     */
    public Gara(Giudice giudice) {
        this.giudiceGara = giudice;
        controllaDir();
    }

    /**
     * Metodo per la verifica dell'esistenza della direcotory "infogara" e del file "pilotiEauto.txt" per il funzionamento della gara.
     * Termina l'esecuzione nel caso in cui si verifichi un errore.
     */
    private void controllaDir() {
        File cartellaPiloti = new File("infogara");
        if(!cartellaPiloti.exists() && !cartellaPiloti.isDirectory()){
            System.out.println("\033[31mErrore nel caricamento della directory dei piloti/auto!\033[0m");
            System.exit(1);
        } else {
            File filePiloti = new File("infogara/pilotiEauto.txt");
            if(!filePiloti.exists() && !filePiloti.isFile()){
                System.out.println("\033[31mErrore nel caricamento del file dei piloti/auto!\033[0m");
                scanner.close();
                System.exit(1);
            }
        }
    }

    /**
     * Metodo che inizializza e configura il circuito di gara e carica i piloti memorizzati nel file "pilotiEauto.txt".
     */
    public void inizializza() {
        System.out.println("\033[33m---------------------< Circuito >--------------------\033[0m");
        System.out.print("Inserisci il nome del circuito in cui gareggiare: ");
        String nomeC = scanner.nextLine();

        int lunghezza = 0;
        do {
            System.out.print("Lunghezza in metri: ");
            try {
                lunghezza = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\033[31mErrore: valore non valido! (1 - N)\033[0m");
            }
        } while (lunghezza < 1);

        int giri = 0;
        do {
            System.out.print("Numero di giri: ");
            try {
                giri = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\033[31mErrore: valore non valido! (1 - N)\033[0m");
            }
        } while (giri  < 1);

        int ps = -1;
        do {
            System.out.print("Numero pit-stop permessi: ");
            try {
                ps = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\033[31mErrore: valore non valido! (0 - N)\033[0m");
            }
        } while (ps < 0);

        this.circuito = new Circuito(nomeC, lunghezza, ps, giri);
        this.piloti = caricaPiloti();
    }

    /**
     * Metodo per la lettura del file "pilotiEauto.txt" e l'ottenimento dei piloti e i loro dati memorizzati.
     * Termina l'esecuzione nel caso in cui si verifichi un errore.
     * @return l'array contenente i piloti.
     * @see Pilota
     */
    private CopyOnWriteArrayList<Pilota> caricaPiloti() {
        CopyOnWriteArrayList<Pilota> piloti = new CopyOnWriteArrayList<>();
        try (BufferedReader lettore = new BufferedReader(new FileReader("infogara/pilotiEauto.txt"))){
            String riga;
            while((riga = lettore.readLine()) != null){
                String[] info = riga.split(";");
                String nome = info[0];
                String squadra = info[1];
                int vittore = Integer.parseInt(info[2]);
                int giocate = Integer.parseInt(info[3]);
                piloti.add(new Pilota(nome , squadra, vittore, giocate, this.circuito, this.giudiceGara));
            }
            return piloti;
        } catch (IOException e){
            System.out.println("\033[31mErrore nella lettura del file dei piloti/auto!\033[0m");
            scanner.close();
            System.exit(1);
        }
        return null;
    }

    /**
     * Metodo che permette ad un giocatore di scegliere un pilota tra quelli disponibili.
     * @param giocatore il giocatore a cui assegnare il pilota scelto.
     * @see Giocatore
     */
    public void scegliPilota(Giocatore giocatore) {
        System.out.println("\033[33m----------------------< Pilota >---------------------\033[0m");
        int pilotaScelto = 0;
        do {
            System.out.println("Piloti in gara:");
            int contatore = 1;
            for(Pilota pilota: piloti){
                System.out.println(contatore + ") " + pilota.getNome() + " (" + pilota.getSquadra() + ") con " + pilota.getVittorie() + " vittorie su " + pilota.getGareGiocate() + " gare giocate.");
                contatore++;
            }
            System.out.print("\nScelta: ");
            pilotaScelto = Integer.parseInt(scanner.nextLine());
            if(pilotaScelto <= 0 || pilotaScelto > piloti.size()){
                System.out.println("\033[31mErrore: valore non valido!\033[0m");
            }
        } while (pilotaScelto <= 0 || pilotaScelto > piloti.size());
        String scelta = piloti.get(pilotaScelto -1).getNome();
        giocatore.setPilotaScelto(scelta);
    }

    /**
     * Metodo che permette di sabotare un pilota tra quelli in gara.
     * @see Pilota
     */
    public void trucca() {
        System.out.println("\033[33m-------------------< Trucca Gara >-------------------\033[0m");
        System.out.print("Vuoi truccare la gara? (S/N) ");
        String trucca = scanner.nextLine();
        if(trucca.equalsIgnoreCase("s")){
            int pilotaScelto = 0;
            do {
                System.out.print("Inserisci il numero del pilota da sabotare: ");
                try {
                    pilotaScelto = Integer.parseInt(scanner.nextLine());
                    if(pilotaScelto <= 0 || pilotaScelto > piloti.size()){
                        System.out.println("\033[31mErrore: valore non valido!\033[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\033[31mErrore: valore non valido!\033[0m");
                }
            } while (pilotaScelto <= 0 || pilotaScelto > piloti.size());
            piloti.get(pilotaScelto - 1).setSabotato();
        }
    }

    /**
     * Metodo che permette di rimuovere un pilota tra quelli in gara nel caso di incidente.
     * @param pilota il pilota incidentato da rimuovere
     * @see Giudice
     * @see Pilota
     */
    public void rimuoviPilota(Pilota pilota){
        piloti.remove(pilota);
    }

    /**
     * Getter per l'ottenimento dei pilori in gara (non incidentati).
     * @return l'array dei piloti in gara.
     */
    public CopyOnWriteArrayList<Pilota> getPiloti() {
        return piloti;
    }
}
