// Travis Waterman
// April 7th, 2022 - June 9th, 2022
// GraphicsWindow.java
// A Class to do the actual display stuff

import java.awt.*;
import java.util.ArrayList;

class GraphicsWindow extends Canvas { // handles 2d drawing
  ArrayList<Triangle2D> drawStorage = new ArrayList<Triangle2D>(); // storage for all the triangles to draw to screen
  boolean debugMode = false; // no setting for this yet, just set in-code

  GraphicsWindow() {
    setBackground(Color.BLACK);
  }
  
  public void frameTick() { // an inbound request to reload the graphics
    sanitizeDrawStorage();
    sortDrawStorage();
    repaint();
  }

  public void addTriangle(Triangle2D t) { // adds a triangle to the end of the render stack (top);
    drawStorage.add(t);
  }

  public void clearStorage() { // clears the drawStorage, possibly unoptimal solution?
    drawStorage.clear();
  }

  public void printTriangle(Triangle2D tri) { // a console output of the coordinates being written to screen
    System.out.println(tri.xCords[0] + " " + tri.yCords[0]);
    System.out.println(tri.xCords[1] + " " + tri.yCords[1]);
    System.out.println(tri.xCords[2] + " " + tri.yCords[2]);
    System.out.println();
  }

  public void paint(Graphics g) { // actual drawing method
    super.paint(g);
    g.setColor(Color.WHITE);
    for (int x = 0; x < drawStorage.size(); x++) {
      if (debugMode) {
        printTriangle(drawStorage.get(x));
      }
      g.drawPolygon(drawStorage.get(x).xCords, drawStorage.get(x).yCords, 3);
      g.setColor(new Color((float)(Math.random()), (float)(Math.random()), (float)(Math.random())));
      g.fillPolygon(drawStorage.get(x).xCords, drawStorage.get(x).yCords, 3);
      g.setColor(Color.WHITE);
    }
  }

  public void sanitizeDrawStorage() {
    for (int i = 0; i < drawStorage.size(); i++) {
      if (drawStorage.get(i).distance <= 1) {
        drawStorage.remove(i);
      }
    }
  }
  
  public void sortDrawStorage() {
    for (int i = 0; i < drawStorage.size(); i++) {
      for (int j = drawStorage.size() - 1; j > i; j--) {
        if (drawStorage.get(i).distance < drawStorage.get(j).distance) {
          Triangle2D temp = drawStorage.get(i);
          drawStorage.set(i, drawStorage.get(j));
          drawStorage.set(j,temp);
        }
      }
    }
  }
}
