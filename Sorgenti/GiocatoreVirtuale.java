import java.awt.*;
import java.awt.event.*;
import java.lang.Math.*;
import javax.swing.JButton;
import javax.swing.ImageIcon;

/*****************************************************************************
 * Il giocatore virtuale di tris si appoggia su matrici 3x3 per l'analisi
 * delle mosse fatte dall'avversario e lo studio delle proprie possibili mosse
 * in risposta. Utilizza algoritmi basati su punteggio. Ogni mossa conferisce
 * un particolare punteggio alle caselle rimanenti, la casella che ottiene
 * il punteggio maggiore verrà selezionata come successiva mossa.
 *  I punti si dividono in due categorie :
 * - punti avversari
 * - punti propri
 * Le caselle occupate dall'avversario hanno valore 1
 * Le caselle proprie hanno valore 3
 *
 *  Un giocatore per arrivare alla meta (mettere in fila 3 dei segni a sua
 * disposizione) attua i seguenti giochi:
 *
 * GIOCO SEMPLICE:
 *    mette le pedine in fila fino a raggiungere lo scopo
 * GIOCO DOPPIO:
 *    crea delle mosse che portano alla possibile vittoria in due modi differenti
 *
 * Un gioco semplice può essere riconosciuto dalla somma ded valore di due caselle
 * pari a 2(coppia avversaria) o pari a 6 (coppia propria).
 *   Una casella è rappresentante di gioco doppio se crea gioco (semplice o
 * doppio) su due linee che in essa si incontrano.
 *
 * MATRICI:
 *
 * av :   indica la disponibilità della cella
 *    true se la casella è disponibile
 *    false se la casella è occupata
 *
 * messe : contiene il valore riferito alla proprietà della cella
 *    1  avversario
 *    4  propria
 *
 * studioMosse : riferisce l'importanza della cella mediante punteggio
 *    +200  se il computer vince
 *    +50   se blocco una vincita avversaria
 *    +2    se blocco un gioco doppio avversario
 *    +1    se faccio una coppia
 *
 *  AL MOMENTO E' IMPLEMENTATO UN LIVELLO DI GIOCO PURAMENTE DIFENSIVO.
 *  MANCA TUTTA LA PARTE DI ATTACCO.
 *
 *
 *****************************************************************************/

