package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Utilities.FileResourceReader;
import com.crispin.crispinmobile.Utilities.Logger;
import com.crispin.crispinmobile.Utilities.ShaderCache;

import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.glGetProgramInfoLog;
import static android.opengl.GLES30.glGetProgramiv;
import static android.opengl.GLES30.glGetShaderInfoLog;
import static android.opengl.GLES30.GL_INFO_LOG_LENGTH;
import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_INVALID_VALUE;
import static android.opengl.GLES30.GL_TRUE;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glCreateShader;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glGetAttribLocation;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glGetUniformLocation;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.glUseProgram;

/**
 * Shader class is used to load and compile and manage GLSL shader programs from a file or
 * string. It can be used to load vertex/fragment shader programs. The class is designed to be
 * derived from/extended.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Shader
{
    // Tag for the logger
    private static final String TAG = "Shader";

    // Undefined handle value
    protected static int UNDEFINED_HANDLE = -1;

    // Name used in logging
    private String name;

    // Vertex shader code
    private String vertexShaderCode;

    // Fragment shader code
    private String fragmentShaderCode;

    // The ID of the OpenGL program
    private int programId;

    // Position attribute handle
    protected int positionAttributeHandle;

    // Colour attribute handle
    protected int colourAttributeHandle;

    // Texture attribute handle
    protected int textureAttributeHandle;

    // Normal attribute handle
    protected int normalAttributeHandle;

    // Colour uniform handle
    protected int colourUniformHandle;

    // Texture uniform handle
    protected int textureUniformHandle;

    // Specular map uniform handle
    protected int specularMapUniformHandle;

    // Normal map uniform handle
    protected int normalMapUniformHandle;

    // Matrix uniform handle
    protected int matrixUniformHandle;

    // UV multiplier uniform handle
    protected int uvMultiplierUniformHandle;

    // UV offset uniform handle
    protected int uvOffsetUniformHandle;

    // Projection matrix uniform handle
    protected int projectionMatrixUniformHandle;

    // View matrix uniform handle
    protected int viewMatrixUniformHandle;

    // Model matrix uniform handle
    protected int modelMatrixUniformHandle;

    // Number of point lights
    protected int  numPointLightsUniformHandle;

    public class PointLightHandles {
        // Position uniform handle
        public int positionUniformHandle;

        // Colour uniform handle
        public int colourUniformHandle;

        // Diffuse strength uniform handle
        public int diffuseUniformHandle;

        // Ambience strength uniform handle
        public int ambientUniformHandle;

        // Specular strength uniform handle
        public int specularUniformHandle;

        // Attenuation constant variable uniform handle
        public int constantUniformHandle;

        // Attenuation linear variable uniform handle
        public int linearUniformHandle;

        // Attenuation quadratic variable uniform handle
        public int quadraticUniformHandle;

        public PointLightHandles() {
            positionUniformHandle = UNDEFINED_HANDLE;
            colourUniformHandle = UNDEFINED_HANDLE;
            diffuseUniformHandle = UNDEFINED_HANDLE;
            ambientUniformHandle = UNDEFINED_HANDLE;
            specularUniformHandle = UNDEFINED_HANDLE;
            constantUniformHandle = UNDEFINED_HANDLE;
            linearUniformHandle = UNDEFINED_HANDLE;
            quadraticUniformHandle = UNDEFINED_HANDLE;
        }
    }

//    public class SpotLightHandles {
//        // Position uniform handle
//        public int positionUniformHandle;
//
//        // Direction uniform handle
//        public int directionUniformHandle;
//
//        // Colour uniform handle
//        public int colourUniformHandle;
//
//        // Diffuse strength uniform handle
//        public int diffuseUniformHandle;
//
//        // Ambience strength uniform handle
//        public int ambientUniformHandle;
//
//        // Specular strength uniform handle
//        public int specularUniformHandle;
//
//        // Attenuation constant variable uniform handle
//        public int constantUniformHandle;
//
//        // Attenuation linear variable uniform handle
//        public int linearUniformHandle;
//
//        // Attenuation quadratic variable uniform handle
//        public int quadraticUniformHandle;
//
//        // Size of the light (when to begin fading out the light radius of the light)
//        public int sizeUniformHandle;
//
//        // Outer size of the light (when to end fading out the light radius of the light)
//        public int outerSizeUniformHandle;
//
//        public SpotLightHandles() {
//            positionUniformHandle = UNDEFINED_HANDLE;
//            directionUniformHandle = UNDEFINED_HANDLE;
//            colourUniformHandle = UNDEFINED_HANDLE;
//            diffuseUniformHandle = UNDEFINED_HANDLE;
//            ambientUniformHandle = UNDEFINED_HANDLE;
//            specularUniformHandle = UNDEFINED_HANDLE;
//            constantUniformHandle = UNDEFINED_HANDLE;
//            linearUniformHandle = UNDEFINED_HANDLE;
//            quadraticUniformHandle = UNDEFINED_HANDLE;
//            sizeUniformHandle = UNDEFINED_HANDLE;
//            outerSizeUniformHandle = UNDEFINED_HANDLE;
//        }
//    }

    // Handles for all the shader point lights
    protected PointLightHandles pointLightHandles[];

    // View position uniform handle
    protected int viewPositionUniformHandle;

    // Material diffuse strength uniform handle
    protected int materialDiffuseUniformHandle;

    // Material ambience strength uniform handle
    protected int materialAmbientUniformHandle;

    // Material specular strength uniform handle
    protected int materialSpecularUniformHandle;

    // Material shininess uniform handle
    protected int materialShininessUniformHandle;

    // Is the shader a lighting shader
    protected boolean lightingShader;

    public boolean validHandle(int handle)
    {
        return handle != UNDEFINED_HANDLE;
    }

    /**
     * Enable the shader program. Should be used before making render calls to objects that you wish
     * to render using the shader.
     *
     * @since 1.0
     */
    public void enableIt()
    {
        glUseProgram(programId);
    }

    /**
     * Disable the shader program. Should be used when finished drawing objects with the shader.
     *
     * @since 1.0
     */
    public void disableIt()
    {
        glUseProgram(0);
    }

    /**
     * Re-create and compile the shader program. This is used to re-load the shader into OpenGL ES
     * memory if it is destroyed.
     *
     * @since 1.0
     */
    public void reconstruct()
    {
        programId = createProgram(name, vertexShaderCode, fragmentShaderCode);
    }

    /**
     * Remove the program from OpenGL ES memory
     *
     * @since 1.0
     */
    public void destroy()
    {
        glDeleteProgram(programId);
        programId = GL_INVALID_VALUE;
    }

    /**
     * Get an integer ID for a handle of a specified attribute
     *
     * @param attributeName The name of the attribute in the shader code
     * @return              Integer ID of the attribute handle
     * @since               1.0
     */
    public int getAttribute(String attributeName)
    {
        return glGetAttribLocation(programId, attributeName);
    }

    /**
     * Get an integer ID for a handle of a specified uniform
     *
     * @param uniformName   The name of the uniform in the shader code
     * @return              Integer ID of the uniform handle
     * @since               1.0
     */
    public int getUniform(String uniformName)
    {
        return glGetUniformLocation(programId, uniformName);
    }

    /**
     * Get the position attribute handle
     *
     * @return Integer ID of the position attribute handle
     * @since 1.0
     */
    public int getPositionAttributeHandle()
    {
        return positionAttributeHandle;
    }

    /**
     * Get the colour attribute handle
     *
     * @return Integer ID of the colour attribute handle
     * @since 1.0
     */
    public int getColourAttributeHandle()
    {
        return colourAttributeHandle;
    }

    /**
     * Get the texture attribute handle
     *
     * @return Integer ID of the texture attribute handle
     * @since 1.0
     */
    public int getTextureAttributeHandle()
    {
        return textureAttributeHandle;
    }

    /**
     * Get the normal attribute handle
     *
     * @return Integer ID of the normal attribute handle
     * @since 1.0
     */
    public int getNormalAttributeHandle()
    {
        return normalAttributeHandle;
    }

    /**
     * Get the colour uniform handle
     *
     * @return Integer ID of the colour uniform handle
     * @since 1.0
     */
    public int getColourUniformHandle()
    {
        return colourUniformHandle;
    }

    /**
     * Get the texture uniform handle
     *
     * @return Integer ID of the texture uniform handle
     * @since 1.0
     */
    public int getTextureUniformHandle()
    {
        return textureUniformHandle;
    }

    /**
     * Get the specular map uniform handle
     *
     * @return Integer ID of the specular map uniform handle
     * @since 1.0
     */
    public int getSpecularMapUniformHandle()
    {
        return specularMapUniformHandle;
    }

    /**
     * Get the normal map uniform handle
     *
     * @return Integer ID of the normal map uniform handle
     * @since 1.0
     */
    public int getNormalMapUniformHandle()
    {
        return normalMapUniformHandle;
    }

    /**
     * Get the matrix uniform handle
     *
     * @return  Integer ID of the matrix uniform handle
     * @since   1.0
     */
    public int getMatrixUniformHandle()
    {
        return matrixUniformHandle;
    }

    /**
     * Get the UV multiplier uniform handle
     *
     * @return  Integer ID of the UV multipliers uniform handle
     * @since   1.0
     */
    public int getUvMultiplierUniformHandle()
    {
        return uvMultiplierUniformHandle;
    }

    /**
     * Get the UV offset uniform handle
     *
     * @return  Integer ID of the UV offset uniform handle
     * @since   1.0
     */
    public int getUvOffsetUniformHandle()
    {
        return uvOffsetUniformHandle;
    }

    /**
     * Get the projection matrix uniform handle
     *
     * @return  Integer ID of the projection matrix uniform handle
     * @since   1.0
     */
    public int getProjectionMatrixUniformHandle()
    {
        return projectionMatrixUniformHandle;
    }

    /**
     * Get the view matrix uniform handle
     *
     * @return  Integer ID of the view matrix uniform handle
     * @since   1.0
     */
    public int getViewMatrixUniformHandle()
    {
        return viewMatrixUniformHandle;
    }

    /**
     * Get the model matrix uniform handle
     *
     * @return  Integer ID of the model matrix uniform handle
     * @since   1.0
     */
    public int getModelMatrixUniformHandle()
    {
        return modelMatrixUniformHandle;
    }

    /**
     * Get the view position uniform handle
     *
     * @return  Integer ID of the view position uniform handle
     * @since   1.0
     */
    public int getViewPositionUniformHandle() {
        return viewPositionUniformHandle;
    }

    /**
     * Get the num lights uniform handle
     *
     * @return  Integer ID of the num lights uniform handle
     * @since   1.0
     */
    public int getNumLightsUniformHandle() {
        return numPointLightsUniformHandle;
    }

    /**
     * Get the maximum number of lights that the shader supports
     *
     * @return  Max number of lights the shader can handle
     * @since   1.0
     */
    public int getMaxLights() {
        if(pointLightHandles == null) {
            return 0;
        }

        return pointLightHandles.length;
    }

