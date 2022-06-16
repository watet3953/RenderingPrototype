// Travis Waterman
// April 8th, 2022 - May 30th, 2022
// main.java
// running the stuff

// needs culling based on FOV and possibly future layers;
// Render seems to only fail in cases where the point is perpendicular or behind the camera's vector

import java.lang.Math;

class Main {
  public static void main(String[] args) {
    DisplayShell ds = new DisplayShell();
    ThreeDEngine td = new ThreeDEngine(ds.gw,ds);
    MeshInstantiator mi = new MeshInstantiator(td,"meshes");
    td.setPosition(new Vector3(0,0,100));
    mi.importMesh("bottle.stl", new Vector3(0,0,0), new Vector2(0,0));
/*    for (int x = 0; x < 200; x++) {
      mi.importMesh("sphere.stl",new Vector3(
          (Math.random() - 0.5) * 100000,
          (Math.random() - 0.5) * 100000,
          (Math.random() - 0.5) * 100000
        ),new Vector2(
          Math.random() * 2,
          Math.random() * 2
        )
      );
    }
*/   
    Vector2 pos = new Vector2(0,0);
    td.setAngle(pos);
    td.renderTick();
    long x = System.currentTimeMillis();
    while(true) { // rerenders on a set frame time, likely not most efficient setup
      if (System.currentTimeMillis() - x > 330) {
        pos.x += 0.05;
        pos.y += 0.1;
        td.setAngle(pos);
        td.renderTick();
        x = System.currentTimeMillis();
      }
    }
  }
}
