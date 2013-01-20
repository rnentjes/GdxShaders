package nl.astraeus.shader.test.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import nl.astraeus.shader.test.util.ShaderHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 7:08 PM
 */
public class Stars extends BaseScreen {

    private final static float SIZE = 750;

    private List<Star> stars = new ArrayList<Star>(10000);

    private static class Star {
        public float x,y,z;
        public float [] color;

        private Star(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;

            color = new float [] { 1f, 1f, 1f };
        }

        private Star(float x, float y, float z, float r, float g, float b) {
            this.x = x;
            this.y = y;
            this.z = z;

            color = new float [] { r, g, b };
        }
    }

    Matrix4 projection = new Matrix4();
    Matrix4 view = new Matrix4();
    Matrix4 model = new Matrix4();
    Matrix4 combined = new Matrix4();
    float vof = 60f;

    @Override
    public void init() {
        createLineMesh();
        createTestMesh();
    }

    private Mesh lineMesh;
    public void createLineMesh() {
        float [] vertices = new float[] {
                -1.0f, -1.0f,
                 1.0f, -1.0f,
                 1.0f,  1.0f,
                -1.0f,  1.0f,
        };

        lineMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"));
        lineMesh.setVertices(vertices);
    }

    private Mesh testMesh;
    private float [] testVertices = new float[600];
    public void createTestMesh() {
        testMesh = new Mesh(false, 600, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.Generic, 2, "a_star_position"),
                new VertexAttribute(VertexAttributes.Usage.Color, 4, "a_color"));
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
            Star star = new Star(
                    SIZE / 2 - random.nextFloat() * SIZE, SIZE / 2 - random.nextFloat() * SIZE, - random.nextFloat() * SIZE,
                    random.nextFloat(), random.nextFloat(), random.nextFloat() );

            stars.add(star);
        }
    }

    private void removeStars(int amount) {
        while(amount-- > 0 && !stars.isEmpty()) {
            stars.remove(random.nextInt(stars.size()));
        }
    }

    @Override
    public void render(float delta ) {
        super.render(delta);

        float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        projection.setToProjection(1.0f, SIZE, vof, aspect);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET)) {
            removeStars(25);
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
            z += 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            z -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            vof += 0.1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            vof -= 0.1f;
        }

        // move the stars
        for (Star star : stars) {
            star.z += delta * 60;

            if (star.z > 0) {
                star.z = -SIZE;
            }
        }

        // render the stars
        ShaderProgram program = ShaderHandler.get("star");

        program.begin();

        view.setToTranslation(0, 0, z);

        view.rotate(1,0,0,x);
        view.rotate(0,1,0,y);

        float alphaNear = SIZE - SIZE / 10;
        float alphaDistance = SIZE - alphaNear;

        float [] alpha = new float[1];
        for (Star star : stars) {
            alpha[0] = 1f;
            if (z > alphaNear) {
                alpha[0] = 1 - ((z-alphaNear) / alphaDistance);
            }

            model.setToTranslation(star.x, star.y, star.z);
            //model.scale(0.25f, 0.25f, 0.25f);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);
            program.setUniform3fv("star_color", star.color, 0, 3);
            //program.setUniform1fv("star_alpha", alpha, 0, 1);
            lineMesh.render(program, GL20.GL_TRIANGLE_FAN);
        }

        program.end();

        batch.begin();

        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);
        font.draw(batch, "Stars "+stars.size(), 50, 100);
        font.draw(batch, "x,y,z "+x+", "+y+", "+z, 50, 80);
        font.draw(batch, "vof "+vof, 50, 60);

        batch.end();

    }

    @Override
    public void dispose() {

    }

}
