package nl.astraeus.shader.test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import nl.astraeus.shader.test.screen.BaseScreen;
import nl.astraeus.shader.test.screen.Particles;
import nl.astraeus.shader.test.screen.Splash;
import nl.astraeus.shader.test.util.ShaderHandler;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 7:04 PM
 */
public class ShaderTest extends Game {

    public static final String LOG = Game.class.getSimpleName();

    public static Game game = new ShaderTest();

    public static BaseScreen SPLASH;
    public static BaseScreen PARTICLES;

    @Override
    public void create() {
        Gdx.app.log( LOG, "Creating game" );

        SPLASH = new Splash();
        PARTICLES = new Particles();
        SPLASH.init();
        setScreen(SPLASH);

        Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
    }

    @Override
    public void resize(
            int width,
            int height ) {
        Gdx.app.log( LOG, "Resizing game to: " + width + " x " + height );

        SPLASH        = new Splash();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void pause()
    {
        Gdx.app.log( LOG, "Pausing game" );

        super.pause();
    }

    @Override
    public void resume()
    {
        Gdx.app.log( LOG, "Resuming game" );

        super.resume();
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( LOG, "Disposing game" );

        ShaderHandler.dispose();

        super.dispose();
    }

    public static void main(String [] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        //config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        config.width=1024;
        config.height=768;
        config.useGL20 = true;
        config.vSyncEnabled = true;

        // create the game
        LwjglApplication app = new LwjglApplication( game, config );
    }
}
