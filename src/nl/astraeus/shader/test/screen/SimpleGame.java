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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: rnentjes
 * Date: 1/5/13
 * Time: 10:36 PM
 */
public class SimpleGame extends BaseScreen {


    private static class Entity {
        public float x, y;
        public double dx,dy;
    }

    private static class Bullet extends Entity {
        private Bullet(float x, float y) {
            this.x = x;
            this.y = y;

            this.dy = 0.002f - random.nextDouble() * 0.004f;
            this.dx = 0.05f + random.nextFloat() * 0.02f;
        }

        public boolean move(float delta) {
            x += dx;
            y += dy;

            return x > 5f;
        }
    }

    private static class Enemy extends Entity {
        private Enemy() {
            this.x = 5f;
            this.y = 1 - random.nextFloat() * 2;

            this.dy = - (0.002f - random.nextDouble() * 0.004f);
            this.dx = - (0.01f + random.nextFloat() * 0.005f);
        }

        public boolean move(float delta) {
            x += dx;
            y += dy;

            return x < -5f;
        }

        public boolean isHit(Bullet bullet) {
            float dx = x - bullet.x;
            float dy = y - bullet.y;

            float delta2 = dx * dx + dy * dy;

            return delta2 < 0.003f;
        }
    }

    private static class Ship extends Entity {
        private Ship() {
            x = 0f;
            y = 0f;
        }

        public void move(float delta) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                dx = dx - delta * 0.25f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                dx = dx + delta * 0.25f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                dy = dy + delta * 0.25f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                dy = dy - delta * 0.25f;
            }

            dx = Math.max(-0.1f, dx);
            dx = Math.min( 0.1f, dx);
            dy = Math.max(-0.1f, dy);
            dy = Math.min( 0.1f, dy);

            dx = dx * 0.9f;
            dy = dy * 0.9f;

            x += dx;
            y += dy;

            x = Math.max(-1.2f,x);
            x = Math.min(1f, x);

            y = Math.max(-1f,y);
            y = Math.min(1f, y);
        }
    }

    private final static float SIZE = 750;

    Ship ship = new Ship();
    List<Bullet> bullets = new ArrayList<Bullet>(500);
    List<Enemy> enemys = new ArrayList<Enemy>(500);

    Matrix4 projection = new Matrix4();
    Matrix4 view = new Matrix4();
    Matrix4 model = new Matrix4();
    Matrix4 combined = new Matrix4();
    float vof = 60f;

    private float x = 0;
    private float y = 0;
    private float z = 0f;

    @Override
    public void init() {
        createPointMesh();
        createLineMesh();
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

    @Override
    public void escaped() {
        Gdx.app.exit();
    }

    float chance = 0f;
    long lastBulletTime = 0;

    @Override
    public void render(float delta) {
        super.render(delta);

        ship.move(delta);

        if (random.nextFloat() < chance) {
            enemys.add(new Enemy());
            chance = 0;
        } else {
            chance += delta;
        }

        float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        projection.setToProjection(1.0f, SIZE, vof, aspect);

        // render

        view.setToTranslation(0, 0, z);

        view.rotate(1,0,0,x);
        view.rotate(0,1,0,y);

        ShaderProgram program = ShaderHandler.get("blue_point");

        program.begin();

        for (Bullet bullet : bullets) {
            model.setToTranslation(bullet.x, bullet.y, -2f);
            model.scale(0.025f, 0.025f, 0.025f);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);
            //program.setUniform1fv("star_alpha", alpha, 0, 1);
            pointMesh.render(program, GL20.GL_TRIANGLE_FAN);
        }

        program.end();

        program = ShaderHandler.get("red_point");

        program.begin();

        for (Enemy enemy : enemys) {
            model.setToTranslation(enemy.x, enemy.y, -2f);
            model.scale(0.05f, 0.05f, 0.05f);
            combined.set(projection).mul(view).mul(model);

            program.setUniformMatrix("u_projectionViewMatrix", combined);
            //program.setUniform1fv("star_alpha", alpha, 0, 1);
            pointMesh.render(program, GL20.GL_TRIANGLE_FAN);
        }

        program.end();

        program = ShaderHandler.get("point");

        program.begin();

        model.setToTranslation(ship.x, ship.y, -2f);
        model.scale(0.05f, 0.05f, 0.05f);
        combined.set(projection).mul(view).mul(model);

        program.setUniformMatrix("u_projectionViewMatrix", combined);
        //program.setUniform1fv("star_alpha", alpha, 0, 1);
        pointMesh.render(program, GL20.GL_TRIANGLE_FAN);

        program.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            long time = System.nanoTime();

            if (time - lastBulletTime > 100000000) {
                lastBulletTime = time;

                Bullet bullet = new Bullet(ship.x, ship.y);

                bullets.add(bullet);
            }
        }

        Set<Bullet> remove = new HashSet<Bullet>();
        Set<Enemy> removeEnemy = new HashSet<Enemy>();

        for (Enemy enemy : enemys) {
            for (Bullet bullet : bullets) {
                if (enemy.isHit(bullet)) {
                    remove.add(bullet);
                    removeEnemy.add(enemy);
                }
            }
        }

        for (Bullet bullet : bullets) {
            if (bullet.move(delta)) {
                remove.add(bullet);
            }
        }

        for (Enemy enemy : enemys) {
            if (enemy.move(delta)) {
                removeEnemy.add(enemy);
            }
        }

        for (Bullet bullet : remove) {
            bullets.remove(bullet);
        }

        for (Enemy enemy : removeEnemy) {
            enemys.remove(enemy);
        }

        batch.begin();

        font.draw(batch, "Bullets "+ bullets.size(), 50, 200);
        font.draw(batch, "Enemys "+ enemys.size(), 50, 180);
        font.draw(batch, "Chance "+chance, 50, 160);
        font.draw(batch, "Delta "+getAvgDelta(), 50, 140);
        font.draw(batch, "FPS   "+getFps(), 50, 120);
        font.draw(batch, "ship x,y,z "+ship.x+", "+ship.y+", -5", 50, 100);
        font.draw(batch, "x,y,z "+x+", "+y+", "+z, 50, 80);
        font.draw(batch, "vof "+vof, 50, 60);

        batch.end();

    }
}
