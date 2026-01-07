/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simple3d;

/**
 *
 * @author Adam Bellford
 */
public class Matrix4x4 {
    public final float m[][] = new float[4][4];
    
    public String toString() {
        String output = "Matrix:";
        for(int i = 0; i < m.length; i++) {
            output += "\n\t[";
            for(int j = 0; j < m[i].length; j++) {
                output += m[i][j] + " ";
            }
            output += "]";
        }
        return output;
    }
}
