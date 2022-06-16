// Travis Waterman
// May 27th, 2022 - June 1st, 2022
// MeshInstantiator.java
// Reads Mesh data from file, sends to 3d renderer

// TODO: allow normals to be imported from stl files


import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

class MeshInstantiator {
  String filepath = "meshes";
  ThreeDEngine tde;
  
  public MeshInstantiator(ThreeDEngine tdein, String fpin) {
    filepath = fpin;
    tde = tdein;
  }

  public void importMesh(String meshname, Vector3 location, Vector2 rotation) {
    File meshFile = new File(filepath + "/" + meshname);
    Scanner meshIn;
    try {
      meshIn = new Scanner(meshFile);
    } catch (FileNotFoundException fnfe) {
      System.out.println("Mesh not found");
      return;
    }
    if (meshname.endsWith(".stl")) {
      iMSTL(meshIn, location, rotation);
    } else if (meshname.endsWith(".txt")) {
      iMTXT(meshIn, location, rotation);
    } else {
      System.out.println("Filetype not recognized");
      meshIn.close();
    }
    
  }

  private void iMSTL(Scanner input, Vector3 offset, Vector2 rotation) {
    byte count = 0; // counting coordinates in triangle
    double[] triArr = new double[9];
    while(input.hasNextLine()) {
      String inLine = input.nextLine();
      inLine = inLine.trim();
      if (!(inLine.startsWith("vertex"))) {
        continue;
      }
      String[] inLineArr = inLine.split(" ");
      for (int x = 0; x < inLineArr.length; x++) {
        try {
          triArr[count] = Double.parseDouble(inLineArr[x]);
        } catch (NumberFormatException nfe) {
          continue;
        } catch (ArrayIndexOutOfBoundsException obe) {
          break;
        }
        count++;
        if (count > 8) {
          Triangle3D triangle = new Triangle3D(
            triArr[0] + offset.x,
            triArr[1] + offset.y,
            triArr[2] + offset.z,
            triArr[3] + offset.x,
            triArr[4] + offset.y,
            triArr[5] + offset.z,
            triArr[6] + offset.x,
            triArr[7] + offset.y,
            triArr[8] + offset.z
          );
          triangle = rotateTriangle(triangle,rotation);
          tde.addTriangle(triangle);
          count = 0;
          triArr = new double[9];
        }
      }
    }
    input.close();
  }

  private void iMTXT(Scanner input, Vector3 offset, Vector2 rotation) {
    while(input.hasNextLine()) {
      String triIn = input.nextLine();
      String[] triSArr = triIn.split(",");
      double[] triArr = new double[triSArr.length];
      for (int x = 0; x < triSArr.length; x++) {
        triArr[x] = Double.parseDouble(triSArr[x]);
      }
      Triangle3D triangle = new Triangle3D(
        triArr[0] + offset.x,
        triArr[1] + offset.y,
        triArr[2] + offset.z,
        triArr[3] + offset.x,
        triArr[4] + offset.y,
        triArr[5] + offset.z,
        triArr[6] + offset.x,
        triArr[7] + offset.y,
        triArr[8] + offset.z
      );
      triangle = rotateTriangle(triangle,rotation);
      tde.addTriangle(triangle);
    }
    input.close();
  }

  private Triangle3D rotateTriangle(Triangle3D triangle, Vector2 rotation) {

    if ((rotation.x == 0) && (rotation.y == 0)) { // saving a lot of computing
      return triangle;
    }
    float[][] rotationMatrixX = new float[4][4];
    float[][] rotationMatrixZ = new float[4][4];
    
    rotationMatrixX[0][0] = 1;
    rotationMatrixX[1][1] = (float)(Math.cos(rotation.x));
    rotationMatrixX[1][2] = (float)(Math.sin(rotation.x));
    rotationMatrixX[2][1] = (float)(-(Math.sin(rotation.x)));
    rotationMatrixX[2][2] = (float)(Math.cos(rotation.x));
    rotationMatrixX[3][3] = 1;

    rotationMatrixZ[0][0] = (float)(Math.cos(rotation.y));
    rotationMatrixZ[0][1] = (float)(Math.sin(rotation.y));
    rotationMatrixZ[1][0] = (float)(-(Math.sin(rotation.y)));
    rotationMatrixZ[1][1] = (float)(Math.cos(rotation.y));
    rotationMatrixZ[2][2] = 1;
    rotationMatrixZ[3][3] = 1;

    for (int x = 0; x < 3; x++) {
      Vector3 out = multiplyMatrixVector(
        new Vector3(
          triangle.xCords[x],
          triangle.yCords[x],
          triangle.zCords[x]
        ),
        rotationMatrixX
      );
      out = multiplyMatrixVector(out, rotationMatrixZ);
      triangle.xCords[x] = out.x;
      triangle.yCords[x] = out.y;
      triangle.zCords[x] = out.z;
    }
    return triangle;
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
