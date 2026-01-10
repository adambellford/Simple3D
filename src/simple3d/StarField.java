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
    
    // Счетчик фепасов
    long lastTime;
    int frames;
    int fps;
    
    private final int width = getWidth();
    private final int height = getHeight();
    
    private static Mesh meshCube = new Mesh();
    private static Matrix matProj = new Matrix();
    
    private static Vec3 vCamera;
    
    private static Matrix matRotZ, matRotX;
    private float fTheta = 0f;
    
    Vec3 normal, line1, line2;
    
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
    
    
    Triangle projected, translated, rotatedZ, rotatedZX;
    
    public void MultiplyMatrixVector(Matrix mat, Vec3 vecIn, Vec3 vecOut) {
        vecOut.x = vecIn.x * mat.m00 + vecIn.y * mat.m10 + vecIn.z * mat.m20 + mat.m30;
        vecOut.y = vecIn.x * mat.m01 + vecIn.y * mat.m11 + vecIn.z * mat.m21 + mat.m31;
        vecOut.z = vecIn.x * mat.m02 + vecIn.y * mat.m12 + vecIn.z * mat.m22 + mat.m32;
        
        float w = vecIn.x * mat.m03 + vecIn.y * mat.m13 + vecIn.z * mat.m23 + mat.m33;
        
        if(w != 0f) {
            vecOut.x /= w;
            vecOut.y /= w;
            vecOut.z /= w;
        }
    }
    
    public StarField() {
        super(true);
        this.setFullScreenMode(true);
        
        frames = 0;
        fps = 0;
        
        
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
        matProj.m00 = fAspectRatio * fFovRad;
        matProj.m11 = fFovRad;
        matProj.m22 = fFar / (fFar - fNear);
        matProj.m32 = (-fFar * fNear) / (fFar - fNear);
        matProj.m23 = 1f;
        matProj.m33 = 0f;
        
        matRotZ = new Matrix();
        matRotX = new Matrix();
        
        projected = new Triangle();
        translated = new Triangle();
        rotatedZ = new Triangle();
        rotatedZX = new Triangle();
        
        vCamera = new Vec3();
        
        normal = new Vec3();
        line1 = new Vec3();
        line2 = new Vec3();
        
        lastTime = System.currentTimeMillis();
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
//        graphics.fillTriangle((int)x1, (int)y1, (int)x2, (int)y2, (int)x3, (int)y3);
//        graphics.setColor(BACKGROUND);
        graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        graphics.drawLine((int)x2, (int)y2, (int)x3, (int)y3);
        graphics.drawLine((int)x3, (int)y3, (int)x1, (int)y1);
    }
    
    protected int getColor(float lum) {
        int c = (int)(0.1f + (170f * lum) * 0.85f);
        return (c << 16) | (c << 8) | c; // RGB
    }
    
    protected void drawTriangles(float fTheta) {        
        float a = (float) Math.toRadians(fTheta) * .5f;
        
        matRotZ.m00 = (float)Math.cos(Math.toRadians(fTheta));
        matRotZ.m01 = (float)Math.sin(Math.toRadians(fTheta));
        matRotZ.m10 = -(float)Math.sin(Math.toRadians(fTheta));
        matRotZ.m11 = (float)Math.cos(Math.toRadians(fTheta));
        matRotZ.m22 = 1f;
        matRotZ.m33 = 1f;
        
        matRotX.m00 = 1f;
        matRotX.m11 = (float)Math.cos(a);
        matRotX.m12 = (float)Math.sin(a);
        matRotX.m21 = -(float)Math.sin(a);
        matRotX.m22 = (float)Math.cos(a);
        matRotX.m33 = 1f;
        
        for(int i = 0; i < meshCube.tris.size(); i++) {
            Triangle current = (Triangle) meshCube.tris.elementAt(i);
            
            // Rotate in Z axis
            MultiplyMatrixVector(matRotZ, current.v0, rotatedZ.v0);
            MultiplyMatrixVector(matRotZ, current.v1, rotatedZ.v1);
            MultiplyMatrixVector(matRotZ, current.v2, rotatedZ.v2);
            
            // Rotate in X axis
            MultiplyMatrixVector(matRotX, rotatedZ.v0, rotatedZX.v0);
            MultiplyMatrixVector(matRotX, rotatedZ.v1, rotatedZX.v1);
            MultiplyMatrixVector(matRotX, rotatedZ.v2, rotatedZX.v2);
            
            // Offset into the screen
            translated.v0.set(rotatedZX.v0);
            translated.v1.set(rotatedZX.v1);
            translated.v2.set(rotatedZX.v2);
            translated.v0.z += 3f;
            translated.v1.z += 3f;
            translated.v2.z += 3f;
            
            
            line1.x = translated.v1.x - translated.v0.x;
            line1.y = translated.v1.y - translated.v0.y;
            line1.z = translated.v1.z - translated.v0.z;
            
            line2.x = translated.v2.x - translated.v0.x;
            line2.y = translated.v2.y - translated.v0.y;
            line2.z = translated.v2.z - translated.v0.z;
            
            normal.x = line1.y * line2.z - line1.z * line2.y;
            normal.y = line1.z * line2.x - line1.x * line2.z;
            normal.z = line1.x * line2.y - line1.y * line2.x;
            
            
            
            float l = (float)Math.sqrt(normal.x*normal.x + normal.y*normal.y + normal.z*normal.z);
            if (l == 0f) continue;
            normal.x /= l; normal.y /= l; normal.z /= l;
            
            //if (normal.z < 0) {
            if (normal.x * (translated.v0.x - vCamera.x) +
                normal.y * (translated.v0.y - vCamera.y) +
                normal.z * (translated.v0.z - vCamera.z) < 0f)
            {
                // Illumination
                Vec3 lightDirection = new Vec3(0f, 0f, -1f);
                l = (float)Math.sqrt(lightDirection.x*lightDirection.x +
                               lightDirection.y*lightDirection.y +
                               lightDirection.z*lightDirection.z);
                lightDirection.x /= l; lightDirection.y /= l; lightDirection.z /= l;
                
                float dp = normal.x * lightDirection.x + normal.y * lightDirection.y + normal.z * lightDirection.z;
                translated.col = getColor(dp);
                
                
                
                // Project triangles from 3D to 2D
                MultiplyMatrixVector(matProj, translated.v0, projected.v0);
                MultiplyMatrixVector(matProj, translated.v1, projected.v1);
                MultiplyMatrixVector(matProj, translated.v2, projected.v2);
                projected.col = translated.col;

                // Scale into view
                projected.v0.x += 1f; projected.v0.y += 1f;
                projected.v1.x += 1f; projected.v1.y += 1f;
                projected.v2.x += 1f; projected.v2.y += 1f;

                projected.v0.x *= 0.5f * (float)width;
                projected.v0.y *= 0.5f * (float)height;
                projected.v1.x *= 0.5f * (float)width;
                projected.v1.y *= 0.5f * (float)height;
                projected.v2.x *= 0.5f * (float)width;
                projected.v2.y *= 0.5f * (float)height;

//                drawTriangle(
//                    projected.v0.x, projected.v0.y,
//                    projected.v1.x, projected.v1.y,
//                    projected.v2.x, projected.v2.y
//                );
                graphics.setColor(projected.col);
                graphics.fillTriangle((int)projected.v0.x, (int)projected.v0.y,
                        (int)projected.v1.x, (int)projected.v1.y,
                        (int)projected.v2.x, (int)projected.v2.y);
            }
        }
    }
    
    double t = 0.0;
    
    protected void render() {
        fTheta += 1f;
        // рисуем
        clear();
        // трансформация
        drawTriangles(fTheta);
        graphics.setColor(FOREGROUND);
        graphics.drawString("FPS: " + fps, 2, 2, Graphics.TOP | Graphics.LEFT);
        
        flushGraphics();
    }
    
    // Game cycle
    public void run() {
        long frameStart;
        long frameTime;
        final long frameDelay = (long)(1000 / FPS);
        
        while(thread == Thread.currentThread()) {
            frameStart = System.currentTimeMillis();
            
            render();
            frames++;
            
            long now = System.currentTimeMillis();
            if (now - lastTime >= 1000) {
                fps = frames;
                frames = 0;
                lastTime = now;
            }
            
            frameTime = System.currentTimeMillis() - frameStart;
            if (frameTime < frameDelay) {
                try {
                    Thread.sleep(frameDelay - frameTime);
                } catch (InterruptedException e) {}
            }
        }
    }
    
    protected void showNotify() {        
        thread = new Thread(this);
        thread.start();
    }
    
}
