package tpsit;

import java.io.*;
import java.util.Scanner;

/**
 * Classe che rappresenta un giocatore (l'utente umano).
 * Ogni giocatore ha un username e una password.
 * La password viene cifrata con il cifrario di Vigenere.
 * @author Matteo Bagnoletti Tini
 * @see Cifrario
 */
public class Giocatore {
    private String username;
    private String pilotaScelto;

    /**
     * Cifrario di Vigenere.
     * Rimane costante.
     * @see Cifrario
     */
    private Cifrario cifrario;

    Scanner scanner = new Scanner(System.in);
    /**
     * Costruttore della classe Giocatore.
     */
    public Giocatore() {
        controllaDir();
    }

    /**
     * Metodo per la verifica dell'esistenza della direcotory "giocatori" contenente i file dei giocatori precedentemente registrati.
     * La crea se non esiste.
     */
    private void controllaDir() {
        File cartellaGiocatori = new File("giocatori");
        if(!cartellaGiocatori.exists() && !cartellaGiocatori.isDirectory()){
            if(!cartellaGiocatori.mkdir()){
                System.out.println("\033[31mErrore nella creazione della directory dei giocatori!\033[0m");
                System.exit(0);
            }
        }
    }

    /**
     * Metodo per inizializzare il profilo del giocatore creando l'opportuno oggetto Cifrario
     * @see Cifrario
     */
    public void inizializza() {
        System.out.println("\033[33m--------------------< Benvenuto! >-------------------\033[0m\n");
        System.out.print("Inserisci il tuo username: ");
        this.username = scanner.nextLine();
        System.out.println("\033[33mL'accesso ai dati è protetto da crittografia.\nSe già possiedi un account inserisci la chiave da te precedentemente scelta, altrimenti inventane una!\033[0m");
        System.out.println("\033[33mLa chiave deve essere composta da soli caratteri ASCII e non deve essere più lunga della password.\033[0m");

        String verme;
        boolean chiaveValida;

        do {
            chiaveValida = true;
            System.out.print("Chiave-Vigenere: ");
            verme = scanner.nextLine();

            /* verifica correttezza chiave */
            if(!verme.isEmpty() && verme.length() < 95){
                for(int i = 0; i < verme.length(); i++){
                    if(verme.charAt(i) < 32 || verme.charAt(i) > 126){
                        System.out.println("Errore! La chiave contiene caratteri non validi!");
                        chiaveValida = false;
                        break;
                    }
                }
            } else {
                System.out.println("Errore! La chiave deve essere composta da soli caratteri ASCII e non deve essere più lunga della password.");
                chiaveValida = false;
            }
        } while (!chiaveValida);

        this.cifrario = new Cifrario(verme);
    }

    /**
     * Metodo per l'autenticazione dell'utente.
     * Mediante la funzione Giocatore.ricerca() è possibile effettuare o il login o la registrazione.
     * Il login permette 3 tentativi massimi di inserimento password, dopodiché il programma verrà terminato.
     */
    public void autentica() {
        int tentativi = 3;
        boolean autenticato = false;

        if(ricerca()){
            System.out.println("\n\033[33m----------------------< Login >----------------------\033[0m\n");
            do {
                System.out.print("Password: ");
                String password = scanner.nextLine();
                if(accesso(password)){
                    autenticato = true;
                } else {
                    tentativi--;
                    if(tentativi == 0){
                        System.out.println("\033[31mPassword errata! Terminazione programma in corso...\033[0m");
                        scanner.close();
                        System.exit(0);
                    } else {
                        System.out.println("\033[31mPassword errata! Tentativi rimanenti: " + tentativi + "\033[0m");
                    }
                }
            } while (!autenticato);
        } else {
            System.out.println("\n\033[33m------------------< Registrazione >------------------\033[0m\n");
            do {
                System.out.print("Scegli una password: ");
                String password = scanner.nextLine();
                if(registrazione(password)){
                    autenticato = true;
                } else {
                    System.err.println("Si è verificato un errore! Ritenta.");
                }
            } while (!autenticato);
        }
    }

    /**
    * Metodo che permette di ricercare se esiste un file con il nome del giocatore.
    * @return true se esiste, false altrimenti.
    */
    private boolean ricerca() {
      if(username != null && !username.equalsIgnoreCase("")) {
         File filGiocatore = new File("giocatori/" + username + ".giocatore");
            return (filGiocatore.exists() && filGiocatore.isFile());
      } else {
         return false;
      }
   }

   /**
    * Metodo che permette di verificare se la password inserita è uguale a quella decifrata salvata.
    * @param password Password inserita dall'utente.
    * @return true se la password è corretta, false altrimenti.
    */
    private boolean accesso(String password) {
      if (username != null && !username.equalsIgnoreCase("")) {
         try (BufferedReader lettore = new BufferedReader(new FileReader("giocatori/" + username + ".giocatore"))) {
            String pswCifrata = lettore.readLine(); // La riga del file contiene la password cifrata
            if (pswCifrata != null) {
                String pswInChiaro = cifrario.decifra(pswCifrata);
                return pswInChiaro.equals(password);
            } else {
               return false;
            }
         } catch (IOException e) {
            return false;
         }
      } else {
         return false;
      }
   }

    /**
    * Metodo che permette di registrare un nuovo giocatore.
    * Ad ogni giocatore sono associati 2 file: username.giocatore contenente la password cifrata del suo account; username.gare contenente i progressi ottenuti giocando.
    * @param password Password del nuovo giocatore.
    * @return true se la registrazione è avvenuta con successo, false altrimenti.
    */
    private boolean registrazione(String password) {
        if (username != null && !username.equalsIgnoreCase("")) {
           File fileUtente = new File("giocatori/" + username + ".giocatore");
           File gareUtente = new File("giocatori/" + username + ".gare");
            
           try {
               if(!fileUtente.createNewFile()){
                   return false;
               }
               if(!gareUtente.createNewFile()){
                   return false;
               }
           } catch (IOException e) {
               return false;
           }
           
           try (BufferedWriter scrittore = new BufferedWriter(new FileWriter("giocatori/" + username + ".giocatore"));
           BufferedWriter scrittoreGare = new BufferedWriter(new FileWriter("giocatori/" + username + ".gare"));
           BufferedReader lettore = new BufferedReader(new FileReader("infogara/default.txt"))) {
               scrittore.write(cifrario.cifra(password));
               scrittore.newLine();
               String riga;
               while((riga = lettore.readLine()) != null){
                   scrittoreGare.write(riga);
                   scrittoreGare.newLine();
               }
               return true;
           } catch (IOException e) {
               return false;
           }
            
        } else {
            return false;
        }
   }

    /**
     * Metodo per ottenere il pilota scelto dall'utente nella fase di avvio gara e confrontarlo con il vincitore.
     * @return il pilota scelto dall'utente
     * @see Giudice
     */
    public String getPilotaScelto() {
        return pilotaScelto;
    }

    /**
     * Metodo per impostare il pilota scelto dall'utente nella fase di avvio gara.
     * @see Giudice
     * @param pilotaScelto il nome del pilota scelto
     */
    public void setPilotaScelto(String pilotaScelto) {
        this.pilotaScelto = pilotaScelto;
    }
    
    public String getUsername() {
        return this.username;
    }
}