public class GiocatoreVirtuale
                implements Runnable {

  Tris tris;
  String name;
  boolean[][] av = new boolean [3][3];   // matrice delle posizioni disponibili
  boolean threadSuspended, turn;
  int mie = 4;
  int avv = 1;
  static boolean finito;
  int righe;
  int colonne;

  ImageIcon x = new ImageIcon( "XGothic.GIF" );

  /***************************************************************************
   * Questa matrice viene inizilizzata alla prima mossa fatta ed aggiornato
   * ad ogni tale, e' usata per lo studio delle mosse da fare, qui viene
   * segnato il punteggio per ogni cella.
   **************************************************************************/
  public int [][] studioMosse = new int [3][3];

  /***************************************************************************
   * Inizializza la matrice delle mosse disponibili a true, viene chiamato
   * al momento della creazione del giocatore virtuale.
   **************************************************************************/
  public void initAv(){
     for (int i = 0; i < 3; i++) {
      for (int ii = 0; ii < 3; ii++) {
        av[i][ii] = true;
        //System.out.print(av[i][ii]+" ");
      }
      //System.out.println("");
    }
  }

  /***************************************************************************
   * Chiamato ad ogni mossa si occupa di controllare se ci sono mosse
   * disponibili, in caso contrario incomincia un nuova partita.
   **************************************************************************/
  public synchronized void controlla(){

    for (int i = 0; i < 3; i++) {
      finito = contrRiga( i );
     if ( finito ){
       try {
         System.out.println("\n .....  HAI PERSO  ....."+i);
         wait( 1000 );
       }
       catch (Exception ex) {
         ex.printStackTrace();
       }
       //System.exit(0);
     }
    }

    finito = true;

    for (int i=0; i<3; i++) {
      for (int ii=0; ii<3; ii++) {
        if (tris.tasti[i][ii].getText().compareTo("")==0)  finito=false;
      }
    }

    if(finito){
      System.out.println("il gioco era finito......");
      System.out.println("...mi sono permesso di ricominciare");
      tris.hide();
      new TrovaIlColore ( tris, 2 );
      for (int i=0; i<3; i++) {
        for (int ii=0; ii<3; ii++) {
          tris.tasti[i][ii].setText("");
          tris.tasti[i][ii].setIcon(new ImageIcon(""));
          tris.campoGioco.add(tris.tasti[i][ii]);
        }
      }
      tris.num_mosse = 0;
      tris.turn = !tris.turn;
      initAv();
      this.run();
      tris.add("Center",tris.campoGioco);
      tris.show();
    }
  }

  /***************************************************************************
   * Il costruttorei parametri che vuole sono un stringa che identifica
   * il suo nome (String s), un tris su cui giocare (Tris t), una
   * variabile booleana che ne indica lo stato (boolean th; true indica
   * sveglio, false lo mette in attesa), ed una variabile booleana che
   * indica il turno del giocatore (boolean turn).
   **************************************************************************/
  public GiocatoreVirtuale (String s, Tris t, boolean th,boolean tu){
    new Thread(s);
    this.tris = t;
    this.threadSuspended = th;
    this.turn = tu;
    initAv();
    init(messe, 0);
  }

  /***************************************************************************
   * Il metodo per la gestione del thread 'giocatoreVirtuale', all'interno
   * di un ciclo infinito si sincronizza con il giocatore umno mediante
   * due semafori: thread e turn, due variabili booleane.
   **************************************************************************/
  public void run(){
    while(true){
      //System.out.println("RUN ---");
      while (tris.turn) {
        try {
          synchronized (this) {
            this.wait( 2500 );
            this.threadSuspended = false;
            while (this.threadSuspended && tris.turn) {
              this.wait();
            }
          }
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      mettiMossa();
    }
  }

  int [][] bianco = new int[3][3];       // la priorità delle celle
  /***************************************************************************
   * Metodo chiamato quando il giocatore virtuale si trova a
   * dover fare la seconda mossa dopo che l'avversario ha
   * conquistato il centro, inizializza la matrice bianco[][]
   * secondo lo schema piu' appropriato.
   **************************************************************************/
  private void bianco1(){
    //System.out.println("BIANCO UNO");
    bianco[0][1] = 0;
    bianco[1][0] = 0;
    bianco[1][2] = 0;
    bianco[2][1] = 0;

    bianco[2][2] = 1;
    bianco[2][0] = 1;
    bianco[0][2] = 1;
    bianco[0][0] = 1;

    bianco[1][1] = 2;

    centro = false;
  }

  /***************************************************************************
   * Metodo chiamato quando il giocatore virtuale si trova a dover fare la
   * prima mossa, inizializza la matrice bianco[][] secondo lo schema piu'
   * appropriato.
   **************************************************************************/
  private void bianco2(){
    //System.out.println("BIANCO DUE");
    bianco[0][1] = 1;
    bianco[1][0] = 1;
    bianco[1][2] = 1;
    bianco[2][1] = 1;

    bianco[2][2] = 0;
    bianco[2][0] = 0;
    bianco[0][2] = 0;
    bianco[0][0] = 0;

    bianco[1][1] = 2;

    centro = true;
  }

  /***************************************************************************
   * Metodo chiamato quando il giocatore virtuale si trova a dover fare la
   * seconda mossa senza che l'avversario abbia conquistato il centro,
   * inizializza la matrice bianco[][] secondo lo schema piu' appropriato.
   **************************************************************************/
  private void bianco3(){
    bianco[0][1] = 2;
    bianco[1][0] = 2;
    bianco[1][2] = 2;
    bianco[2][1] = 2;

    bianco[2][2] = 0;
    bianco[2][0] = 0;
    bianco[0][2] = 0;
    bianco[0][0] = 0;

    bianco[1][1] = 5;
    centro = false;
  }

  /***************************************************************************
   * Setta le varibili turn e thread affinche il thread si sospenda
   **************************************************************************/
  public synchronized void stop(){
    tris.turn = true;
    this.threadSuspended = true;
    notify();
  }

  /***************************************************************************
   * inizializza a value la matrice matrix
   * @param matrix int[][]
   * @param value int
   **************************************************************************/
  public void init (int[][] matrix, int value){
    for (int i = 0; i < 3; i++) {
      for (int ii = 0; ii < 3; ii++) {
        matrix[i][ii] = value;
      }
    }
  }

  /***************************************************************************
   * Matrice usata per il recupero delle mosse fatte. Vengono segnate in base
   * al fautore della mossa.
   **************************************************************************/
  public int [][] messe = new int[3][3];
  boolean centro;  // false se l'avversario ha il centro

  /***************************************************************************
   * aggiorna la matrice messe
   **************************************************************************/
  public void trovaMosseFatte(){
    init(messe, 0);

    //System.out.println("trovaMosseFatte...");
    if (tris.tasti[1][1].getText().compareToIgnoreCase("O") == 0) centro = false;
             // System.out.println("centro: "+centro);
    for (int righe=0; righe<3; righe++) {
      for (int colonne=0; colonne<3; colonne++) {
        if (tris.tasti[righe][colonne].getText().compareToIgnoreCase("O") == 0){
                 messe[righe][colonne] = avv;
                 av[righe][colonne] = false;
        }
        else if (tris.tasti[righe][colonne].getText().compareToIgnoreCase("X") == 0){
                 messe[righe][colonne] = mie;
                 av[righe][colonne] = false;
        }
      }
    }
  }

  /***************************************************************************
   * cerco la mossa avversaria che lo farebbe vincere
   **************************************************************************/
  private void trovaVincentiAvversario (){
   // System.out.println("trovaVincentiAvversrio...");
     controllaRigheVincenti();
     controllaColonneVincenti();
     controllaDiagonaliVincenti();
  }

  /***************************************************************************
   *  gestisco le righe
   **************************************************************************/
  private void controllaRigheVincenti(){
   // System.out.println("controllaRighe...");
    for (int i = 0; i < 3; i++) {
      controllaRiga(i);
    }
  }

  /***************************************************************************
   * Controlla la riga passata come parametro cercando nella matrice delle
   * messe quella con due pedine avversarie. Le pedine avversarie hanno il
   * valore salvato in avv
   * @param riga int
   **************************************************************************/
  public void controllaRiga(int riga){
   // System.out.println("controllaRiga..."+riga);
    int re = messe[riga][0] + messe [riga][1] + messe[riga][2];
   // System.out.println(".... punteggio riga: "+re);
    if (re == (2*avv)) {
      segnaVincenteRiga(riga);
    }
  }

  public boolean contrRiga ( int riga ){
    boolean ret = false;
    if( messe[riga][0] + messe[riga][1] + messe[riga][2] == (3*mie) ||
      messe[riga][0] + messe[riga][1] + messe[riga][2] == (3*avv) ) ret = true;
    return ret;
  }

  /***************************************************************************
   * Accortosi di una riga vincente avversaria ne segno il punto
   * aggiungendo 50
   * @param riga int
   **************************************************************************/
  private void segnaVincenteRiga(int riga){
   // System.out.println("segnaVincenteRiga..."+riga);
    if(av[riga][2] ) studioMosse[riga][2] += 50;
    if(av[riga][0] ) studioMosse[riga][0] += 50;
    if(av[riga][1] ) studioMosse[riga][1] += 50;
  }

  /***************************************************************************
   * Gestisco le colonne.
   **************************************************************************/
  private void controllaColonneVincenti(){
   // System.out.println("controllaColonne...");
    for (int i = 0; i < 3; i++) {
      controllaColonna(i);
    }
  }

  /***************************************************************************
   * controlla la presenza di una coppia avversaria
   * @param colonna
   **************************************************************************/
  private void controllaColonna(int colonna){
    int re = messe[0][colonna] + messe [1][colonna] + messe[2][colonna];
    if (re == 2*avv) {
      segnaVincenteColonna(colonna);
         //   System.out.println("colonnaVincente..."+colonna);
    }
  }

  /***************************************************************************
   * accortosi di una vincente avversaria ne segno il punteggio  aggiungendo
   * 50 punti.
   *
   * @param colonna
   **************************************************************************/
  private void segnaVincenteColonna (int colonna){
    //System.out.println("segnaColonnaVincente..."+colonna);
    if (av[2][colonna] ) studioMosse[2][colonna] += 50;
    if (av[0][colonna] ) studioMosse[0][colonna] += 50;
    if (av[1][colonna] ) studioMosse[1][colonna] += 50;
  }

  /***************************************************************************
   * gestisco le diagonali
   **************************************************************************/
  private void controllaDiagonaliVincenti(){
    //System.out.println("controllaDiagonali...");
    controllaDiagonaleDxVincente();
    controllaDiagonaleSxVincente();
  }

  /***************************************************************************
   * controlla la diagonale sinistra ricercando la somma delle caselle.
   * Se il valore è 2 ne segna la coppia avversaria.
   **************************************************************************/
  private void controllaDiagonaleSxVincente(){
    //System.out.println("controllaDiagonale...");
    int re = messe[0][2] + messe[1][1];
    if (re == 2) segnaDiagonaleVincente(2,0);
    re = messe[1][1]  + messe[2][0];
    if (re == 2) segnaDiagonaleVincente(0,2);
  }

  /***************************************************************************
   * controlla la diagonale destra ricercando la somma delle caselle.
   * Se il valore è 2 ne segna la coppia avversaria.
   **************************************************************************/
  private void controllaDiagonaleDxVincente(){
    //System.out.println("controllaDiagonale dx...");
       int re = messe[0][0] + messe[1][1];
    if (re == (2*avv)) segnaDiagonaleVincente(2,2);
       re = messe[1][1] + messe[2][2];
    if (re == (2*avv)) segnaDiagonaleVincente(0,0);
  }

  /***************************************************************************
   * Segna la diagonale vincente avversaria aggiungendo 50 p.ti alla cella
   * di coordinate riga, colonna.
   *
   * @param riga int
   * @param colonna int
   **************************************************************************/
  private void segnaDiagonaleVincente(int riga, int colonna){
    //System.out.println("diagonale vincente...");
    if (av[riga][colonna]){
          studioMosse[riga][colonna] += 50;
    }
  }



//----------------------------------------------------
 // gestisco i giochi doppi avversari
 // un gioco doppio viene cercato su tutte le caselle
 // disponibili
 // Una
 private void trovaDoppieAvversarie(){
   //System.out.println("trovaDoppie...");
   for (int i = 0; i < 3; i++) {
     for (int ii = 0; ii < 3; ii++) {
       trovaDoppiaAvversaria(i,ii);
     }
   }
 }


 public void trovaDoppiaAvversaria(int a, int b){
   //System.out.println("trovaDoppiaAvversaria..."+a+""+b);
   if (av[a][b] && sommaRiga(a) == 1 && sommaRiga(a) == sommaColonna(b)) {
       studioMosse[a][b] += 5;
       if (av[(a+1)%3][b]) 	 studioMosse[(a+1)%3][b] += 5;
       if (av[(a+2)%3][b]) 	 studioMosse[(a+2)%3][b] += 5;
       if (av[a][(b+1)%3])       studioMosse[a][(b+1)%3] += 5;
       if (av[a][(b+2)%3])       studioMosse[a][(b+2)%3] += 5;
     //  System.out.println("------------------>doppia trovata <-----: " + a + "" + b);
     }
 }






 public int sommaRiga(int riga){
   int ret=0;
   for (int i = 0; i < 3; i++) {
     ret += messe[riga][i];
   }
   //System.out.println("sommariga..."+riga+" : "+ret);
   return ret;
 }


 public int sommaColonna(int colonna){
   int ret=0;
   for (int i = 0; i < 3; i++) {
     ret += messe[i][colonna];
   }
   //System.out.println("sommacolonna..."+colonna+" : "+ret);
   return ret;
 }


// ora mi occupo delle mie
 public void trovaVincenti(){
   for (int i = 0; i < 3; i++) {
     vincentiRiga(i);
     vincentiColonna(i);
   }
  vincentiDiagonaleSx();
  vincentiDiagonaleDx();
 }

public void vincentiDiagonaleDx(){
   if (messe[1][1] == mie){
     int re = 0;
     re = messe[0][2] + messe[1][1] + messe[2][0];
     if (re == (2*mie) ) trovaVittoriaDiagonale(1);
   }
 }


 public void vincentiDiagonaleSx(){
   if (messe[1][1] == mie){
     int re = 0;
     re = messe[0][0] + messe[1][1] + messe[2][2];
     if (re == (2*mie) ) trovaVittoriaDiagonale(0);
   }
 }



 public void vincentiDiagonaleDs(){
   if (messe[1][1] == mie){
     int re = 0;
     re = messe[0][2] + messe[1][1] + messe[2][0];
     if (re == (2*mie) ) trovaVittoriaDiagonale(2);
   }
 }



 public void trovaVittoriaDiagonale(int i){
   if (i == 0){
      if (messe[0][0] == 2) studioMosse[2][2] += 500;
      if (messe[2][2] == 2) studioMosse[0][0] += 500;
   }
   else if (i == 2){
     if (messe[0][2] == 2) studioMosse[2][0] += 500;
     if (messe[2][0] == 2) studioMosse[0][2] += 500;
   }
 }

 public void vincentiRiga(int riga){
   int re = 0;
   re = messe[riga][0] + messe[riga][1] + messe[riga][2];
   if (re == (2*mie) ) {
   		rigaVincente(riga);
   		}
 }

 // aggiunge 500 punti nella posizione vuota della riga
 // passata in esame
 public void rigaVincente(int riga){
   if (av[riga][0]) studioMosse[riga][0] += 500;
   if (av[riga][1]) studioMosse[riga][1] += 500;
   if (av[riga][2]) studioMosse[riga][2] += 500;
 }


 // cerca nelle colonne della matrice delle messe quella colonna che
 // ha due mie pedine. Una mia pedina segna il valore 'mie' nellaù
 // cella corrispondente
 public void vincentiColonna(int colonna){
   int re=0;
   re = messe[0][colonna] + messe[1][colonna] + messe[2][colonna];
   if (re == (2*mie) ) colonnaVincente(colonna);
 }


 // trovata una posizione che fa vincere il giocatore virtuale
 // aggiunge 200 punti e continua nella ricerca
 private void colonnaVincente(int colonna){
   if ((messe[0][colonne]+messe[1][colonne]) == (2*mie) )
           studioMosse[2][colonna] += 500;
   if ((messe[1][colonne]+messe[2][colonne]) == (2*mie) )
           studioMosse[0][colonna] += 500;
   if ((messe[0][colonne]+messe[2][colonne]) == (2*mie) )
           studioMosse[1][colonna] += 500;
 }


// percorre le caselle della matrice studioMosse ricercando
 // la cella col punteggio maggiore. Quando trovata se possibile
 // mette il segno
 public void scegliMossa(){
   //System.out.println("scegliMossa...");
   int[] re = new int[2];
   int max=0;
   for (int i = 0; i < 3; i++) {
     for (int ii = 0; ii < 3; ii++) {
       if (av[i][ii] && studioMosse[i][ii] > max){
         max = studioMosse[i][ii];
         re[0] = i;
         re[1] = ii;
       }
     }
   }
   mettiPedina(re[0],re[1]);
 }


// trovata la posizione giusta segno e passo il turno
  public void mettiPedina(int riga, int colonna){
    //System.out.println("mettiPedina..."+riga+" "+colonna);
     this.tris.tasti[riga][colonna].setText("X");
     this.tris.tasti[riga][colonna].setIcon( x );
     av[riga][colonna] = false;
     messe[riga][colonna] += mie;
     tris.num_mosse++;
     controlla();
     stop();
  }


  // inizializziamo la matrice di studio
  public void initStudio(){
    //System.out.println("inizializzo la matrice studio .......");
    studioMosse = bianco;
  }


  // ritorna true se l'avversrio ha preso un angolo
  private boolean angolo(){
    boolean ret = false;
            if (tris.tasti[0][0].getText().compareTo("O") == 0 ||
                tris.tasti[0][2].getText().compareTo("O") == 0 ||
                tris.tasti[2][0].getText().compareTo("O") == 0 ||
                tris.tasti[2][2].getText().compareTo("O") == 0 )
                                  ret = true;
    return ret;
  }

 /*********************************************************
  *********************************************************
  ** ------------------ THE CORE ----------------------- **
  *********************************************************
  *********************************************************/

  public synchronized void mettiMossa (){
    //System.out.println("mettiMossa...");
    tris.thread=false;

// ------------------------------------
// tutti questi metodi lavorano sulla matrice studioMosse
// -----------------------------------

    // inizializzo il bianco
    if (tris.num_mosse == 1){
     if (angolo()) bianco3();
     else if (tris.tasti[1][1].getText().compareTo("O") == 0) bianco1();
     else bianco2();
    }
    else if (tris.num_mosse == 0)bianco1();

    // leggo il campo gioco cercando le caselle libere
    trovaMosseFatte();

    // reinizializzare a zero la matrice di studio
    initStudio();

    //trovo le possibili coppie avversarie vincenti
    // le vincenti avversario aggiungono 50
    trovaVincentiAvversario();

    // trovo le possibili doppie mosse avversarie
    // le doppie aggiungono 5
    trovaDoppieAvversarie();

    // trovo le mie vincenti
    // le mie vincenti aggiungono 1000
    trovaVincenti();

/*    for (int i = 0; i < 3; i++) {
      for (int ii = 0; ii < 3; ii++) {
        System.out.print(studioMosse[i][ii]+" ");
      }
      System.out.println("");
    }*/

    // trovo le mie doppiemosse
    // le mie doppie mosse aggiungono 2

    // trovo le mie coppie
    // le coppie aggiungono 1


    // in fine metto la pedina nella cella ove
    // risiede il punteggio maggiore
         // in caso di 2 o + celle con pri punteggio un tra di esse a caso
    scegliMossa();
  }
}
