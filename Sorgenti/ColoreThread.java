import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;

public class ColoreThread extends Thread {

  TrovaIlColore parent;
  boolean azione;

  /////////////////////////////////////////////////////////////////////
  // mi segno le posizioni iniziali dove posizionare ipannelli
  // I pannello    8  8
  // II pannello  131 8
  // III pannello 255 8
  /////////////////////////////////////////////////////////////////////

  /**
   * Creao il thread.
   * @param tic il riferimento al genitore.
   */
  public ColoreThread ( TrovaIlColore tic ){
    super();
    parent = tic;
  }

  /**
   * Il metodo run. Al via, mediante setting a true della booleane azione,
   * mischio i pannelli.
   */
  public synchronized void run(){
    while( !azione ){
      try {
        wait();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    // un numero arbitrario di volte.
    int n =(int) Math.random()*10;

    while( n >= 0 ){
      shuffle();
    }
    }
  }

  /**
   * Questo metodo prende il primo pannello e lo scambia con uno a caso
   * degli altri due.
   */
  public void shuffle(){

    // il pannello da spostare
    int panel = (int)((Math.random()*10)%3);

    // la posizione dove metterlo
    int p = (int)(Math.random()*10)%2;

    switch (panel) {
       case 0:    // sposto il primo

         if ( p == 0 )  sswitch( parent.jPanel1, parent.jPanel2 );
         else sswitch ( parent.jPanel1, parent.jPanel3 );

         break;
       case 1:    // sposto il secondo

         if ( p == 0) sswitch( parent.jPanel2, parent.jPanel1 );
         else sswitch ( parent.jPanel2, parent.jPanel3 );

         break;
       default:   // sposto il terzo

         if ( p == 0) sswitch ( parent.jPanel3, parent.jPanel1 );
         else sswitch ( parent.jPanel3, parent.jPanel2 );
     }
  }

  public synchronized void sswitch( JPanel a, JPanel b ){
    try {
      wait();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}