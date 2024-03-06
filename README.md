<h1 align="center">GrandPrix</h1>

<p align="center" style="font-family: monospace">Made by <a href="https://github.com/matbagnoletti">@matbagnoletti</a></p>
<p align="center" style="font-family: monospace">Docenti: prof.ssa <a href="https://github.com/mciuchetti">@mciuchetti</a> e prof.ssa Fioroni</p>
<p align="center" style="font-family: monospace">Corso TPSIT a.s. 2023/2024, <a href="https://www.avoltapg.edu.it/">ITTS A. Volta (PG)</a></p>
<p align="center">
    <img src="https://img.shields.io/github/last-commit/matbagnoletti/GrandPrix?style=for-the-badge" alt="Ultimo commit">
    <img src="https://img.shields.io/badge/Language-Java-blue?style=for-the-badge" alt="Java">
</p>

## Descrizione e obiettivi
Applicazione Java che simula una gara di auto da corsa in ambiente multi-thread.

Il gioco è composto da un giudice di gara, dei giocatori, dei piloti e le rispettive auto, un circuito e un sistema di cifratura e decifratura delle password.

- È previsto l'utilizzo e la gestione degli stream per la lettura e scrittura dei dati su file. I dati dei piloti e delle loro auto sono memorizzati in un file, così come quelle dei giocatori, le cui password sono cifrate secondo il cifrario di Vigenère.
- Ogni giocatore può scegliere il numero di auto da far partire contemporaneamente e il circuito su cui gareggiano. Quest'ultimo ha una lunghezza che viene preimposta dal giocatore, come anche il numero di giri, il numero di possibili pit stop e la possibilità di truccare una macchina o di fermarne una per un incidente. Deve poi essere eventualmente possibile far intervenire (in un certo punto della gara) la safety car. 
- Il giudice di gara deve poter comunicare a tutti i partecipanti e al pubblico l’inizio e la fine della gara, nonché, al termine della gara, il vincitore, salvando i dati della classifica in un file che può essere letto dal giocatore alla fine del gioco.
   
## Requisiti
- [Java](https://www.oracle.com/it/java/technologies/downloads/) (utilizzata la versione 21.0.2)
- [Maven](https://maven.apache.org/download.cgi) (utilizzata la versione 3.9.6)

È possibile verificare la corretta installazione di Java e Maven eseguendo i seguenti comandi da terminale:
```bash
java -version
mvn -v
```

## Installazione
1. Scaricare il file compresso del progetto
2. Estrarre il progetto
3. Eseguire il Server e il Client separatamente:
  - Tramite IDE
  - Tramite terminale:
    1. Naviga nella root del progetto
    2. Esegui la build del progetto: `mvn clean install`.
    3. Identifica il file `jar` nella directory `target` e avvia la classe Main: `java -jar target/GrandPrix-1.0-SNAPSHOT.jar Main`.

## Struttura e funzionamento
Il progetto si compone dalle seguenti classi:

- [Giudice.java](src/main/java/tpsit/Giudice.java): è la classe che gestisce:
    - l'account dell'utente sotto forma di `Giocatore`
    - la lettura da file dei dati dei piloti e delle loro auto
    - l'avvio della la gara, la sua gestione e la classifica. 

- [Giocatore.java](src/main/java/tpsit/Giocatore.java): classe che identifica l'utente che sta giocando.

- [Pilota.java](src/main/java/tpsit/Pilota.java): Thread creato dalla classe `Giudice.java` per poter concorrere in gara simultaneamente ad altri piloti. Il metodo `run()` verrà utilizzato finché la propria `Auto` (associata come attributo) non abbia terminato il circuito (vedi metodo `percorri()`), segnalando al `Giudice` ogni qual volta venga completato un giro.

- [Auto.java](src/main/java/tpsit/Auto.java): è una classe associata a ciascun `Pilota` e possiede i metodi per poter avanzare nel circuito o generare (in modo random) un pit stop (se possibile)o un incidente. Eseguendo un pit stop, per quel round la velocità dell'auto sarà ridotta a 0.
   
- [Cifrario.java](src/main/java/tpsit/Cifrario.java), [Matrice.java](src/main/java/tpsit/Matrice.java) e [Vigenere.java](src/main/java/tpsit/Vigenere.java): classi utilizzate per cifrare e decifrare la password dei giocatori salvata su file. Vedi progetto [Encryption](https://www.github.com/matbagnoletti/Encryption).

- [Circuito.java](src/main/java/tpsit/Circuito.java): classe che rappresenta un generico circuito che deve essere creato dall'utente, specificando la lunghezza in metri, il numero di giri, e il numero di pit stop possibili. Possiede un attributo `safetyCar` che viene controllato dal `Giudice` e, se impostato su `true`, fa avanzare tutte le auto a 50 m/s, così che nessuna possa superare l'altra.

- [Gara.java](src/main/java/tpsit/Gara.java): classe che rappresenta la gara, con i metodi per poterla avviare, fermare e gestire la classifica.

- [Main.java](src/main/java/tpsit/Main.java): classe principale del progetto, che avvia il gioco.

## Licenza d'uso
Questo progetto (e tutte le sue versioni) sono rilasciate sotto la [MB Collective Copyleft License](LICENSE).