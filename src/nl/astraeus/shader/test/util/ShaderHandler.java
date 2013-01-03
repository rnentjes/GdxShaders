package nl.astraeus.shader.test.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.HashMap;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 8:54 PM
 */
public class ShaderHandler {

    private static ShaderHandler instance = new ShaderHandler();

    public static ShaderHandler get() {
        return instance;
    }

    private Map<String, ShaderProgram> programs = new HashMap<String, ShaderProgram>();

    public ShaderHandler() {
        createShaderProgram("basic");
        createShaderProgram("basic2");
        createShaderProgram("simple");
        createShaderProgram("line");
    }

    private void createShaderProgram(String name) {
        // make an actual shader from our strings
        ShaderProgram shader = new ShaderProgram(
                Gdx.files.internal("data/shader/" + name + ".vert").readString(),
                Gdx.files.internal("data/shader/" + name + ".frag").readString());

        // check there's no shader compile errors
        if (!shader.isCompiled()) {
            throw new IllegalStateException(shader.getLog());
        }

        programs.put(name, shader);
    }

    public static ShaderProgram get(String name) {
        ShaderProgram result;

        result = get().programs.get(name);

        if (result == null) {
            throw new IllegalStateException("Shader with name '"+name+"' not found!");
        }

        return result;
    }

    public static void dispose() {
        for(ShaderProgram program : get().programs.values()) {
            program.dispose();
        }
    }

}
