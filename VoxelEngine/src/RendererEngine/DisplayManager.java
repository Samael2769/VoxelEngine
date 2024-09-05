package RendererEngine;

import SamVoxel.MainGameLoop;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class DisplayManager {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS_CAP = 120;

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("SamVoxel");
            Display.setFullscreen(true);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        Mouse.setGrabbed(true);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();

        while(Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                    Mouse.setGrabbed(!Mouse.isGrabbed());
                }
            }
        }
    }

    public static void closeDisplay() {
        MainGameLoop.loader1.cleanUp();
        MainGameLoop.shader1.cleanUp();
        Display.destroy();
        System.exit(0);
    }

}
