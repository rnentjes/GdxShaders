package nl.astraeus.shader.test.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import nl.astraeus.shader.test.util.MeshHelper;
import nl.astraeus.shader.test.util.MeshHelperSimple;
import nl.astraeus.shader.test.util.ShaderHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 7:08 PM
 */
public class Tunnel extends BaseScreen {

    private final static float DEPTH = 500;

    private List<Star> stars = new ArrayList<Star>(10000);

    private static class Star {
        public float x,y,z;

        private Star(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private long start = System.currentTimeMillis();
    private MeshHelper meshHelper;
    private MeshHelper meshHelper2;
    private MeshHelperSimple meshHelper3;
    private MeshHelperSimple meshHelper4;
    private List<MeshHelperSimple> triangles = new ArrayList<MeshHelperSimple>(1000);
    private PerspectiveCamera cam;

    Matrix4 projection = new Matrix4();
    Matrix4 view = new Matrix4();
    Matrix4 model = new Matrix4();
    Matrix4 model2 = new Matrix4();
    Matrix4 combined = new Matrix4();
    Vector3 axis = new Vector3(1, 0, 1).nor();

    int points = 1000;

    @Override
    public void init() {
        cam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.translate(0f, 0, 2f);

        float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        projection.setToProjection(1.0f, DEPTH, 60.0f, aspect);

        meshHelper = new MeshHelper("basic");
        meshHelper2 = new MeshHelper("basic2");
        meshHelper3 = new MeshHelperSimple("simple");
        meshHelper4 = new MeshHelperSimple("simple");

        // opengl uses -1 to 1 for coords and also is inverted
        meshHelper.createMesh(new float[] { -0.5f, -0.5f, 1.0f,
                                             0.5f, -0.5f, 1.0f,
                                               0f,  0.5f, 0.6f,
                                            -0.5f,  0.5f, 0.5f,
                                            -0.5f,  0.0f, 0.75f,
                                               0f,  0.2f, 0.8f},
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Generic , 1, "a_generic"));

        meshHelper2.createMesh(new float[] { 0.3f, 0.3f, 1.0f,
                                             0.3f, 0.9f, 1.0f,
                                             0.9f, 0.3f, 1.0f,
                                             0.9f, 0.3f, 1.0f,
                                             0.9f, 0.9f, 1.0f,
                                             0.3f, 0.9f, 1.0f,},
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Generic , 1, "a_generic"));

        meshHelper3.createMesh(new float[] {
                -0.3f, -0.3f, 0.0f,
                0.3f, -0.3f, 0.0f,
                0.15f, 0.3f, 0.0f,

                0.15f, 0.3f, 0.0f,
                0.3f, -0.3f, 0.0f,
                -0.15f, -0.15f, 0.15f,
        }, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        meshHelper3.getMatrix().translate(-0.5f, 0.00f, 0.0f);

        //for (int i = 0; i < 100; i++) {
        MeshHelperSimple ms = new MeshHelperSimple("simple");
        ms.createMesh(new float[] {
                -0.3f, -0.3f, 0.0f,
                0.3f, -0.3f, 0.0f,
                0.15f, 0.3f, 0.0f,
        }, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        ms.getMatrix().trn(0.31f, 0.01f, 0.0f);
        //ms.getMatrix().toNormalMatrix();

        triangles.add(ms);

        ms = new MeshHelperSimple("simple");
        ms.createMesh(new float[] {
                -0.3f, -0.3f, 0.0f,
                0.3f, -0.3f, 0.0f,
                0.15f, 0.3f, 0.0f,
        }, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        ms.getMatrix().trn(-0.2f, -0.2f, -0.0f);
        //ms.getMatrix().toNormalMatrix();

        triangles.add(ms);
        //}

        meshHelper4.createMesh(new float[] {
                -0.3f, -0.3f, 0.0f,
                0.3f, -0.3f, 0.0f,
                0.15f, 0.3f, 0.0f,
        }, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        createLineMesh();
        createPointMesh();
        createCubeMesh();
        createTestMesh();
    }

    private Mesh lineMesh;
    public void createLineMesh() {
        float [] vertices = new float[] {
                -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, 1.0f,
                 1.0f,  1.0f, 1.0f,
                -1.0f,  1.0f, -1.0f
        };
//                0.15f, 0.3f, 0.0f,};

        lineMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Generic, 1, "a_edge"));
        lineMesh.setVertices(vertices);
    }

    private Mesh pointMesh;
    public void createPointMesh() {
        float [] vertices = new float[] {
                -1.0f, -1.0f,
                1.0f, -1.0f,
                1.0f,  1.0f,
                -1.0f,  1.0f,
        };

        pointMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"));
        pointMesh.setVertices(vertices);
    }

    private Mesh cubeMesh;
    public void createCubeMesh() {
        float [] vertices = new float[] {
                //front
                -1f,  1f,  -1f,
                 1f,  1f,  -1f,
                -1f, -1f,  1f,
                 1f,  -1f, -1f,

                /*
                 1f,  -1f,  1f,


                 1f,  1f,  1f,

                // right
                 1f, -1f,  -1f,
                 1f, -1f,   1f,
*/

        };
//                0.15f, 0.3f, 0.0f,};

        cubeMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        cubeMesh.setVertices(vertices);
    }

    private Mesh testMesh;
    private float [] testVertices = new float[3000];
    public void createTestMesh() {
        testMesh = new Mesh(false, 3000, 0, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        testMesh.setVertices(testVertices);
    }

    @Override
    public void escaped() {
        Gdx.app.exit();
    }

    private float flow = 0;
    private float x = 0;
    private float y = 0;
    private float z = 0;

    private void generateStars(int amount) {
        while(amount-- > 0) {
            double angle = random.nextDouble() * Math.PI * 2;

            float x = (float)Math.sin(angle) * 2;
            float y = (float)Math.cos(angle) * 2;

            Star star = new Star(x , y, - random.nextFloat() * DEPTH);

            stars.add(star);
        }
    }

    @Override
    public void render(float delta ) {
        super.render(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            points -= 25;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            generateStars(25);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y -= 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y += 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            z += 0.1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            z -= 0.1f;
        }

        // move the stars
        for (Star star : stars) {
            star.z += 1;

            if (star.z > 0) {
                star.z -= DEPTH;
            }
        }

        // render the stars
        ShaderProgram program = ShaderHandler.get("point");

        program.begin();

        view.idt().setToTranslation(0, 0, z);

        view.rotate(1,0,0,x);
        view.rotate(0,1,0,y);

        for (Star star : stars) {
            model.setToTranslation(star.x, star.y, star.z);
            model.scale(0.05f, 0.05f, 0.05f);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);
            lineMesh.render(program, GL20.GL_TRIANGLE_FAN);
        }

        program.end();

        batch.begin();
        //batch.enableBlending();
        //batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);

//        ubuntuMedium.draw(batch, "Testing 123!", 100, Gdx.graphics.getHeight() - 300);

        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);
        font.draw(batch, "Stars "+stars.size(), 50, 100);
        font.draw(batch, "x,y,z "+x+", "+y+", "+z, 50, 80);

//        font.draw(batch, "ESC. Exit", 50, 70);
//        font.draw(batch, "F1. Switch fullscreen", 50, 50);

        batch.end();

    }

    @Override
    public void dispose() {
        meshHelper.dispose();
    }

}
