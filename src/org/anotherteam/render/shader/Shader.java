package org.anotherteam.render.shader;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.math.Matrix4f;
import org.anotherteam.math.Vector2i;
import org.anotherteam.util.FileUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public final class Shader {

    private final int programId;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    public Shader(String vertexPath, String fragmentPath) {
        programId = glCreateProgram();

        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId, FileUtils.loadAsString(vertexPath));
        glCompileShader(vertexShaderId);
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(vertexShaderId));
        }

        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, FileUtils.loadAsString(fragmentPath));
        glCompileShader(fragmentShaderId);
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(fragmentShaderId));
        }

        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        glBindAttribLocation(programId, 0, "vertices");
        glBindAttribLocation(programId, 1, "textures");

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(programId));
        }
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(programId));
        }
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }

    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, boolean value) {
        glUniform1i(getUniformLocation(name), value ? GL_TRUE : GL_FALSE);
    }

    public void setUniform(String name, Vector2i value) {
        glUniform2f(getUniformLocation(name), value.getX(), value.getY());
    }

    public void setUniform(String name, Matrix4f value) {
        FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
        matrix.put(value.getAll()).flip();
        glUniformMatrix4fv(getUniformLocation(name), true, matrix);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        glDeleteProgram(programId);
    }
}
