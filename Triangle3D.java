// Travis Waterman
// April 12th, 2022 - June 7th, 2022
// Triangle3D.java
// A class for storing graphical triangle coordinates, in 3D.

import java.lang.Math;

class Triangle3D {
  double[] xCords = new double[3];
  double[] yCords = new double[3];
  double[] zCords = new double[3];
  Vector3 normal;

  public Triangle3D(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double xn, double yn, double zn) {
    xCords[0] = x1;
    xCords[1] = x2;
    xCords[2] = x3;
    yCords[0] = y1;
    yCords[1] = y2;
    yCords[2] = y3;
    zCords[0] = z1;
    zCords[1] = z2;
    zCords[2] = z3;
    normal = new Vector3(xn,yn,zn);
  }
  
  public Triangle3D(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
    xCords[0] = x1;
    xCords[1] = x2;
    xCords[2] = x3;
    yCords[0] = y1;
    yCords[1] = y2;
    yCords[2] = y3;
    zCords[0] = z1;
    zCords[1] = z2;
    zCords[2] = z3;
    normal = calculateNormal(
      new Vector3(x1,y1,z1),
      new Vector3(x2,y2,z2),
      new Vector3(x3,y3,z3)
    );
  }
  
  public Triangle3D(Vector3 one, Vector3 two, Vector3 three, Vector3 normalin) {
    xCords[0] = one.x;
    xCords[1] = two.x;
    xCords[2] = three.x;
    yCords[0] = one.y;
    yCords[1] = two.y;
    yCords[2] = three.y;
    zCords[0] = one.z;
    zCords[1] = two.z;
    zCords[2] = three.z;
    normal = normalin;
  }

  public Triangle3D(Vector3 one, Vector3 two, Vector3 three) {
    xCords[0] = one.x;
    xCords[1] = two.x;
    xCords[2] = three.x;
    yCords[0] = one.y;
    yCords[1] = two.y;
    yCords[2] = three.y;
    zCords[0] = one.z;
    zCords[1] = two.z;
    zCords[2] = three.z;
    normal = calculateNormal(one,two,three);
  }

  public Vector3 calculateNormal(Vector3 one, Vector3 two, Vector3 three) {
    Vector3 line1 = new Vector3(
      two.x - one.x,
      two.y - one.y,
      two.z - one.z
    );
    Vector3 line2 = new Vector3(
      three.x - one.x,
      three.y - one.y,
      three.z - one.z
    );
    Vector3 wnormal = new Vector3(
      (line1.y * line2.z) - (line1.z * line2.y),
      (line1.z * line2.x) - (line1.x * line2.z),
      (line1.x * line2.y) - (line1.y * line2.x)
    );
    double normalroot = Math.sqrt((wnormal.x * wnormal.x) + (wnormal.y * wnormal.y) + (wnormal.z * wnormal.z));
    wnormal.x /= normalroot;
    wnormal.y /= normalroot;
    wnormal.z /= normalroot;
    return wnormal;
  }
}
