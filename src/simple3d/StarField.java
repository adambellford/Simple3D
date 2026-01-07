/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simple3d;

import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Adam Bellford
 */
public class StarField extends GameCanvas implements Runnable {
    
    private static final double FPS = 60;
    
    private final int width = getWidth();
    private final int height = getHeight();
    
    private static Mesh meshCube = new Mesh();
    private static Matrix4x4 matProj = new Matrix4x4();
    
    private static Matrix4x4 matRotZ, matRotX;
    private float fTheta = 0f;
    
    // Projection Matrix
    private final float fNear = 0.1f;
    private final float fFar = 1000f;
    private final float fFov = 90f;
    private final float fAspectRatio = (float)height / (float)width;
    private final float fFovRad = 1f / (float)Math.tan(fFov * 0.5f / 180.0f * Math.PI);
    
    
    private final Graphics graphics;
    private volatile Thread thread;
    
    private static final int BACKGROUND = 0x00FFFFFF;
    private static final int FOREGROUND = 0x00000000;
    
    
    public Vec3 MultiplyMatrixVector(Matrix4x4 mat, Vec3 vecIn) {
        Vec3 vecOut = new Vec3();
        
        vecOut.x = vecIn.x * mat.m[0][0] + vecIn.y * mat.m[1][0] + vecIn.z * mat.m[2][0] + mat.m[3][0];
        vecOut.y = vecIn.x * mat.m[0][1] + vecIn.y * mat.m[1][1] + vecIn.z * mat.m[2][1] + mat.m[3][1];
        vecOut.z = vecIn.x * mat.m[0][2] + vecIn.y * mat.m[1][2] + vecIn.z * mat.m[2][2] + mat.m[3][2];
        float w = vecIn.x * mat.m[0][3] + vecIn.y * mat.m[1][3] + vecIn.z * mat.m[2][3] + mat.m[3][3];
        
        if(w != 0f) {
            vecOut.x /= w;
            vecOut.y /= w;
            vecOut.z /= w;
            
            return vecOut;
        }
        
        return vecOut;
    }
    
