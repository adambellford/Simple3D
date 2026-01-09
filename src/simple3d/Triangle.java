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
public final class Triangle {
    public Vec3 v0, v1, v2;
    public Vec3 normal;
    
    public Triangle(Vec3 v0, Vec3 v1, Vec3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        normal = new Vec3();
        computeNormal();
    }
    
    public Triangle() {
        this(new Vec3(), new Vec3(), new Vec3());
    }
    
    public final void computeNormal() {
        float e1x = v1.x - v0.x;
        float e1y = v1.y - v0.y;
        float e1z = v1.z - v0.z;

        float e2x = v2.x - v0.x;
        float e2y = v2.y - v0.y;
        float e2z = v2.z - v0.z;

        normal.x = e1y * e2z - e1z * e2y;
        normal.y = e1z * e2x - e1x * e2z;
        normal.z = e1x * e2y - e1y * e2x;

        normal.normalize();
    }
    
    public void set(Vec3 v0, Vec3 v1, Vec3 v2) {
        this.v0.set(v0);
        this.v1.set(v1);
        this.v2.set(v2);
        computeNormal();
    }
    
    public Vec3 getCenter(Vec3 out) {
        out.set(v0);
        out.add(v1);
        out.add(v2);
        out.scale(1f / 3f);
        return out;
    }
    
    public Vec3 getCenter() {
        return new Vec3(
            (v0.x + v1.x + v2.x) * (1f/3f),
            (v0.y + v1.y + v2.y) * (1f/3f),
            (v0.z + v1.z + v2.z) * (1f/3f)
        );
    }
    
    public final void computeNormalUnsafe() {
        float e1x = v1.x - v0.x;
        float e1y = v1.y - v0.y;
        float e1z = v1.z - v0.z;

        float e2x = v2.x - v0.x;
        float e2y = v2.y - v0.y;
        float e2z = v2.z - v0.z;

        normal.x = e1y * e2z - e1z * e2y;
        normal.y = e1z * e2x - e1x * e2z;
        normal.z = e1x * e2y - e1y * e2x;
    }
    
    public boolean isFacing(Vec3 viewDir) {
        return normal.dot(viewDir) < 0f;
    }
    
    public boolean isBackFacing(Vec3 cameraDir) {
        return normal.dot(cameraDir) >= 0f;
    }
    
    public boolean isDegenerate() {
        // по бырому
        return normal.lengthSq() < 1e-12f;
    }
    
    public float area2Sq() {
        float e1x = v1.x - v0.x;
        float e1y = v1.y - v0.y;
        float e1z = v1.z - v0.z;
        
        float e2x = v2.x - v0.x;
        float e2y = v2.y - v0.y;
        float e2z = v2.z - v0.z;
        
        float cx = e1y * e2z - e1z * e2y;
        float cy = e1z * e2x - e1x * e2z;
        float cz = e1x * e2y - e1y * e2x;
        
        return cx*cx + cy*cy + cz*cz;
    }
    
    public float area2() {
        float e1x = v1.x - v0.x;
        float e1y = v1.y - v0.y;
        float e1z = v1.z - v0.z;
        
        float e2x = v2.x - v0.x;
        float e2y = v2.y - v0.y;
        float e2z = v2.z - v0.z;
        
        float cx = e1y * e2z - e1z * e2y;
        float cy = e1z * e2x - e1x * e2z;
        float cz = e1x * e2y - e1y * e2x;
        
        return (float)Math.sqrt(cx*cx + cy*cy + cz*cz);
    }
    
    public float area() {
        float e1x = v1.x - v0.x;
        float e1y = v1.y - v0.y;
        float e1z = v1.z - v0.z;
        
        float e2x = v2.x - v0.x;
        float e2y = v2.y - v0.y;
        float e2z = v2.z - v0.z;
        
        float cx = e1y * e2z - e1z * e2y;
        float cy = e1z * e2x - e1x * e2z;
        float cz = e1x * e2y - e1y * e2x;
        
        return 0.5f * (float)Math.sqrt(cx*cx + cy*cy + cz*cz);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("Triangle(");
        sb.append("\n\t").append(this.v0).append("\n\t").append(this.v1).append("\n\t").append(this.v2).append("\n)");
        return sb.toString();
    }
}