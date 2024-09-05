package Chunks;

import Entities.Entity;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import Cube.Block;

public class Chunk {

    public List<Block> blocks;
    public Vector3f origin;

    public Chunk(List<Block> blocks, Vector3f origin) {
        this.blocks = blocks;
        this.origin = origin;

    }
}
