package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    float speed = 0.3f;
    float turn_speed = 0.1f;
    float moveAt = 0f;
    Vector3f position;
    float rotX, rotY, rotZ;

    public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            moveAt = -speed;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveAt = speed;
        } else {
            moveAt = 0;
        }

        rotX += -Mouse.getDY() * turn_speed;
        rotY += Mouse.getDX() * turn_speed;

        float dx = (float) -(moveAt * Math.sin(Math.toRadians(rotY)));
        float dy = (float) (moveAt * Math.sin(Math.toRadians(rotX)));
        float dz = (float) (moveAt * Math.cos(Math.toRadians(rotY)));
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }
}
