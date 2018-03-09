import java.awt.*;
import java.awt.event.* ;

class WindowAction extends WindowAdapter {
  public void windowClosing (WindowEvent e){
    e.getWindow().dispose();
  }
  public void windowClosed (WindowEvent e){
    e.getWindow().dispose();
    System.exit(0);
  }
}//chiude il frame da creare e poi legare al frame
