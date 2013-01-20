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
public class Cubes extends BaseScreen {

    private final static float SIZE = 750;

    private List<Cube> cubes = new ArrayList<Cube>(10000);

    private static class Cube {
        public float x,y,z;
        public float rx,ry,rz;
        public float rdx,rdy,rdz;
        public float [] color;

        private Cube(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;

            this.rdx = random.nextFloat();
            this.rdy = random.nextFloat();
            this.rdz = random.nextFloat();

            color = new float [] { 1f, 1f, 1f };
        }

        private Cube(float x, float y, float z, float r, float g, float b) {
            this.x = x;
            this.y = y;
            this.z = z;

            this.rdx = random.nextFloat();
            this.rdy = random.nextFloat();
            this.rdz = random.nextFloat();

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
        createCubeMesh();
    }

    private Mesh cubeMesh;
    public void createCubeMesh() {
        float [] vertices = new float[] {
                //front
                -1f, -1f,  1f,
                 1f,  1f,  1f,
                -1f,  1f,  1f,

                 1f,  1f,  1f,
                -1f, -1f,  1f,
                 1f, -1f,  1f,

                //left
                -1f, -1f, -1f,
                -1f,  1f,  1f,
                -1f,  1f, -1f,

                -1f,  1f,  1f,
                -1f, -1f, -1f,
                -1f, -1f,  1f,

                //right
                 1f, -1f,  1f,
                 1f,  1f, -1f,
                 1f,  1f,  1f,

                 1f,  1f, -1f,
                 1f, -1f,  1f,
                 1f, -1f, -1f,

                //top
                 1f,  1f, -1f,
                -1f,  1f,  1f,
                 1f,  1f,  1f,

                -1f,  1f,  1f,
                 1f,  1f, -1f,
                -1f,  1f, -1f,

                //bottom
                -1f, -1f, -1f,
                 1f, -1f,  1f,
                -1f, -1f,  1f,

                 1f, -1f,  1f,
                -1f, -1f, -1f,
                 1f, -1f, -1f,

                //back
                 1f, -1f, -1f,
                -1f,  1f, -1f,
                 1f,  1f, -1f,

                -1f,  1f, -1f,
                 1f, -1f, -1f,
                -1f, -1f, -1f,

        };
//                0.15f, 0.3f, 0.0f,};

        cubeMesh = new Mesh(true, vertices.length, 0, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        cubeMesh.setVertices(vertices);
    }

    @Override
    public void escaped() {
        Gdx.app.exit();
    }

    private float x = 0;
    private float y = 0;
    private float z = 0;

    private void generateStars(int amount) {
        while(amount-- > 0) {
            Cube cube = new Cube(
                    SIZE / 2 - random.nextFloat() * SIZE, SIZE / 2 - random.nextFloat() * SIZE, - random.nextFloat() * SIZE,
                    random.nextFloat(), random.nextFloat(), random.nextFloat() );

            cubes.add(cube);
        }
    }

    private void removeStars(int amount) {
        while(amount-- > 0 && !cubes.isEmpty()) {
            cubes.remove(random.nextInt(cubes.size()));
        }
    }

    private void reset() {
        x = 0;
        y = 0;
        z = 0;
        vof = 60f;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean result = true;

        if (!super.keyUp(keycode)) {
            switch(keycode) {
                case Input.Keys.R:
                    reset();
                    break;
                default:
                    result = false;
                    break;
            }
        }

        return result;
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

        // move the cubes
        for (Cube cube : cubes) {
            cube.z += delta * 60;

            cube.rx += cube.rdx * delta * 120;
            cube.ry += cube.rdy * delta * 120;
            cube.rz += cube.rdz * delta * 120;

            if (cube.z > 0) {
                cube.z = -SIZE;
            }
        }

        // render the cubes
        ShaderProgram program = ShaderHandler.get("cube");

        program.begin();

        view.setToTranslation(0, 0, z);

        view.rotate(1,0,0,x);
        view.rotate(0,1,0,y);

        float alphaNear = SIZE - SIZE / 10;
        float alphaDistance = SIZE - alphaNear;

        float [] alpha = new float[1];
        for (Cube cube : cubes) {
            alpha[0] = 1f;
            if (z > alphaNear) {
                alpha[0] = 1 - ((z-alphaNear) / alphaDistance);
            }

            model.setToTranslation(cube.x, cube.y, cube.z);
            model.rotate(1, 0, 0, cube.rx);
            model.rotate(0, 1, 0, cube.ry);
            model.rotate(0, 0, 1, cube.rz);
            //model.scale(0.25f, 0.25f, 0.25f);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);
            program.setUniform3fv("cube_color", cube.color, 0, 3);
            //program.setUniform1fv("star_alpha", alpha, 0, 1);
            cubeMesh.render(program, GL20.GL_TRIANGLES);
        }

        program.end();

        batch.begin();

        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);
        font.draw(batch, "Cubes "+ cubes.size(), 50, 100);
        font.draw(batch, "x,y,z "+x+", "+y+", "+z, 50, 80);
        font.draw(batch, "vof "+vof, 50, 60);

        batch.end();

    }

    @Override
    public void dispose() {

    }

}
