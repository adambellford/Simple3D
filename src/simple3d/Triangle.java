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
    public Vec3 v0, v1, v2;
    public Vec3 normal;
    
    public Triangle(Vec3 v0, Vec3 v1, Vec3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        computeNormal();
    }
    
    public Triangle() {
        this(new Vec3(), new Vec3(), new Vec3());
    }
    
    public void computeNormal() {
        Vec3 edge1 = v1.copy();
        edge1.sub(v0);
        Vec3 edge2 = v2.copy();
        edge2.sub(v0);
        normal = Vec3.cross(edge1, edge2);
        normal.normalize();
    }
    
    public String toString() {
        return "Triangle(" + "\n\t" + this.v0.toString()
                + "\n\t" + this.v1.toString() + "\n\t" + this.v2.toString() + "\n)";
    }
}
