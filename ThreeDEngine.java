// Travis Waterman
// April 12th, 2022 - June 9th, 2022
// ThreeDEngine.java
// An intepreter to translate 3d coorindates to 2d screenspace.

import java.util.ArrayList;
import java.lang.Math;

class ThreeDEngine {
  ArrayList<Triangle3D> renderCache = new ArrayList<Triangle3D>();
  Vector3 cameraPosition = new Vector3(0,0,0);
  Vector2 cameraAngle = new Vector2(0,0);
  double cameraFOV = 90.0f; // FOV in degrees
  Vector2 cameraAspect; // x = height, y = width
  float[][] projectionMatrix = new float[4][4];
  float[][] rotationMatrixX = new float[4][4];
  float[][] rotationMatrixZ = new float[4][4];
  GraphicsWindow gw;
  DisplayShell ds;

  public ThreeDEngine(GraphicsWindow gwin, DisplayShell dsin) { // tying 2d engine to 3d engine on init
    gw = gwin;
    ds = dsin;
    cameraAspect = new Vector2(
      ds.jf.getSize().height,
      ds.jf.getSize().width
    );
    float zNear = 0.1f;
    float zFar = 100000.0f;
    float fovRad = (float)(1.0f / Math.tan(cameraFOV * 0.5f / 180f * Math.PI));
    projectionMatrix[0][0] = (float)((cameraAspect.x / cameraAspect.y) * fovRad);
    projectionMatrix[1][1] = fovRad;
    projectionMatrix[2][2] = zFar / (zFar - zNear);
    projectionMatrix[3][2] = (-zFar * zNear) / (zFar - zNear);
    projectionMatrix[2][3] = 1.0f;
    projectionMatrix[3][3] = 0.0f;

  }

  public void setPosition(Vector3 position) { // inbound request to redefine the camera's coordinates
    cameraPosition = position;
  }

  public void setAngle(Vector2 angle) { // inbound request to redefine the camera's angle of view
    cameraAngle = angle;
  }

  public void renderTick() { // an inbound request to re-render the 2d scene
    refill2DCache();
    gw.frameTick();
  }


  public void addTriangle(Triangle3D triangle) { // inbound request to add a new triangle to the render cache
    renderCache.add(triangle);
  }

  public void clearCache() { // inbound request to clear the render cache
    renderCache.clear();
  }
  
  public void refill2DCache() { // rerenders everything in the render cache, sends to 2d cache.
    gw.clearStorage();

    rotationMatrixX[0][0] = 1;
    rotationMatrixX[1][1] = (float)(Math.cos(cameraAngle.x));
    rotationMatrixX[1][2] = (float)(Math.sin(cameraAngle.x));
    rotationMatrixX[2][1] = (float)(-(Math.sin(cameraAngle.x)));
    rotationMatrixX[2][2] = (float)(Math.cos(cameraAngle.x));
    rotationMatrixX[3][3] = 1;

    rotationMatrixZ[0][0] = (float)(Math.cos(cameraAngle.y));
    rotationMatrixZ[0][1] = (float)(Math.sin(cameraAngle.y));
    rotationMatrixZ[1][0] = (float)(-(Math.sin(cameraAngle.y)));
    rotationMatrixZ[1][1] = (float)(Math.cos(cameraAngle.y));
    rotationMatrixZ[2][2] = 1;
    rotationMatrixZ[3][3] = 1;
    
    for (int x = 0; x < renderCache.size(); x++) {
      Vector3[] triangleUNC = new Vector3[3];
      Triangle3D triangle3d = renderCache.get(x);

      
      for (int i = 0; i < triangle3d.xCords.length; i++) { // conversion suff here
        Vector3 pointVector = new Vector3(
          triangle3d.xCords[i],
          triangle3d.yCords[i],
          triangle3d.zCords[i]
        );
        pointVector = multiplyMatrixVector(pointVector, rotationMatrixZ);
        pointVector = multiplyMatrixVector(pointVector, rotationMatrixX);
        
        pointVector = new Vector3(
          cameraPosition.x - pointVector.x,
          cameraPosition.y - pointVector.y,
          cameraPosition.z - pointVector.z
        );
        
        triangleUNC[i] = pointVector;
        
      }
      
      Triangle3D normal = new Triangle3D(triangleUNC[0], triangleUNC[1], triangleUNC[2]);
      
       if (
        (normal.normal.x * (triangleUNC[0].x)) + 
        (normal.normal.y * (triangleUNC[0].y)) +
        (normal.normal.z * (triangleUNC[0].z)) <= 0
      ) {
        //break;
      }

      // normalize point vector, store magnitude for later
      Vector2[] projectVectors = new Vector2[3];
      float distance = 0;
      for (int i = 0; i < 3; i++) {
        Vector3 translated = (multiplyMatrixVector(triangleUNC[i], projectionMatrix));
        projectVectors[i] = new Vector2(translated.x,translated.y);
        projectVectors[i].x++; // shifting from (-1 to 1) to (0 to 2)
        projectVectors[i].y++;
        distance = (float)(translated.z);

      projectVectors[i].x *= 0.5 * cameraAspect.x;
      projectVectors[i].y *= 0.5 * cameraAspect.y;
      }
      

      try {
        gw.addTriangle(new Triangle2D(projectVectors[0], projectVectors[1], projectVectors[2], distance));
      } catch(NullPointerException temp) {}
    }
  }

  public Vector3 multiplyMatrixVector(Vector3 in, float[][] matrix) {
    float w = (float)(in.x * matrix[0][3] + in.y * matrix[1][3] + in.z * matrix[2][3] + matrix[3][3]);
    Vector3 outvector = new Vector3(
      in.x * matrix[0][0] + in.y * matrix[1][0] + in.z * matrix[2][0] + matrix[3][0],
      in.x * matrix[0][1] + in.y * matrix[1][1] + in.z * matrix[2][1] + matrix[3][1],
      in.x * matrix[0][2] + in.y * matrix[1][2] + in.z * matrix[2][2] + matrix[3][2]
    );
    if (w != 0.0f) {
      outvector.x /= w;
      outvector.y /= w;
      outvector.z /= w;
    }
    return outvector;
  }
}
