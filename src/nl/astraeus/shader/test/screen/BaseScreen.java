package nl.astraeus.shader.test.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.astraeus.shader.test.ShaderTest;

import java.util.Random;

/**
 * User: rnentjes
 * Date: 11/16/12
 * Time: 9:53 PM
 */
public abstract class BaseScreen extends InputAdapter implements Screen {
    protected static Random random = new Random(System.nanoTime());

    protected final BitmapFont font;
    protected final BitmapFont ubuntuMedium;
    protected final SpriteBatch batch;
    protected final ShapeRenderer shapeRenderer;

    protected boolean escaped = false;
    protected boolean F1 = false;

    private double totalDelta = 0;
    private double frames = 0;
    private double avgDelta = 0;

    public BaseScreen() {
        this.ubuntuMedium = new BitmapFont(Gdx.files.internal("data/font/UbuntuMedium83.fnt"), Gdx.files.internal("data/font/UbuntuMedium83.png"), false);
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setScreen(BaseScreen screen) {
        screen.init();
        ShaderTest.game.setScreen(screen);
    }

    public abstract void init();
    public abstract void escaped();

    public void switchFullScreen() {
        if (Gdx.app.getGraphics().supportsDisplayModeChange()) {
            boolean fullscreen = Gdx.app.getGraphics().isFullscreen();

            if (!fullscreen) {
                Gdx.app.getGraphics().setDisplayMode(Gdx.app.getGraphics().getDesktopDisplayMode());
            } else {
                Gdx.app.getGraphics().setDisplayMode(1024, 768, false);
            }
        }
    }

    public double getAvgDelta() {
        return avgDelta;
    }

    public int getFps() {
        return (int)(1.0/avgDelta);
    }

    @Override
    public void render(float delta ) {
        totalDelta += delta;
        frames++;

        if (totalDelta > 1.0 && frames > 0) {
            avgDelta = totalDelta / frames;

            totalDelta -= 1.0;
            frames = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escaped = true;
        } else if (escaped) {
            escaped();
            escaped = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            F1 = true;
        } else if (F1) {
            switchFullScreen();
            F1 = false;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor( 0.03f, 0f, 0.03f, 1.0f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}


    @Override
    public void resize(int width, int height) {
        // todo: something...
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        shapeRenderer.dispose();;
    }
}
