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
    
    public Vec3(float[] a) {
        if (a.length == 3) {
            this.x = a[0];
            this.y = a[1];
            this.z = a[2];
        } else {
            this.x = this.y = this.z = 0f;
        }
    }
    
    public Vec3(Vec3 v) {
        this.x = v.x; this.y = v.y; this.z = v.z;
    }
    
    
    
    public static final Vec3 ZERO = new Vec3();
    public static final Vec3 ONE = new Vec3(1f, 1f, 1f);
    
    /*
    public static final Vec3 BACK = new Vec3(0f, 0f, -1f);
    public static final Vec3 FORWARD = new Vec3(0f, 0f, 1f);
    
    public static final Vec3 DOWN = new Vec3(0f, -1f, 0f);
    public static final Vec3 UP = new Vec3(0f, 1f, 0f);
    
    public static final Vec3 LEFT = new Vec3(-1f, 0f, 0f);
    public static final Vec3 RIGHT = new Vec3(1f, 0f, 0f);
    */
    
    public static final Vec3 X_AXIS = new Vec3(1f, 0f, 0f);
    public static final Vec3 Y_AXIS = new Vec3(0f, 1f, 0f);
    public static final Vec3 Z_AXIS = new Vec3(0f, 0f, 1f);
    
    /*
    public static final Vec3 NEGATIVE_INFINITY = new Vec3(
            Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY
    );
    public static final Vec3 POSITIVE_INFINITY = new Vec3(
            Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY,
            Float.POSITIVE_INFINITY
    );
    */
    
    /*
     * Инплейс методы
     */
    
    public void set(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    
    public void set(float[] a) {
        if (a.length == 3) {
            this.x = a[0]; this.y = a[1]; this.z = a[2];
        }
    }
    
    public void set(Vec3 v) {
        this.x = v.x; this.y = v.y; this.z = v.z;
    }
    
    public void setZero() {
        this.x = this.y = this.z = 0f;
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
    
    // Деление на скаляр
    public void div(float s) {
        if (s != 0f) {
            this.x /= s;
            this.y /= s;
            this.z /= s;
        }
    }
       
    // Поэлементное умножение (Хадамар)
    public void mul(Vec3 v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
    }
    
    // Поэлементное деление
    public void div(Vec3 v) {
        if (v.x != 0f)
            this.x /= v.x;
        if (v.y != 0f)
            this.y /= v.y;
        if (v.z != 0f)
            this.z /= v.z;
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
    
    public void minComponent(Vec3 v) {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        this.z = Math.min(this.z, v.z);
    }
    
    public void maxComponent(Vec3 v) {
        this.x = Math.max(this.x, v.x);
        this.y = Math.max(this.y, v.y);
        this.z = Math.max(this.z, v.z);
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
    
    public boolean isZero(float epsilon) {
        return Math.abs(this.x) < epsilon &&
               Math.abs(this.y) < epsilon &&
               Math.abs(this.z) < epsilon;
    }
    
    public boolean epsilonEquals(Vec3 v, float epsilon) {
        return Math.abs(this.x - v.x) < epsilon &&
               Math.abs(this.y - v.y) < epsilon &&
               Math.abs(this.z - v.z) < epsilon;
    }
    
    public boolean isFinite() {
        return !Float.isNaN(this.x) && !Float.isInfinite(this.x) &&
           !Float.isNaN(this.y) && !Float.isInfinite(this.y) &&
           !Float.isNaN(this.z) && !Float.isInfinite(this.z);
    }
    
    
    // Возвращает вектор
    /*
    public Vec3 addedBy(Vec3 v) {
        return new Vec3(
            this.x + v.x, this.y + v.y, this.z + v.z
        );
    }
    
    public Vec3 subbedBy(Vec3 v) {
        return new Vec3(
            this.x - v.x, this.y - v.y, this.z - v.z
        );
    }
    
    public Vec3 scaledBy(float s) {
        return new Vec3(
            this.x * s, this.y * s, this.z * s
        );
    }
    
    public Vec3 scaledAddedBy(Vec3 v, float s) {
        return new Vec3(
            this.x + v.x * s,
            this.y + v.y * s,
            this.z + v.z * s
        );
    }
    
    public Vec3 dividedBy(float s) {
        if (s != 0f) {
            return new Vec3(
                this.x / s,
                this.y / s,
                this.z / s
            );
        }
        return new Vec3();
    }

    public Vec3 multipliedBy(Vec3 v) {
        return new Vec3(
            this.x * v.x,
            this.y * v.y,
            this.z * v.z
        );
    }
    
    public Vec3 dividedBy(Vec3 v) {
        float dx, dy, dz;
        dx = dy = dz = 0f;
        if (v.x != 0f)
            dx = this.x / v.x;
        if (v.y != 0f)
            dy = this.y / v.y;
        if (v.z != 0f)
            dz = this.z / v.z;
        return new Vec3(dx, dy, dz);
    }
    
    public Vec3 crossedBy(Vec3 v) {
        float ax = this.x; float ay = this.y; float az = this.z;
        float bx = ay * v.z - az * v.y;
        float by = az * v.x - ax * v.z;
        float bz = ax * v.y - ay * v.x;
        return new Vec3(bx, by, bz);
    }
    
    public Vec3 normalized() {
        float len = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if(len != 0f) {
            float inv = 1f / len;
            return new Vec3(
                this.x * inv, this.y * inv, this.z * inv
            );
        }
        return this.copy();
    }
    
    public Vec3 negated() {
        return new Vec3(
            -this.x, -this.y, -this.z
        );
    }
    
    public Vec3 absed() {
        return new Vec3(
            Math.abs(this.x),
            Math.abs(this.y),
            Math.abs(this.z)
        );
    }    
    
    public Vec3 lerpedBy(Vec3 v, float t) {
        float dx = this.x + (v.x - this.x) * t;
        float dy = this.y + (v.y - this.y) * t;
        float dz = this.z + (v.z - this.z) * t;
        return new Vec3(dx, dy, dz);
    }    
    */
    
    public Vec3 minLength(Vec3 v) {
        return this.lengthSq() < v.lengthSq() ? this.copy() : v.copy();
    }
    
    public Vec3 maxLength(Vec3 v) {
        return this.lengthSq() > v.lengthSq() ? this.copy() : v.copy();
    }
    
    public Vec3 projection(Vec3 onto) {
        if(onto.lengthSq() != 0f) {
            Vec3 ontoNorm = onto.copy();
            ontoNorm.normalize();
            float amount = this.dot(ontoNorm);
            ontoNorm.scale(amount);
            return ontoNorm;
        }
        return new Vec3();
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
        return new Vec3();
    }
    
    public Vec3 copy() {
        return new Vec3(this.x, this.y, this.z);
    }

    
    
    /*
    * Статические методы
    */
    
    public static void setZero(Vec3 v) {
        v.x = v.y = v.z = 0f;
    }
    
    public static Vec3 direction(float x, float y, float z) {
        Vec3 v = new Vec3(x, y, z);
        v.normalize();
        return v;
    }
    
    public static Vec3 direction(float[] a) {
        if (a.length == 3) {
            Vec3 v = new Vec3(a[0], a[1], a[2]);
            v.normalize();
            return v;
        }
        return new Vec3();
    }
    
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
    
    public static Vec3 div(Vec3 v, float s) {
        if (s != 0) {
            return new Vec3(
                v.x / s, v.y / s, v.z / s
            );
        }
        return new Vec3();
    }
    
    public static Vec3 mul(Vec3 a, Vec3 b) {
        return new Vec3(a.x * b.x, a.y * b.y, a.z * b.z);
    }
    
    public static Vec3 div(Vec3 a, Vec3 b) {
        float x = b.x != 0f ? a.x / b.x : a.x;
        float y = b.y != 0f ? a.y / b.y : a.y;
        float z = b.z != 0f ? a.z / b.z : a.z;
        return new Vec3(x, y, z);
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
        return new Vec3();
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
    
    public static Vec3 minComponent(Vec3 a, Vec3 b) {
        return new Vec3(
            Math.min(a.x, b.x),
            Math.min(a.y, b.y),
            Math.min(a.z, b.z)
        );
    }
    
    public static Vec3 maxComponent(Vec3 a, Vec3 b) {
        return new Vec3(
            Math.max(a.x, b.x),
            Math.max(a.y, b.y),
            Math.max(a.z, b.z)
        );
    }
    
    public static Vec3 minLength(Vec3 a, Vec3 b) {
        return a.lengthSq() < b.lengthSq() ? a.copy() : b.copy();
    }
    
    public static Vec3 maxLength(Vec3 a, Vec3 b) {
        return a.lengthSq() > b.lengthSq() ? a.copy() : b.copy();
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
    
    public static boolean isZero(Vec3 v, float epsilon) {
        return Math.abs(v.x) < epsilon &&
               Math.abs(v.y) < epsilon &&
               Math.abs(v.z) < epsilon;
    }
    
    public static boolean epsilonEquals(Vec3 a, Vec3 b, float epsilon) {
        return Math.abs(a.x - b.x) < epsilon &&
               Math.abs(a.y - b.y) < epsilon &&
               Math.abs(a.z - b.z) < epsilon;
    }
    
    public static boolean isFinite(Vec3 v) {
        return !Float.isNaN(v.x) && !Float.isInfinite(v.x) &&
           !Float.isNaN(v.y) && !Float.isInfinite(v.y) &&
           !Float.isNaN(v.z) && !Float.isInfinite(v.z);
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