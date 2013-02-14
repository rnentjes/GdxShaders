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
public class Splash extends BaseScreen {

    private long start = System.currentTimeMillis() - 100000;
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
    float angle = 45;
    int points = 1000;

    @Override
    public void init() {
        cam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.translate(0f, 0, 2f);

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

    private float flow = 100f;
    private float x = 0;
    private float y = 0;
    private float z = 0;

    @Override
    public void render(float delta ) {
        super.render(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            z += 0.1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            z -= 0.1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            points -= 25;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET)) {
            points += 25;
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

        ShaderProgram program = ShaderHandler.get("color_point");

        program.begin();

        Gdx.gl.glLineWidth(5.0f);

        long t = System.currentTimeMillis() - start;
        float time = t / 1000.0f;


        flow += 0.08f;
        angle += delta * 40.0f;
        float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        projection.setToProjection(1.0f, 100.0f, 60.0f, aspect);
        view.idt().trn(0, 0, z);

        float [] colors = new float[] { 0.9f, 0.3f, 0.2f };
        float t2 = time / 100f;
        for (int i=1; i < 10001; i++) {
            model.setToTranslation((float)Math.sin(t2*i*0.031f+ flow) * 10f * aspect,(float)Math.cos(t2*i*0.037f+ flow) * 10f, -40f+(float)Math.sin(t2*i*0.047f+ flow) * 10f);
            model.scale(2f, 2f, 2f);
            //model.rotate(axis, angle);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);

            float angle = (float)(((t2*i*0.031f+ flow) + Math.cos(t2*i*0.037f+ flow) + Math.sin(t2*i*0.047f+ flow)) / (Math.PI * 2));
            java.awt.Color c = java.awt.Color.getHSBColor(angle, 0.8f, 0.3f);
            c.getRGBColorComponents(colors);


            program.setUniform3fv("u_color", colors , 0, 3);
            lineMesh.render(program, GL20.GL_TRIANGLE_FAN);
        }

        program.end();

        program = ShaderHandler.get("bordered_circle");

        program.begin();

        model.setToTranslation( -0.8f, 0.8f, -2f );
        model.scale(1f, 0.5f, 1f);
        //model.rotate(axis, angle);
        combined.set(projection).mul(view).mul(model);

        program.setUniformMatrix("u_projectionViewMatrix", combined);
        pointMesh.render(program, GL20.GL_TRIANGLE_FAN);

        model.setToTranslation( -0.8f, 0.8f, -50f + (float)Math.sin(time) * 48f );
        model.scale(0.6f, 0.6f, 0.6f);
        //model.rotate(axis, angle);
        combined.set(projection).mul(view).mul(model);

        program.setUniformMatrix("u_projectionViewMatrix", combined);
        lineMesh.render(program, GL20.GL_TRIANGLE_FAN);

        program.end();

        ShaderProgram testProgram = ShaderHandler.get("simple");

        t2 = time / 10f;
        int index = 0;
        for (int i=0; i < 1000; i++) {
            testVertices[index++] = (float)(Math.sin(t2*i*0.011f+ flow) * Math.sin(t2*i*0.031f+ flow) + Math.sin(t2*i*0.032f+ flow));
            testVertices[index++] = (float)(Math.cos(t2*i*0.017f+ flow) * Math.sin(t2*i*0.033f+ flow) + Math.sin(t2*i*0.034f+ flow));
            testVertices[index++] = (float)(Math.sin(t2*i*0.023f+ flow) * Math.sin(t2*i*0.037f+ flow) + Math.sin(t2*i*0.038f+ flow));
        }

        testMesh.setVertices(testVertices);

        testProgram.begin();

        // whole screen in front
        model.setToTranslation( 0f, 0f, -2f );
        //model.scale(0.1f, 0.1f, 0.1f);
        model.rotate(1,0,0,x);
        model.rotate(0,1,0,y);
        combined.set(projection).mul(view).mul(model);

        Gdx.graphics.getGL20().glLineWidth(3f);

        testProgram.setUniformMatrix("u_projectionViewMatrix", combined);
        testMesh.render(testProgram, GL20.GL_TRIANGLES);

        testProgram.end();

/*
        program.begin();
        lineMatrix = new Matrix4();
        lineMatrix.translate(0,0,100f);
        lineMatrix.scale(0.5f, 0.1f, 0);
        lineMatrix.rotate(0,0,1, (float) (time * (180 / Math.PI)));

        program.setUniformMatrix("u_projectionViewMatrix", lineMatrix);
        lineMesh.render(program, GL20.GL_TRIANGLE_FAN);

        program.end();
*/

        //meshHelper.drawMesh();
        //meshHelper2.drawMesh();
        //meshHelper3.getMatrix().rotate(1.0f, 1.0f, 0.0f, 001f);

        //meshHelper3.drawMesh();
//        meshHelper4.drawMesh();

        /*
        float r = 0.01f;
        //r += 0.0001f;
        for (MeshHelperSimple simple : triangles) {
            simple.getMatrix().rotate(1.0f, 1.0f, 0.0f, r);

            simple.drawMesh();
        }*/

        batch.begin();
        //batch.enableBlending();
        //batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);

//        ubuntuMedium.draw(batch, "Testing 123!", 100, Gdx.graphics.getHeight() - 300);

        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);
        font.draw(batch, "Points   "+points, 50, 100);
        font.draw(batch, "x,y "+x+", "+y, 50, 80);

//        font.draw(batch, "ESC. Exit", 50, 70);
//        font.draw(batch, "F1. Switch fullscreen", 50, 50);

        batch.end();
    }

    @Override
    public void dispose() {
        meshHelper.dispose();
    }

}
