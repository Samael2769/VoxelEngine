package SamVoxel;

import Chunks.Chunk;
import Chunks.ChunkMesh;
import Cube.Block;
import Entities.Camera;
import Entities.Entity;
import Models.CubeModel;
import Models.RawModel;
import Models.TexturedModel;
import RendererEngine.DisplayManager;
import RendererEngine.Loader;
import RendererEngine.MasterRenderer;
import Shaders.StaticShader;
import Textures.ModelTexture;
import Toolbox.PerlinNoiseGenerator;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainGameLoop {

    public static Loader loader1 = null;
    public static StaticShader shader1 = null;

    static List<ChunkMesh> chunks = Collections.synchronizedList(new ArrayList<ChunkMesh>());
    static Vector3f camPos = new Vector3f(0, 0, 0);
    static List<Vector3f> usedPos = new ArrayList<Vector3f>();

    static List<Entity> entities = new ArrayList<Entity>();

    static final int WORLD_SIZE = 5 * 32;

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        Loader loader = new Loader();
        loader1 = loader;
        StaticShader shader = new StaticShader();
        MasterRenderer renderer = new MasterRenderer();
        shader1 = shader;

        RawModel model = loader.loadToVAO(CubeModel.vertices, CubeModel.indices, CubeModel.uv);
        ModelTexture texture = new ModelTexture(loader.loadTexture("dirtTex"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Camera camera = new Camera(new Vector3f(0, 0, 0), 0, 0, 0);
        PerlinNoiseGenerator generator = new PerlinNoiseGenerator();


        new Thread(new Runnable() {public void run() {
            while(!Display.isCloseRequested()) {

                for (int i = (int)(camPos.x - WORLD_SIZE) / 32; i <= (camPos.x + WORLD_SIZE) / 32; i++) {
                    for (int j = (int) (camPos.z - WORLD_SIZE) / 32; j <= (camPos.z + WORLD_SIZE) / 32; j++) {
                        if (!usedPos.contains(new Vector3f(i * 32, 0 * 32, j * 32))) {

                            List<Block> blocks = new ArrayList<Block>();

                            for (int x = 0; x < 32; x++) {
                                for (int z = 0; z < 32; z++) {
                                    blocks.add(new Block(x, (int) generator.generateHeight(x + (i * 32), z + (j * 32)), z, Block.TYPE.DIRT));
                                }
                            }
                            Chunk chunk = new Chunk(blocks, new Vector3f(i * 32, 0 * 32, j * 32));
                            chunks.add(new ChunkMesh(chunk));
                            usedPos.add(new Vector3f(i * 32, 0 * 32, j * 32));
                        }
                    }
                }

            }
        }}).start();

        /*List<Block> blocks = new ArrayList<Block>();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {

                    blocks.add(new Block(x, y, z, Block.TYPE.DIRT));

                }
            }
        }

        Chunk chunk = new Chunk(blocks, new Vector3f(0, 0, 0));
        ChunkMesh mesh = new ChunkMesh(chunk);

        RawModel model123 = loader.loadToVAO(mesh.positions, mesh.uvs);
        TexturedModel texturedModel123 = new TexturedModel(model123, texture);
        Entity entity = new Entity(texturedModel123, new Vector3f(0,0,0), 0, 0, 0, 1);*/

        // Main loop
        int index = 0;
        while (!Display.isCloseRequested()) {
            camera.move();
            camPos = camera.getPosition();

            if (index < chunks.size()) {

                RawModel model123 = loader.loadToVAO(chunks.get(index).positions, chunks.get(index).uvs);
                TexturedModel texturedModel123 = new TexturedModel(model123, texture);
                Entity entity = new Entity(texturedModel123, chunks.get(index).chunk.origin, 0, 0, 0, 1);
                entities.add(entity);

                index++;
            }

            for (int i = 0; i < entities.size(); i++) {

                Vector3f origin = entities.get(i).getPosition();

                int distX = (int) (camPos.x - origin.x);
                int distZ = (int) (camPos.z - origin.z);
                distX = Math.abs(distX);
                distZ = Math.abs(distZ);

                if ((distX <= WORLD_SIZE) || (distZ <= WORLD_SIZE)) {
                        renderer.addEntity(entities.get(i));
                }

            }
            renderer.render(camera);
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }
}
