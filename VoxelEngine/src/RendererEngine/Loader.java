package RendererEngine;

import Models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    static List<Integer> vaos = new ArrayList<Integer>();
    static List<Integer> vbos = new ArrayList<Integer>();
    static List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] vertices, int[] indices, float[] uv) {
        int vaoID = createVAO();
        storeDataInAttributeList(vertices, 0, 3);
        storeDataInAttributeList(uv, 1, 2);
        bindIndicesBuffer(indices);
        GL30.glBindVertexArray(0);

        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVAO(float[] vertices, float[] uv) {
        int vaoID = createVAO();
        storeDataInAttributeList(vertices, 0, 3);
        storeDataInAttributeList(uv, 1, 2);
        GL30.glBindVertexArray(0);

        return new RawModel(vaoID, vertices.length);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    public int loadTexture(String filename) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("./resources/res/" + filename + ".PNG")));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -4);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private void storeDataInAttributeList(float[] data, int attributeNumber, int dimensions) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dimensions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public void cleanUp() {
        for (int vao: vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo: vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture: textures) {
            GL11.glDeleteTextures(texture);
        }
    }

}
