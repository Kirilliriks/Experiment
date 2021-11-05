package org.anotherteam.render.shader;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.util.FileUtils;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.lwjgl.system.MemoryUtil;

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

        glBindAttribLocation(programId, 0, "vertex_pos");
        glBindAttribLocation(programId, 1, "in_tex_coord");

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

    public void setUniform(String name, Vector2i vector2i) {
        glUniform2f(getUniformLocation(name), vector2i.x, vector2i.y);
    }

    public void setUniform(String name, float x, float y) {
        glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, Matrix4f value) {
        val matrix = MemoryUtil.memAllocFloat(16);
        value.get(matrix);
        glUniformMatrix4fv(getUniformLocation(name), false, matrix);
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
