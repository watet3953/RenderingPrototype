// Travis Waterman
// April 8th, 2022 - April 13th, 2022
// DisplayShell.java
// A Shell for Displaying the contents of GraphicsWindow.java

import javax.swing.JFrame;

class DisplayShell {
  GraphicsWindow gw;
  JFrame jf;

  public DisplayShell() { // sets up a graphics window, ties it to screen display
    gw = new GraphicsWindow();
    jf = new JFrame();
    jf.add(gw);
    jf.setSize(400, 400);
    jf.setVisible(true);
  }
}
