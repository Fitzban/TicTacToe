import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <p>Title: Giochi</p>
 * <p>Description: Una collezione di giochi</p>
 * @author Peretti Emanuele
 */

public class TrovaIlColore extends JFrame {
  /**
   * Ricreo il gioco dei tre napoletani. Si hanno n tentativi per vincere.
   * La finestra viene inizialmentye divisa in due parti.
   * La prima contenente tre pannelli su cui disegnare il retro delle carte;
   * la seconda contenente tre bottoni che, se premuti, rivelano la carta
   * scelta.
   */

  JPanel panelCarte = new JPanel();
  public JPanel jPanel1 = new JPanel();
  public JPanel jPanel2 = new JPanel();
  public JPanel jPanel3 = new JPanel();

  JPanel panelScelta = new JPanel();
  public JButton b1 = new JButton();
  public JButton b2 = new JButton();
  public  JButton b3 = new JButton();

  TitledBorder titledBorder1;
  Border border1;
  Border border2;

  private int coloredPanel;
  private int n;
  private Tris tris;

  public TrovaIlColore( Tris t, int numberOfAttempt ) {

    tris = t;
    n = numberOfAttempt;

    /**
     * Disegno la finestra.
     */
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    // disegno il retro delle carte
    ImageIcon back = new ImageIcon ( "XGothic.GIF" );
    jPanel1.setBackground( Color.black );
    jPanel2.setBackground( Color.black );
    jPanel3.setBackground( Color.black );

    // creo il thread e lo attivo
    ColoreThread ct = new ColoreThread( this );

    // scelgo un pannello
    choosePanel();

    // attivo il thread
    ct.start();

    // chiusura
    this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    // dimensioni
    setSize( 410, 320 );
    // visualizzazione
    setVisible( true );
  }

  /**
   * definizione settaggi frame.
   *
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(Color.white,4);
    titledBorder1 = new TitledBorder("");
    border2 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.blue,new Color(205, 255, 206),new Color(0, 0, 62),new Color(0, 0, 89));
    this.getContentPane().setBackground(SystemColor.control);
    this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    this.setResizable(false);
    this.setState(Frame.NORMAL);
    this.setTitle("___ Dove pensi che sia il colore ?? ___");
    this.getContentPane().setLayout(null);
    panelCarte.setBorder(border1);
    panelCarte.setBounds(new Rectangle(8, 9, 384, 224));
    panelCarte.setLayout(null);
    panelScelta.setBackground(Color.pink);
    panelScelta.setBorder(border1);
    panelScelta.setBounds(new Rectangle(12, 244, 375, 43));
    panelScelta.setLayout(null);
    b2.setBounds(new Rectangle(127, 7, 120, 30));
    b2.setBorder(border2);
    b2.setPreferredSize(new Dimension(100, 30));
    b2.setText("^");
    b2.addActionListener(new TrovaIlColore_b2_actionAdapter(this));
    b1.setBounds(new Rectangle(7, 7, 120, 30));
    b1.setBorder(border2);
    b1.setMaximumSize(new Dimension(45, 100));
    b1.setMinimumSize(new Dimension(45, 75));
    b1.setPreferredSize(new Dimension(100, 30));
    b1.setToolTipText("");
    b1.setText("^");
    b1.addActionListener(new TrovaIlColore_b1_actionAdapter(this));
    b3.setBounds(new Rectangle(247, 7, 121, 30));
    b3.setBorder(border2);
    b3.setPreferredSize(new Dimension(100, 30));
    b3.setText("^");
    b3.addActionListener(new TrovaIlColore_b3_actionAdapter(this));
    jPanel1.setEnabled(false);
    jPanel1.setBorder(border2);
    jPanel1.setBounds(new Rectangle(8, 8, 121, 208));
    jPanel2.setEnabled(false);
    jPanel2.setBorder(border2);
    jPanel2.setBounds(new Rectangle(131, 8, 121, 208));
    jPanel3.setEnabled(false);
    jPanel3.setBorder(border2);
    jPanel3.setBounds(new Rectangle(255, 8, 121, 208));
    this.getContentPane().add(panelCarte, null);
    this.getContentPane().add(panelScelta, null);
    panelScelta.add(b3, null);
    panelScelta.add(b1, null);
    panelScelta.add(b2, null);
    panelCarte.add(jPanel1, null);
    panelCarte.add(jPanel2, null);
    panelCarte.add(jPanel3, null);
  }

  /**
   * Sceglie random il pannello con il colore e ne segna l'id nella
   * variabile coloredPanel.
   */
  private void choosePanel(){
    int choise = (int)(( Math.random()*10 )%3);
    //System.out.println("choosePanel() generato: "+choise);
    switch (choise) {
      case 0:
        coloredPanel = 1;
        break;
      case 1:
        coloredPanel = 2;
        break;
      case 3:
        coloredPanel = 3;
        break;
    }
  }

  /**
   * Il bottone b1 è legato alla scelta del pannello1.
   * @param e
   */
  void b1_actionPerformed(ActionEvent e) {
     if ( coloredPanel == 1 ){
        //animazione();
        System.out.println("bravo");
        tris.show();
        this.hide();
     }
     else{
       n--;
       // peccato hai sbagliato ti rimangono n tentativi
       JOptionPane.showMessageDialog( this,
               " PECCATO !!\n "+
               " Ne hai ancora "+n+" .");

       if ( n == 0 ){
         System.out.println("gameOver();");
         System.exit( 0 );
       }
       choosePanel();
     }
  }

  /**
   * Il bottone b2 è legato alla scelta del pannello2.
   * @param e
   */
  void b2_actionPerformed(ActionEvent e) {
    if ( coloredPanel == 2 ){
       //animazione();
        System.out.println("bravo");
        tris.show();
        this.hide();
    }
    else{
      n--;
      // peccato hai sbagliato ti rimangono n tentativi
      JOptionPane.showMessageDialog( this,
              " PECCATO !!\n "+
               " Ne hai ancora "+n+" .");


       if ( n == 0 ){
         System.out.println("gameOver();");
         System.exit( 0 );
       }

       choosePanel();
    }
  }

  /**
   * Il bottone b3 è legato alla scelta del terzo pannello.
   * @param e
   */
  void b3_actionPerformed(ActionEvent e) {
    if ( coloredPanel == 3 ){
       //animazione();
        System.out.println("bravo");
        tris.show();
        this.hide();
    }
    else{
      n--;
      // peccato hai sbagliato ti rimangono n tentativi
      JOptionPane.showMessageDialog( this,
               " PECCATO !!\n "+
               " Ne hai ancora "+n+" .");

       if ( n == 0 ){
         System.out.println("gameOver();");
         System.exit( 0 );
       }

      choosePanel();
    }
  }
}

class TrovaIlColore_b1_actionAdapter implements java.awt.event.ActionListener {
  TrovaIlColore adaptee;

  TrovaIlColore_b1_actionAdapter(TrovaIlColore adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.b1_actionPerformed(e);
  }
}

class TrovaIlColore_b2_actionAdapter implements java.awt.event.ActionListener {
  TrovaIlColore adaptee;

  TrovaIlColore_b2_actionAdapter(TrovaIlColore adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.b2_actionPerformed(e);
  }
}

class TrovaIlColore_b3_actionAdapter implements java.awt.event.ActionListener {
  TrovaIlColore adaptee;

  TrovaIlColore_b3_actionAdapter(TrovaIlColore adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.b3_actionPerformed(e);
  }
}