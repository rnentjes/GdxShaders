package nl.astraeus.shader.test.util;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 7:55 PM
 */
public class MeshHelper {
    private Mesh mesh;
    private String shaderName;
    private long start = System.currentTimeMillis();

    public MeshHelper(String name) {
        shaderName = name;
    }

    public void createMesh(float[] vertices, VertexAttribute ... attributes) {
        mesh = new Mesh(true, vertices.length, 0, attributes);
        mesh.setVertices(vertices);
        }

    public void drawMesh() {
        // this should be called in render()
        if (mesh == null) {
            throw new IllegalStateException("drawMesh called before a mesh has been created.");
        }

        ShaderProgram meshShader = ShaderHandler.get(shaderName);
        meshShader.begin();


        long timeInMS = System.currentTimeMillis() - start;
        float timeInS = timeInMS / 1000.0f;
        meshShader.setUniformf("u_time", timeInS);


        mesh.render(meshShader, GL20.GL_TRIANGLES);
        meshShader.end();
    }

    public void dispose() {
        mesh.dispose();
    }

}