//    /**
//     * Get a light direction uniform handle
//     *
//     * @return  Integer ID of the light direction uniform handle
//     * @since   1.0
//     */
//    public int getSpotLightDirectionUniformHandle(int index) {
//        if(pointLightHandles != null && pointLightHandles.length >= index) {
//            return pointLightHandles[index].directionUniformHandle;
//        }
//
//        return UNDEFINED_HANDLE;
//    }

//    /**
//     * Get a light size uniform handle
//     *
//     * @return  Integer ID of the light size uniform handle
//     * @since   1.0
//     */
//    public int getLightSizeUniformHandle(int index) {
//        if(pointLightHandles != null && pointLightHandles.length >= index) {
//            return pointLightHandles[index].sizeUniformHandle;
//        }
//
//        return UNDEFINED_HANDLE;
//    }
//
//    /**
//     * Get a light outer size uniform handle
//     *
//     * @return  Integer ID of the outer light size uniform handle
//     * @since   1.0
//     */
//    public int getLightOuterSizeUniformHandle(int index) {
//        if(pointLightHandles != null && pointLightHandles.length >= index) {
//            return pointLightHandles[index].outerSizeUniformHandle;
//        }
//
//        return UNDEFINED_HANDLE;
//    }

    /**
     * Get a point light position uniform handle
     *
     * @return  Integer ID of the point light position uniform handle
     * @since   1.0
     */
    public int getPointLightPositionUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].positionUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light colour uniform handle
     *
     * @return  Integer ID of the point light colour uniform handle
     * @since   1.0
     */
    public int getPointLightColourUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].colourUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light ambience strength uniform handle
     *
     * @return  Integer ID of the point light ambience strength uniform handle
     * @since   1.0
     */
    public int getPointLightAmbientUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].ambientUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light intensity uniform handle
     *
     * @return  Integer ID of the point light intensity uniform handle
     * @since   1.0
     */
    public int getPointLightDiffuseUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].diffuseUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light specular strength uniform handle
     *
     * @return  Integer ID of the point light specular strength uniform handle
     * @since   1.0
     */
    public int getPointLightSpecularUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].specularUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light attenuation constant uniform handle
     *
     * @return  Integer ID of the point light attenuation constant uniform handle
     * @since   1.0
     */
    public int getPointLightAttenuationConstantUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].constantUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light attenuation linear uniform handle
     *
     * @return  Integer ID of the point light attenuation linear uniform handle
     * @since   1.0
     */
    public int getPointLightAttenuationLinearUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].linearUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get a point light attenuation quadratic uniform handle
     *
     * @return  Integer ID of the point light attenuation quadratic uniform handle
     * @since   1.0
     */
    public int getPointLightAttenuationQuadraticUniformHandle(int index) {
        if(pointLightHandles != null && pointLightHandles.length >= index) {
            return pointLightHandles[index].quadraticUniformHandle;
        }

        return UNDEFINED_HANDLE;
    }

    /**
     * Get the material diffuse strength uniform handle
     *
     * @return  Integer ID of the light intensity uniform handle
     * @since   1.0
     */
    public int getMaterialDiffuseUniformHandle() {
        return materialDiffuseUniformHandle;
    }

    /**
     * Get the material ambient strength uniform handle
     *
     * @return  Integer ID of the light ambience strength uniform handle
     * @since   1.0
     */
    public int getMaterialAmbientUniformHandle() {
        return materialAmbientUniformHandle;
    }

    /**
     * Get the material specular strength uniform handle
     *
     * @return  Integer ID of the light specular strength uniform handle
     * @since   1.0
     */
    public int getMaterialSpecularUniformHandle() {
        return materialSpecularUniformHandle;
    }

    /**
     * Get the material shininess uniform handle
     *
     * @return  Integer ID of the material shininess uniform handle
     * @since   1.0
     */
    public int getMaterialShininessUniformHandle() { return materialShininessUniformHandle; }
    /**
     * Set the state of the lighting shader variable. If the shader supports lighting then this
     * should be set to true, else false.
     * @param state Set to true if the shader supports lighting. This will enable different upload
     *              of matrix data to the shader program from the RenderObject.
     * @since 1.0
     */
    public void setLightingShader(boolean state)
    {
        lightingShader = state;
    }

    /**
     * Is the shader a lighting shader. Get the state of the lighting shader variable.
     *
     * @return  True if the shader is a lighting shader, else false
     */
    public boolean isLightingShader()
    {
        return lightingShader;
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache.
     *
     * @param vertexShaderCode      The vertex shader code as a string. Code must be written in GLSL
     *                              shader programming language
     * @param fragmentShaderCode    The fragment shader code as a string. Code must be written in
     *                              GLSL shader programming language.
     * @since                       1.0
     */
    protected Shader(String name, String vertexShaderCode, String fragmentShaderCode)
    {
        this.name = name;
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;

        // Create a new shader program using the vertex shader code and fragment shader code
        programId = createProgram(name, vertexShaderCode, fragmentShaderCode);

        lightingShader = false;

        // Set the default values of all of the different handles to undefined
        positionAttributeHandle = UNDEFINED_HANDLE;
        colourAttributeHandle = UNDEFINED_HANDLE;
        textureAttributeHandle = UNDEFINED_HANDLE;
        normalAttributeHandle = UNDEFINED_HANDLE;
        colourUniformHandle = UNDEFINED_HANDLE;
        textureUniformHandle = UNDEFINED_HANDLE;
        specularMapUniformHandle = UNDEFINED_HANDLE;
        normalMapUniformHandle = UNDEFINED_HANDLE;
        matrixUniformHandle = UNDEFINED_HANDLE;
        uvMultiplierUniformHandle = UNDEFINED_HANDLE;
        uvOffsetUniformHandle = UNDEFINED_HANDLE;
        projectionMatrixUniformHandle = UNDEFINED_HANDLE;
        viewMatrixUniformHandle = UNDEFINED_HANDLE;
        modelMatrixUniformHandle = UNDEFINED_HANDLE;
        viewPositionUniformHandle = UNDEFINED_HANDLE;
        numPointLightsUniformHandle = UNDEFINED_HANDLE;
        materialDiffuseUniformHandle = UNDEFINED_HANDLE;
        materialAmbientUniformHandle = UNDEFINED_HANDLE;
        materialSpecularUniformHandle = UNDEFINED_HANDLE;
        materialShininessUniformHandle = UNDEFINED_HANDLE;
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache.
     *
     * @param vertexShaderCode      The vertex shader code as a byte array. Code must be written in
     *                              GLSL shader programming language
     * @param fragmentShaderCode    The fragment shader code as a byte arrray. Code must be written
     *                              in GLSL shader programming language.
     * @since                       1.0
     */
    protected Shader(String name, byte[] vertexShaderCode, byte[] fragmentShaderCode)
    {
        this(name, new String(vertexShaderCode), new String(fragmentShaderCode));
    }

    /**
     * Shader constructor. Create and compile the GLSL shader in Open GL ES memory and then register
     * it in the shader cache. Reads in the shader files for the given resource IDs
     *
     * @param vertexShaderResourceId    The vertex shader file resource ID. Code must be written in
     *                                  GLSL shader programming language
     * @param fragmentShaderResourceId  The fragment shader file resource ID. Code must be written
     *                                  in GLSL shader programming language.
     * @since                           1.0
     */
    protected Shader(String name, int vertexShaderResourceId, int fragmentShaderResourceId)
    {
        this(name, FileResourceReader.readRawResource(vertexShaderResourceId),
                FileResourceReader.readRawResource(fragmentShaderResourceId));

        // Register the shader to the cache so that the engine can handle the re-init call.
        // It is important that the engine handles this call, because the shader memory (all
        // OpenGL ES memory) is cleared when the Android activity 'onSurfaceCreated' is called.
        // This happens on screen rotation or when re-opening the application. The re-init call
        // will re-create the shader's OpenGL ES memory parts. This means that the user doesn't need
        // to worry about this and can just create new shader's in the constructor of their scenes.
        ShaderCache.registerShader(vertexShaderResourceId, fragmentShaderResourceId, this);
    }

    /**
     * Create the shader program. This includes all steps of GLSL shader program creation. The steps
     * are: create and compile vertex and fragment shaders, create program ID, attach vertex and
     * fragment shaders to program, then link the program.
     *
     * @param name                  Shader name string for logging
     * @param vertexShaderCode      String containing vertex shader code. Must be written in GLSL
     *                              shader programming language.
     * @param fragmentShaderCode    String containing fragment shader code. Must be written in GLSL
     *                              shader programming language.
     * @return                      Program ID integer
     * @since                       1.0
     */
    private static int createProgram(String name,
                                     String vertexShaderCode,
                                     String fragmentShaderCode)
    {
        // Create a vertex shader
        final int vertexShaderId = createShader(name, GL_VERTEX_SHADER, vertexShaderCode);

        // Create a fragment shader
        final int fragmentShaderId = createShader(name, GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create the shader program
        final int programId = glCreateProgram();

        // Throw exception on failure
        if (programId == 0)
        {
            Logger.error(TAG, "Failed to generate a program object");
        }

        // Attach the vertex and fragment shaders to the program
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        // Link the shaders
        glLinkProgram(programId);

        // Delete the vertex and fragment shaders from OpenGL ES memory
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);

        // Check for linking errors
        if(isLinked(programId)) {
            Logger.info("Linked shader program '" + name + "' successfully");
        } else {
            final int[] maxLength = new int[1];
            glGetProgramiv(programId, GL_INFO_LOG_LENGTH, maxLength, 0);
            final String linkLog = glGetProgramInfoLog(programId);
            if(!linkLog.isEmpty()) {
                Logger.error(TAG, "Failed to link '" + name +
                        "' shader program. \n-- Shader Program Link Log Start --\n" + linkLog +
                        "\n-- Shader Program Link Log End --");
            } else {
                Logger.error(TAG, "Failed to link '" + name + "' " + " shader program.");
            }
        }

        return programId;
    }

    /**
     * Create a shader of specified type. This created a shader ID, uploads the source code and then
     * compiles it. The shader source code must be written in the GLSL programming language. This
     * method should not be mistaken for the 'createProgram' method. This method doesn't create a
     * shader program, it creates an individual shader such as a vertex shader or fragment shader.
     * To create a shader program, use the 'createProgram' method.
     *
     * @param name          Shader name string for logging
     * @param type          The type of shader as specified in OpenGL ES. For example:
     *                      GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
     * @param shaderCode    String containing the shader code. Must be written in GLSL shader
     *                      programming language.
     * @return              Return
     * @see                 #createProgram(String, String, String)
     * @see                 android.opengl.GLES20
     * @since               1.0
     */
    private static int createShader(String name, int type, String shaderCode)
    {
        // Create a shader of the specified type and get the ID
        final int shader = glCreateShader(type);

        // Check if the shader ID is invalid
        if (shader == 0)
        {
            // Failed to generate shader object
            Logger.error(TAG, "Failed to generate " + typeToString(type) + " shader object");
        }

        // Upload the source to the shader
        glShaderSource(shader, shaderCode);

        // Compile that shader object
        glCompileShader(shader);

        // Check compilation
        if(!isCompiled(shader))
        {
            final String compilationLog = glGetShaderInfoLog(shader);
            if(!compilationLog.isEmpty()) {
                Logger.error(TAG, "Failed to compile '" + name + "' " + typeToString(type) +
                        " shader program. \n-- Shader Compilation Log Start --\n" + compilationLog +
                        "\n-- Shader Compilation Log End --");
            } else {
                Logger.error(TAG, "Failed to compile '" + name + "' " + typeToString(type) +
                        " shader program.");
            }
        }

        return shader;
    }

    /**
     * Get the compilation status of a shader via its ID.
     *
     * @param shaderID  ID of the shader
     * @return          True if the shader is compiled, false if not.
     * @since           1.0
     */
    private static boolean isCompiled(int shaderID)
    {
        // Place to store the shader compilation status
        final int[] SHADER_COMPILATION_STATUS = new int[1];

        // Get the shader compilation status
        glGetShaderiv(shaderID, GL_COMPILE_STATUS, SHADER_COMPILATION_STATUS, 0);

        // Return true if shader compilation successful
        return SHADER_COMPILATION_STATUS[0] == GL_TRUE;
    }

    /**
     * Get the link status of a shader program
     *
     * @param programID ID of the shader program
     * @return          True if the program is linked, false if not.
     * @since           1.0
     */
    private static boolean isLinked(int programID)
    {
        // Place to store the program link status
        final int[] PROGRAM_LINK_STATUS = new int[1];

        // Get the program link status
        glGetProgramiv(programID, GL_LINK_STATUS, PROGRAM_LINK_STATUS, 0);

        // Return true if program link was successful
        return PROGRAM_LINK_STATUS[0] == GL_TRUE;
    }

    /**
     * Resolve the name of a type of shader
     *
     * @param type  The type of shader as specified in OpenGL ES. For example:
     *              GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
     * @see         android.opengl.GLES30
     * @return      Returns string of shader type specified
     * @since       1.0
     */
    private static String typeToString(int type)
    {
        // Determine the type and return the associated string
        switch (type)
        {
            case GL_VERTEX_SHADER:
                return "GL_VERTEX_SHADER";
            case GL_FRAGMENT_SHADER:
                return "GL_FRAGMENT_SHADER";
            default:
                return "UNDEFINED";
        }
    }
}