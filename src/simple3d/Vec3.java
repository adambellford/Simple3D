package simple3d;

/**
 *
 * @author Adam Bellford
 */
public final class Vec3 {
    public float x, y, z;
    
    public Vec3() {
        this.x = this.y = this.z = 0f;
    }
    public Vec3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    public Vec3(Vec3 v) {
        this.x = v.x; this.y = v.y; this.z = v.z;
    }
    
    public static final Vec3 ZERO = new Vec3();
    public static final Vec3 ONE = new Vec3(1f, 1f, 1f);
    public static final Vec3 X_AXIS = new Vec3(1f, 0f, 0f);
    public static final Vec3 Y_AXIS = new Vec3(0f, 1f, 0f);
    public static final Vec3 Z_AXIS = new Vec3(0f, 0f, 1f);
    
    /*
     * Инплейс методы
     */
    
    public void set(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    
    // Арифметические действия
    
    public void add(Vec3 v) {
        this.x += v.x; this.y += v.y; this.z += v.z;
    }
    
    public void sub(Vec3 v) {
        this.x -= v.x; this.y -= v.y; this.z -= v.z;
    }
    
    public void scale(float s) {
        this.x *= s; this.y *= s; this.z *= s;
    }
    
    public void scaleAdd(Vec3 v, float s) {
        this.x += v.x * s;
        this.y += v.y * s;
        this.z += v.z * s;
    }
    
    
    // Векторные операции
    
    public void cross(Vec3 v) {
        float ax = this.x; float ay = this.y; float az = this.z;
        this.x = ay * v.z - az * v.y;
        this.y = az * v.x - ax * v.z;
        this.z = ax * v.y - ay * v.x;
    }
    
    public void normalize() {
        float len = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        
        if(len != 0f) {
            float inv = 1f / len;
            this.x *= inv; this.y *= inv; this.z *= inv;
        }
    }
    
    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }
    
    public void abs() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
    }
    
    
    // Misc.
    
    public void lerp(Vec3 v, float t) {
        this.x += (v.x - this.x) * t;
        this.y += (v.y - this.y) * t;
        this.z += (v.z - this.z) * t;
    }
    
    
    // Возврат скалярных величин
    
    public float dot(Vec3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }
    
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public float lengthSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public float distance(Vec3 v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public float distanceSq(Vec3 v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public float projectionScalar(Vec3 onto) {
        float vLen = (float) Math.sqrt(onto.x * onto.x + onto.y * onto.y + onto.z * onto.z);
        if(vLen == 0f)
            return 0f;
        float dotProd = this.x * onto.x + this.y * onto.y + this.z * onto.z;
        return dotProd / vLen;
    }
    
    public float cosAngle(Vec3 v) {
        if (this.lengthSq() != 0 && v.lengthSq() != 0) {
            return (this.dot(v) / (this.length() * v.length()));
        }
        return Float.NaN;
    }
    
    
    // Возвращает вектор
    
    public Vec3 min(Vec3 v) {
        return this.lengthSq() < v.lengthSq() ? this : v;
    }
    
    public Vec3 max(Vec3 v) {
        return this.lengthSq() > v.lengthSq() ? this : v;
    }
    
    public Vec3 projection(Vec3 onto) {
        if(onto.lengthSq() != 0f) {
            Vec3 ontoNorm = onto.copy();
            ontoNorm.normalize();
            float amount = this.dot(ontoNorm);
            ontoNorm.scale(amount);
            return ontoNorm;
        }
        return Vec3.ZERO.copy();
    }
    
    public Vec3 reflect(Vec3 normal) {
        if(normal.lengthSq() != 0f) {
            Vec3 n = normal.copy();
            n.normalize();
            float dot = this.dot(n);
            n.scale(2f * dot);
            Vec3 reflected = this.copy();
            reflected.sub(n);
            return reflected;
        }
        return Vec3.ZERO.copy();
    }
    
    public Vec3 copy() {
        return new Vec3(this.x, this.y, this.z);
    }

    
    
    /*
    * Статические методы
    */
    
    // Арифметические действия
    
    public static Vec3 add(Vec3 a, Vec3 b) {
        return new Vec3(
            a.x + b.x, a.y + b.y, a.z + b.z
        );
    }
    
    public static Vec3 sub(Vec3 a, Vec3 b) {
        return new Vec3(
            a.x - b.x, a.y - b.y, a.z - b.z
        );
    }
    
    public static Vec3 scale(Vec3 v, float s) {
        return new Vec3(
            v.x * s, v.y * s, v.z * s
        );
    }
    
    public static Vec3 scaleAdd(Vec3 v, Vec3 addend, float s) {
        return new Vec3(
            v.x + addend.x * s,
            v.y + addend.y * s,
            v.z + addend.z * s
        );
    }
    
    // Векторные операции
    
    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(
            a.y * b.z - a.z * b.y,
            a.z * b.x - a.x * b.z,
            a.x * b.y - a.y * b.x
        );
    }
    
    public static Vec3 normalize(Vec3 v) {
        float len = v.length();
        if(len != 0f)
            return new Vec3(v.x / len, v.y / len, v.z / len);
        return Vec3.ZERO.copy();
    }
    
    public static Vec3 negate(Vec3 v) {
        return new Vec3(
            -v.x, -v.y, -v.z
        );
    }
    
    public static Vec3 abs(Vec3 v) {
        return new Vec3(
            Math.abs(v.x),
            Math.abs(v.y),
            Math.abs(v.z)
        );
    }
    
    public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
        return new Vec3(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        );
    }
    
    public static Vec3 projection(Vec3 v, Vec3 onto) {
        return v.projection(onto);
    }
    
    public static Vec3 reflect(Vec3 v, Vec3 normal) {
        return v.reflect(normal);
    }
    
    public static Vec3 min(Vec3 a, Vec3 b) {
        return a.lengthSq() < b.lengthSq() ? a : b;
    }
    
    public static Vec3 max(Vec3 a, Vec3 b) {
        return a.lengthSq() > b.lengthSq() ? a : b;
    }
    
    public static float dot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    public static float length(Vec3 v) {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }
    
    public static float lengthSq(Vec3 v) {
        return v.x * v.x + v.y * v.y + v.z * v.z;
    }
    
    public static float distance(Vec3 a, Vec3 b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float dz = a.z - b.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public static float distanceSq(Vec3 a, Vec3 b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public static float projectionScalar(Vec3 v, Vec3 onto) {
        float vLen = (float) Math.sqrt(onto.x * onto.x + onto.y * onto.y + onto.z * onto.z);
        if(vLen == 0f)
            return 0f;
        float dotProd = v.x * onto.x + v.y * onto.y + v.z * onto.z;
        return dotProd / vLen;
    }
    
    public static float cosAngle(Vec3 a, Vec3 b) {
        if (a.lengthSq() != 0 && b.lengthSq() != 0) {
            return (a.dot(b) / (a.length() * b.length()));
        }
        return Float.NaN;
    }
    
    
    
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Float.floatToIntBits(this.x);
        hash = 17 * hash + Float.floatToIntBits(this.y);
        hash = 17 * hash + Float.floatToIntBits(this.z);
        return hash;
    }
    
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        Vec3 other = (Vec3) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("Vec3(");
        sb.append(this.x).append(", ").append(this.y).append(", ").append(this.z).append(")");
        return sb.toString();
    }
}