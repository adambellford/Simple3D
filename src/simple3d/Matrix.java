package simple3d;

/**
 *
 * @author Adam Bellford
 */
public final class Matrix {
    public float m00, m01, m02, m03,
                       m10, m11, m12, m13,
                       m20, m21, m22, m23,
                       m30, m31, m32, m33;
    
    public Matrix() {
        m00 = 1f; m01 = 0f; m02 = 0f; m03 = 0f;
        m10 = 0f; m11 = 1f; m12 = 0f; m13 = 0f;
        m20 = 0f; m21 = 0f; m22 = 1f; m23 = 0f;
        m30 = 0f; m31 = 0f; m32 = 0f; m33 = 1f;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("Matrix(");
        sb.append("\n\t[").append(m00).append(", ").append(m01).append(", ")
                          .append(m02).append(", ").append(m03).append("]");
        sb.append("\n\t[").append(m10).append(", ").append(m11).append(", ")
                          .append(m12).append(", ").append(m13).append("]");
        sb.append("\n\t[").append(m20).append(", ").append(m21).append(", ")
                          .append(m22).append(", ").append(m23).append("]");
        sb.append("\n\t[").append(m30).append(", ").append(m31).append(", ")
                          .append(m32).append(", ").append(m33).append("]\n)");
        return sb.toString();
    }
}
