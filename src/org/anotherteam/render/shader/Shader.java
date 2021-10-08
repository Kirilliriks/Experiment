package org.anotherteam.render.shader;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.util.FileUtils;

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

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(programId, name);
        if (location != -1) {
            glUniform1i(location, value);
        }
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
