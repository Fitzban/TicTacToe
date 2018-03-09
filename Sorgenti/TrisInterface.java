/*
//l'avversario nn ha creato mosse vincenti la creo io
    if (turn){
      for (int righe=0; righe<3; righe++) {
        for (int colonne=0; colonne<3; colonne++) {
          if(tris.tasti[righe][colonne].getLabel().compareTo("O")==0){
            try{
              if(tris.tasti[righe+1][colonne].getLabel().compareTo("")==0
                 && !tris.turn
                 && tris.tasti[righe+1][colonne].getLabel().compareTo("")==0){
                 tris.tasti[righe+1][colonne].setLabel("X");
                 controlla();
                 stop();
              }
              else if(tris.tasti[righe+2][colonne].getLabel().compareTo("")==0
                 && !tris.turn
                 && tris.tasti[righe+1][colonne].getLabel().compareTo("")==0){
                tris.tasti[righe+1][colonne].setLabel("X");
                controlla();
                stop();
              }
              else if(tris.tasti[righe][colonne+1].getLabel().compareTo("")==0
                 && !tris.turn
                 && tris.tasti[righe+1][colonne].getLabel().compareTo("")==0){
                tris.tasti[righe][colonne+1].setLabel("X");
                controlla();
                stop();
              }
              else if(tris.tasti[righe][colonne+2].getLabel().compareTo("")==0
                 && !mossaFatta
                 && tris.tasti[righe+1][colonne].getLabel().compareTo("")==0){
                tris.tasti[righe][colonne+2].setLabel("X");
                controlla();
                stop();
              }

            }catch (ArrayIndexOutOfBoundsException e){};
            }
          }
        }
      }                 */
/******************************************************************************
 * Autore      : Peretti Emnuele
 * Data        : Febbraio 2004
 * Descrizione : Questa interfaccia raccoglie i metodi della classe
 *        GiocatoreVirtuale del gioco del tris. Per ogni metodo viene
 *        data la descrizione ed il comportamento.
 *****************************************************************************/

public interface TrisInterface{
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
   *    3  propria
   *
   * studioMosse : riferisce l'importanza della cella mediante punteggio
   *    +200  se il computer vince
   *    +50   se blocco una vincita avversaria
   *    +2    se blocco un gioco doppio avversario
   *    +1    se faccio una coppia
   *.
   *
   *****************************************************************************/


public void initAv();


}