    public StarField() {
        super(true);
        this.setFullScreenMode(true);
        graphics = getGraphics();
        
        // Initializing meshCube
        meshCube.tris = new Vector();
        // SOUTH
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 0f, 0f),
                new Vec3(0f, 1f, 0f),
                new Vec3(1f, 1f, 0f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 0f, 0f),
                new Vec3(1f, 1f, 0f),
                new Vec3(1f, 0f, 0f)
            )
        );
        // EAST
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 0f),
                new Vec3(1f, 1f, 0f),
                new Vec3(1f, 1f, 1f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 0f),
                new Vec3(1f, 1f, 1f),
                new Vec3(1f, 0f, 1f)
            )
        );
        
        // NORTH
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 1f),
                new Vec3(1f, 1f, 1f),
                new Vec3(0f, 1f, 1f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 1f),
                new Vec3(0f, 1f, 1f),
                new Vec3(0f, 0f, 1f)
            )
        );
        
        // WEST
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 0f, 1f),
                new Vec3(0f, 1f, 1f),
                new Vec3(0f, 1f, 0f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 0f, 1f),
                new Vec3(0f, 1f, 0f),
                new Vec3(0f, 0f, 0f)
            )
        );
        
        // TOP
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 1f, 0f),
                new Vec3(0f, 1f, 1f),
                new Vec3(1f, 1f, 1f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(0f, 1f, 0f),
                new Vec3(1f, 1f, 1f),
                new Vec3(1f, 1f, 0f)
            )
        );
        
        // BOTTOM
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 1f),
                new Vec3(0f, 0f, 1f),
                new Vec3(0f, 0f, 0f)
            )
        );
        meshCube.tris.addElement(
            new Triangle(
                new Vec3(1f, 0f, 1f),
                new Vec3(0f, 0f, 0f),
                new Vec3(1f, 0f, 0f)
            )
        );
        
        // Initializing Matrix
        matProj.m[0][0] = fAspectRatio * fFovRad;
        matProj.m[1][1] = fFovRad;
        matProj.m[2][2] = fFar / (fFar - fNear);
        matProj.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        matProj.m[2][3] = 1f;
        matProj.m[3][3] = 0f;
        
        matRotZ = new Matrix4x4();
        matRotX = new Matrix4x4();
    }
    
    protected void hideNotify() {
        thread = null;
    }
    
    protected void clear() {
        graphics.setColor(BACKGROUND);
        graphics.fillRect(0, 0, width, height);
    }
    
    protected void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        graphics.setColor(FOREGROUND);
        graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        graphics.drawLine((int)x2, (int)y2, (int)x3, (int)y3);
        graphics.drawLine((int)x3, (int)y3, (int)x1, (int)y1);
    }
    
    protected void drawTriangles(float fTheta) {
        Triangle projected = new Triangle();
        Triangle translated, rotatedZ = new Triangle(), rotatedZX = new Triangle();
        
        float a = (float) Math.toRadians(fTheta) * .5f;
        
        matRotZ.m[0][0] = (float)Math.cos(Math.toRadians(fTheta));
        matRotZ.m[0][1] = (float)Math.sin(Math.toRadians(fTheta));
        matRotZ.m[1][0] = -(float)Math.sin(Math.toRadians(fTheta));
        matRotZ.m[1][1] = (float)Math.cos(Math.toRadians(fTheta));
        matRotZ.m[2][2] = 1f;
        matRotZ.m[3][3] = 1f;
        
        matRotX.m[0][0] = 1f;
        matRotX.m[1][1] = (float)Math.cos(a);
        matRotX.m[1][2] = (float)Math.sin(a);
        matRotX.m[2][1] = -(float)Math.sin(a);
        matRotX.m[2][2] = (float)Math.cos(a);
        matRotX.m[3][3] = 1f;
        
        for(int i = 0; i < meshCube.tris.size(); i++) {
            Triangle current = (Triangle) meshCube.tris.elementAt(i);
            
            rotatedZ.p[0] = MultiplyMatrixVector(matRotZ, current.p[0]);
            rotatedZ.p[1] = MultiplyMatrixVector(matRotZ, current.p[1]);
            rotatedZ.p[2] = MultiplyMatrixVector(matRotZ, current.p[2]);
            
            rotatedZX.p[0] = MultiplyMatrixVector(matRotX, rotatedZ.p[0]);
            rotatedZX.p[1] = MultiplyMatrixVector(matRotX, rotatedZ.p[1]);
            rotatedZX.p[2] = MultiplyMatrixVector(matRotX, rotatedZ.p[2]);
            
            translated = rotatedZX;
            translated.p[0].z = rotatedZX.p[0].z + 3f;
            translated.p[1].z = rotatedZX.p[1].z + 3f;
            translated.p[2].z = rotatedZX.p[2].z + 3f;
            
            projected.p[0] = MultiplyMatrixVector(matProj, translated.p[0]);
            projected.p[1] = MultiplyMatrixVector(matProj, translated.p[1]);
            projected.p[2] = MultiplyMatrixVector(matProj, translated.p[2]);
            
            // Scale into view
            projected.p[0].x += 1f; projected.p[0].y += 1f;
            projected.p[1].x += 1f; projected.p[1].y += 1f;
            projected.p[2].x += 1f; projected.p[2].y += 1f;
            
            projected.p[0].x *= 0.5f * (float)width;
            projected.p[0].y *= 0.5f * (float)height;
            projected.p[1].x *= 0.5f * (float)width;
            projected.p[1].y *= 0.5f * (float)height;
            projected.p[2].x *= 0.5f * (float)width;
            projected.p[2].y *= 0.5f * (float)height;

            drawTriangle(
                projected.p[0].x, projected.p[0].y,
                projected.p[1].x, projected.p[1].y,
                projected.p[2].x, projected.p[2].y
            );
        }
    }
    double t = 0.0;
    protected void frame() {
        final double dt = 1.0 / FPS;
        t += 1.0 * dt;
        fTheta += 1f * t;
        // рисуем
        clear();
        // трансформация
        drawTriangles(fTheta);
        flushGraphics();
        try {
            Thread.sleep((int) (1000 / FPS));
        } catch(InterruptedException e) {}
    }
    
    // Game cycle
    public void run() {
        while(thread == Thread.currentThread()) {
            frame();
            flushGraphics();
        }
    }
    
    protected void showNotify() {        
        thread = new Thread(this);
        thread.start();
    }
    
}
