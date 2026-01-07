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
public class Triangle {
    public static final int VERTEX_COUNT = 3;
    public final Vec3[] p;
    
    public Triangle() {
        p = new Vec3[VERTEX_COUNT];
        
        p[0] = new Vec3();
        p[1] = new Vec3();
        p[2] = new Vec3();
    }
    public Triangle(Vec3 a, Vec3 b, Vec3 c) {
        this();
        p[0] = a;
        p[1] = b;
        p[2] = c;
    }
    
    public String toString() {
        return "Triangle(" + "\n\t" + this.p[0].toString()
                + "\n\t" + this.p[1].toString() + "\n\t" + this.p[2].toString() + "\n)";
    }
}
