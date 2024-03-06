package tpsit;

/**
 * @author Matteo Bagnoletti Tini
 * @version 1.0
 * @see <a href="https://github.com/matbagnoletti/Encryption">Original project (Encryption)</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html">Documentazione Oracle Thread</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html">Documentazione Oracle Runnable</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/File.html">Documentazione Oracle File</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html">Documentazione Oracle FileWriter</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html">Documentazione Oracle BufferedReader</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html">Documentazione Oracle InputStreamReader</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html">Documentazione Oracle IOException</a>
 * 
 * Classe che permette di cifrare e decifrare un messaggio secondo Vigenere (in questo caso la password);
 */

public class Cifrario {
    private Matrice matrice;
    private Vigenere vigenere;
    private Thread cifratore;
    public Cifrario(String verme){
        this.matrice = new Matrice(verme);
        this.vigenere = new Vigenere(0, 95, 0, 95, matrice);
        this.cifratore = new Thread(this.vigenere);
        this.cifratore.start();
    }
    public String cifra(String testoInChiaro){
        try {
            this.cifratore.join();
        } catch (InterruptedException e) {
            System.out.println("Attenzione! Il thread cifratore è stato interrotto!");
            return testoInChiaro;
        }
        testoInChiaro = testoInChiaro.toUpperCase();

        /* cifratura */
        String testoCifrato;
        try {
            testoCifrato = matrice.cifra(testoInChiaro);
        } catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("Errore! Il testo inserito contiene caratteri non cifrabili! L'operazione verrà annullata.\n");
            testoCifrato = testoInChiaro;
        }

        return testoCifrato;
    }

    public String decifra(String testoCifrato) {
        try {
            this.cifratore.join();
        } catch (InterruptedException e) {
            System.out.println("Attenzione! Il thread cifratore è stato interrotto!");
            return testoCifrato;
        }
        testoCifrato = testoCifrato.toUpperCase();

        /* decifratura */
        String testoInChiaro;
        try {
            testoInChiaro = matrice.deCifra(testoCifrato);
        } catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("Errore! Il testo inserito contiene caratteri non cifrabili! L'operazione verrà annullata.\n");
            return testoCifrato;
        }

        return testoInChiaro;
    }
}