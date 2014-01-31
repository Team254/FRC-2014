package com.team254.lib.util;

/**
 * Matrix implementation. All fields are doubles.
 *
 * @author tom@team254.com (Tom Bottiglieri)
 * @author akalb
 */
public class Matrix {
  private double data[];
  private int width;
  private int height;

  public Matrix (int nRows, int nCols){
    int i;
    width = nCols;
    height = nRows;
    data = new double[width * height];
    for(i = 0; i<nRows*nCols; i++) {
      data[i] = 0.0;
    }
  }

  public double[] getData() {
    return data;
  }

  public double get(int y,int x){
    if (x >= width || y >= height){
      throw new ArrayIndexOutOfBoundsException("Bad index");
    }
    return data[x + (width * y)];
  }

  public double get(int i) {
    if (i >= width * height){
      throw new ArrayIndexOutOfBoundsException("Bad index");
    }
    return data[i];
  }

  public void set(int y, int x, double val){
    if (x >= width || y >= height){
      throw new ArrayIndexOutOfBoundsException("Bad index");
    }
    data[x + (width * y)] = val;
  }

  public void set(int i, double val){
    if (i >= width * height){
      throw new ArrayIndexOutOfBoundsException("Bad index");
    }
    data[i] = val;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }

  boolean sameSize(Matrix m) {
    return (getWidth() == m.getWidth()) && (getHeight() == m.getHeight());
  }

  public static Matrix subtract(Matrix mat1,Matrix mat2) {
    if (!mat1.sameSize(mat2)) {
      throw new IllegalArgumentException("Matrices not same size");
    }
    Matrix result = new Matrix(mat2.getHeight(),mat1.getWidth());
    int comp = mat1.getWidth() * mat2.getHeight();
    int i;
    for(i = 0; i < comp; i++){
      result.set(i,mat1.data[i] - mat2.data[i]);
    }
    return result;
  }

  public static Matrix add(Matrix mat1,Matrix mat2) {
    if (!mat1.sameSize(mat2)) {
      throw new IllegalArgumentException("Matrices not same size");
    }
    Matrix result = new Matrix(mat1.getHeight(), mat1.getWidth());
    int comp = mat1.getWidth() * mat1.getHeight();
    int i;
    for(i = 0; i < comp; i++){
      result.set(i, mat1.data[i] + mat2.data[i]);
    }
    return result;
  }

  public static Matrix multiply(Matrix mat1, Matrix mat2) {
    if (mat1.getWidth() != mat2.getHeight()) {
      throw new IllegalArgumentException("Matrix A's width not equal to Matix B's height");
    }

    int destHeight = mat1.getHeight();
    int destWidth = mat2.getWidth();

    Matrix result = new Matrix(destHeight, destWidth);

    int pMax = mat1.getWidth();
    int width1 = mat1.getWidth();
    int width2 = mat2.getWidth();
    for(int i = 0; i < destWidth; i++){
      for(int j = 0; j < destHeight;j++){
        double tmp = 0.0;
        for(int p = 0; p < pMax; p++){
          tmp += mat2.data[i + width2 * p] * mat1.data[p + width1 * j];
        }
        // mat1->row i * mat2->col
        result.data[i + destWidth * j] = tmp;
      }
    }
    return result;
  }


  public void flash(double [] d){
    for (int i = 0; i < d.length; i++) {
      data[i] = d[i];
    }
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        sb.append(get(i,j));
        sb.append('\t');
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  public boolean equals(Object o) {
    if ( this == o ) return true;
    if ( !(o instanceof Matrix) ) return false;
    Matrix m = (Matrix) o;

    if (!sameSize(m))
      return false;

    for (int i = 0; i < getHeight(); ++i)
      for (int j = 0; j < getWidth(); ++j)
        if (get(i,j) != m.get(i,j))
          return false;

    return true;

  }
}
