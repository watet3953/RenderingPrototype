// Travis Waterman
// April 8th, 2022 - June 9th, 2022
// Triangle2D.java
// A class for storing graphical triangle coordinates, in 2D.

class Triangle2D {
  int[] xCords = {0,0,0};
  int[] yCords = {0,0,0};
  float distance;

  public Triangle2D(int x1, int y1, int x2, int y2, int x3, int y3, float dist) {
    xCords[0] = x1;
    xCords[1] = x2;
    xCords[2] = x3;
    yCords[0] = y1;
    yCords[1] = y2;
    yCords[2] = y3;
    distance = dist;
  }

  public Triangle2D(Vector2 one, Vector2 two, Vector2 three, float dist) {
    xCords[0] = (int)(one.x);
    xCords[1] = (int)(two.x);
    xCords[2] = (int)(three.x);
    yCords[0] = (int)(one.y);
    yCords[1] = (int)(two.y);
    yCords[2] = (int)(three.y);
    distance = dist;
  }
}
