package tpsit;

/**
 * @author Monica Ciuchetti & Matteo Bagnoletti Tini
 * @version 2.0
 * @since 1.0
 * @see <a href="https://github.com/matbagnoletti/Encryption">Original project (Encryption)</a>
 */

public class Matrice {

    char[][] mv;
    String verme;

    /**
     * Nuova matrice di Vigenere
     * Contiene tutti i caratteri ASCII stampabili (da 32 a 126 compresi)
     * @param verme
     */
    public Matrice(String verme) {
        this.mv = new char[95][95];
        this.verme = verme;
    }

    public void setElemento(int r, int c, int el) {
        this.mv[r][c] = (char) el;
    }

    public char getElemento(int r, int c) {
        return mv[r][c];
    }

    public void stampa() {
        int r;
        int c;

        for (r = 0; r < 95; r++) {
            for (c = 0; c < 95; c++) {
                System.out.print(this.getElemento(r, c));
            }
            System.out.print("\n");
        }
    }

    public String cifra(String fch) throws ArrayIndexOutOfBoundsException {
        StringBuilder fcf;
        int k, j, col, row;
        fcf = new StringBuilder();
        k = 0;

        for (j = 0; j < fch.length(); j++) {

            if (k == verme.length()) {
                k = 0;
            }

            if(!Character.isLetter(fch.charAt(j))){
                fcf.append(fch.charAt(j));
            } else {
                col = (int) fch.charAt(j) - 32;
                row = (int) verme.charAt(k) - 32;
                fcf.append(mv[row][col]);
            }
            k++;
        }

        return (fcf.toString());
    }

    public String deCifra(String fcf) {
        StringBuilder fch = new StringBuilder();

        int k, j, col, row;

        k = 0;

        for (j = 0; j < fcf.length(); j++) {

            if (k == verme.length()) {
                k = 0;
            }

            if(!Character.isLetter(fcf.charAt(j))){
                fch.append(fcf.charAt(j));
            } else {
                row = (int) verme.charAt(k) - 32;
                for (col = 0; col < 95; col++) {
                    if (mv[row][col] == fcf.charAt(j)) {
                        char car = (char) (col + 32);
                        fch.append(car);
                    }
                }
            }
            k++;
        }

        return (fch.toString());
    }
}