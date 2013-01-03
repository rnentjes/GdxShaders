package nl.astraeus.shader.test.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
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

    private long start = System.currentTimeMillis();
    private MeshHelper meshHelper;
    private MeshHelper meshHelper2;
    private MeshHelperSimple meshHelper3;
    private MeshHelperSimple meshHelper4;
    private List<MeshHelperSimple> triangles = new ArrayList<MeshHelperSimple>(1000);

    @Override
    public void init() {

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
    }

    private Mesh lineMesh;
    private Matrix4 lineMatrix = new Matrix4();
    public void createLineMesh() {
        float [] vertices = new float[] {
                -1.0f, -1.0f, 0.0f,
                 1.0f, -1.0f, 0.0f,
                 1.0f,  1.0f, 0.0f,
                -1.0f,  1.0f, 0.0f
        };
//                0.15f, 0.3f, 0.0f,};

        lineMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Generic, 1, "a_edge"));
        lineMesh.setVertices(vertices);
    }

    @Override
    public void escaped() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta ) {
        super.render(delta);

        ShaderProgram program = ShaderHandler.get("line");

        program.begin();

        Gdx.gl.glLineWidth(5.0f);

        long t = System.currentTimeMillis() - start;
        float time = t / 1000.0f;
        lineMatrix = new Matrix4();
        lineMatrix.scale(0.2f, 0.2f, 0);
        //lineMatrix.scale((float)Math.sin(time) * 0.5f, (float)Math.cos(time) * 0.5f, 0);
        lineMatrix.translate((float)Math.sin(time) * 0.0f, (float)Math.cos(time) * 7f, 0f);

        program.setUniformMatrix("u_projectionViewMatrix", lineMatrix);
        lineMesh.render(program, GL20.GL_TRIANGLE_FAN);
        //program.end();

        //program.begin();
        lineMatrix = new Matrix4();
        lineMatrix.scale(0.5f, 0.1f, 0);
        lineMatrix.rotate(0,0,1, (float) (time * (180 / Math.PI)));

        program.setUniformMatrix("u_projectionViewMatrix", lineMatrix);
        lineMesh.render(program, GL20.GL_TRIANGLE_FAN);

        program.end();

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
        batch.enableBlending();
        batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);

        ubuntuMedium88.draw(batch, "Testing 123!", 100, Gdx.graphics.getHeight() - 100);

        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);

        font.draw(batch, "ESC. Exit", 50, 70);
        font.draw(batch, "F1. Switch fullscreen", 50, 50);

        batch.end();
    }

    @Override
    public void dispose() {
        meshHelper.dispose();
    }

}
