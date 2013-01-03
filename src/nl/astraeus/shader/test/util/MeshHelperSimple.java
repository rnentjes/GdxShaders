package nl.astraeus.shader.test.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

/**
 * User: rnentjes
 * Date: 1/1/13
 * Time: 7:55 PM
 */
public class MeshHelperSimple {
    private Mesh mesh;
    private String shaderName;
    private Matrix4 matrix;

    public MeshHelperSimple(String name) {
        shaderName = name;
        matrix = new Matrix4();
        matrix.scale(0.5f, 0.5f, 0.5f);
    }

    public void createMesh(float[] vertices, VertexAttribute ... attributes) {
        mesh = new Mesh(true, vertices.length, 0, attributes);
        mesh.setVertices(vertices);
    }

    public Matrix4 getMatrix() {
        return matrix;
    }

    public void translate(float x, float y, float z) {
        matrix.translate(x,y,z);
    }

    public void drawMesh() {
        // this should be called in render()
        if (mesh == null) {
            throw new IllegalStateException("drawMesh called before a mesh has been created.");
        }

        ShaderProgram meshShader = ShaderHandler.get(shaderName);
        meshShader.begin();
        meshShader.setUniformMatrix("u_projectionViewMatrix", matrix);
        mesh.render(meshShader, GL20.GL_TRIANGLES);
        //mesh.render(GL20.GL_LINE_STRIP);
        meshShader.end();
    }

    public void dispose() {
        mesh.dispose();
    }

}