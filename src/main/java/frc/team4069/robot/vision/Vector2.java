package frc.team4069.robot.vision;

/*
 * Trivial simple vector 2d class
 */
public class Vector2 {

    public double x, y;

    public Vector2(double xp, double yp) {
        x = xp;
        y = yp;
    }

    public void add(Vector2 v2) {
        x += v2.x;
        y += v2.y;
    }

    public void sub(Vector2 v2) {
        x -= v2.x;
        y -= v2.y;
    }

    // Dot(v1,v2)=(dx1*dx2)+(dy1*dy2)
    public double dot(Vector2 v2) {
        return (x * v2.x) + (y * v2.y);
    }

    public Vector2 clone() {
        Vector2 v = new Vector2(x, y);
        return v;
    }

    public void scale(double s) {
        x *= s;
        y *= s;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
    }

    public double lengthfast() {
        return x * x + y * y;
    }
} // class Vector2
