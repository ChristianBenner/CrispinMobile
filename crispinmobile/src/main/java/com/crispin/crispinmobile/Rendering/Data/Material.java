package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.TextureCache;

/**
 * The material class is designed to hold rendering information that can be used on objects.
 * Rendering information such as colour, texture, direction maps and UV multiplier. This allows you
 * to configure how rendered objects appear.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @see Texture
 * @see Colour
 * @since 1.0
 */
public class Material {
    // Tag used in logging output
    private static final String TAG = "Material";

    // Default shininess
    public static final float DEFAULT_SHININESS = 32.0f;

    // Flag position for ignoring position data
    public static final int IGNORE_POSITION_DATA_FLAG = 1;

    // Flag position for ignoring texel texel data
    public static final int IGNORE_TEXEL_DATA_FLAG = 2;

    // Flag position for ignoring normal data
    public static final int IGNORE_NORMAL_DATA_FLAG = 4;

    // Default uv multiplier
    private static final Scale2D DEFAULT_UV_MULTIPLIER = new Scale2D();

    // Default uv offset
    private static final Vec2 DEFAULT_UV_OFFSET = new Vec2();

    // The material texture
    public Texture texture;

    // The diffuse map texture
    public Texture diffuseMap;

    // The specular map texture
    public Texture specularMap;

    // The normal map texture
    public Texture normalMap;

    // The UV multiplier for the texture
    public Scale2D uvMultiplier;

    // UV offset for the texture
    public Vec2 uvOffset;

    // The colour
    public Colour colour;

    // Strength of the ambient lighting
    public Colour ambientStrength;

    // Strength of the diffuse lighting
    public Colour diffuseStrength;

    // Strength of the specular/reflective lighting
    public Colour specularStrength;

    // How shiny the object appears when lit
    public float shininess;

    // Ignore position data in rendering
    private boolean ignorePositionData;

    // Ignore texel data in rendering
    private boolean ignoreTexelData;

    // Ignore normal data in rendering
    private boolean ignoreNormalData;

    /**
     * Construct the material object. Includes all of the material data elements. The constructor
     * sets the texture, uv multiplier and colour of the material.
     *
     * @param texture      A texture to apply over the render object
     * @param uvMultiplier The amount to multiply the texture UV co-ordinates (can be used for
     *                     tiling)
     * @param colour       A colour to apply uniformly over the render object
     * @see Texture
     * @see Scale2D
     * @see Colour
     * @since 1.0
     */
    public Material(Texture texture, Scale2D uvMultiplier, Vec2 uvOffset, Colour colour) {
        this.uvMultiplier = new Scale2D();
        this.uvOffset = new Vec2();
        this.colour = new Colour();
        this.ambientStrength = new Colour();
        this.diffuseStrength = new Colour();
        this.specularStrength = new Colour();
        this.shininess = DEFAULT_SHININESS;
        this.ignorePositionData = false;
        this.ignoreTexelData = false;
        this.ignoreNormalData = false;
        this.diffuseMap = null;
        this.specularMap = null;
        this.normalMap = null;
        setTexture(texture);
        setUvMultiplier(uvMultiplier);
        setUvOffset(uvOffset);
        setColour(colour);
    }

    /**
     * Construct the material object. The constructor sets the texture and colour of the material.
     *
     * @param texture A texture to apply over the render object
     * @param colour  A colour to apply uniformly over the render object
     * @see Texture
     * @see Colour
     * @since 1.0
     */
    public Material(Texture texture, Colour colour) {
        this(texture, DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET, colour);
    }

    /**
     * Construct the material object. The constructor sets the texture and uv multiplier.
     *
     * @param texture      A texture to apply over the render object
     * @param uvMultiplier The amount to multiply the texture UV co-ordinates (can be used for
     *                     tiling)
     * @see Texture
     * @see Scale2D
     * @since 1.0
     */
    public Material(Texture texture, Scale2D uvMultiplier) {
        this(texture, uvMultiplier, DEFAULT_UV_OFFSET, new Colour());
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier and colour of the
     * material.
     *
     * @param uvMultiplier The amount to multiply the texture UV co-ordinates (can be used for
     *                     tiling)
     * @param colour       A colour to apply uniformly over the render object
     * @see Scale2D
     * @see Colour
     * @since 1.0
     */
    public Material(Scale2D uvMultiplier, Colour colour) {
        this(null, uvMultiplier, DEFAULT_UV_OFFSET, colour);
    }

    /**
     * Construct the material object. The constructor sets the texture of the material.
     *
     * @param texture A texture to apply over the render object
     * @see Texture
     * @since 1.0
     */
    public Material(Texture texture) {
        this(texture, DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET, new Colour());
    }

    /**
     * Construct the material object. The constructor sets the texture of the material.
     *
     * @param resourceId The resource ID for the texture to load
     * @see Texture
     * @since 1.0
     */
    public Material(int resourceId) {
        this(TextureCache.loadTexture(resourceId), DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET,
                new Colour());
    }

    /**
     * Construct the material object. The constructor sets the colour of the material.
     *
     * @param colour A texture to apply over the render object
     * @see Texture
     * @since 1.0
     */
    public Material(Colour colour) {
        this(null, DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET, colour);
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier of the material.
     *
     * @param uvMultiplier The amount to multiply the texture UV co-ordinates (can be used for
     *                     tiling)
     * @see Scale2D
     * @since 1.0
     */
    public Material(Scale2D uvMultiplier) {
        this(null, uvMultiplier, DEFAULT_UV_OFFSET, new Colour());
    }

    /**
     * Construct the material object. Sets up the material data elements with the default values
     *
     * @since 1.0
     */
    public Material() {
        this(null, DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET, new Colour());
    }

    /**
     * Specify what data elements should be ignored by the renderer. Elements ignored by the
     * renderer may prevent some material preferences from being considered e.g. not being able to
     * display a texture because the texture co-ordinate data is ignored
     *
     * @param dataFlags The flags for the data elements that should be ignored
     * @since 1.0
     */
    public void ignoreData(final int dataFlags) {
        // Check if the data flag contains ignore position data flag
        if ((dataFlags & Material.IGNORE_POSITION_DATA_FLAG) == Material.IGNORE_POSITION_DATA_FLAG) {
            setIgnorePositionData(true);
        }

        // Check if the data flag matches the ignore texel data flag
        if ((dataFlags & Material.IGNORE_TEXEL_DATA_FLAG) == Material.IGNORE_TEXEL_DATA_FLAG) {
            setIgnoreTexelData(true);
        }

        // Check if the data flag matches the ignore normal data flag
        if ((dataFlags & Material.IGNORE_NORMAL_DATA_FLAG) == Material.IGNORE_NORMAL_DATA_FLAG) {
            setIgnoreNormalData(true);
        }
    }

    /**
     * Set the state of ignoring position data
     *
     * @param state The state of ignoring position data
     * @since 1.0
     */
    public void setIgnorePositionData(boolean state) {
        ignorePositionData = state;
    }

    /**
     * Get the state of ignoring position data
     *
     * @return A boolean of the state of ignoring position data
     * @since 1.0
     */
    public boolean isIgnoringPositionData() {
        return ignorePositionData;
    }

    /**
     * Set the state of ignoring texel data
     *
     * @param state The state of ignoring texel data
     * @since 1.0
     */
    public void setIgnoreTexelData(boolean state) {
        ignoreTexelData = state;
    }

    /**
     * Get the state of ignoring texel data
     *
     * @return A boolean of the state of ignoring texel data
     * @since 1.0
     */
    public boolean isIgnoringTexelData() {
        return ignoreTexelData;
    }

    /**
     * Set the state of ignoring normal data
     *
     * @param state The state of ignoring normal data
     * @since 1.0
     */
    public void setIgnoreNormalData(boolean state) {
        ignoreNormalData = state;
    }

    /**
     * Get the state of ignoring normal data
     *
     * @return A boolean of the state of ignoring normal data
     * @since 1.0
     */
    public boolean isIgnoringNormalData() {
        return ignoreNormalData;
    }

    /**
     * Set the UV co-ordinate multiplier. The UV co-ordinate multiplier takes the current texel data
     * UV co-ordinates and multiplies them by the stated amount. This can be used in scaling
     * multiplying or scaling textures on an object face.
     *
     * @param sMultiplier The new multiplier for the S co-ordinate
     * @param tMultiplier The new multiplier for the T co-ordinate
     * @since 1.0
     */
    public void setUvMultiplier(float sMultiplier, float tMultiplier) {
        this.uvMultiplier.w = sMultiplier;
        this.uvMultiplier.h = tMultiplier;
    }

    /**
     * Get the UV co-ordinate multiplier
     *
     * @return Scale2D of the UV multiplier
     * @see Scale2D
     * @since 1.0
     */
    public Scale2D getUvMultiplier() {
        return this.uvMultiplier;
    }

    /**
     * Set the UV co-ordinate multiplier. The UV co-ordinate multiplier takes the current texel data
     * UV co-ordinates and multiplies them by the stated amount. This can be used in scaling
     * multiplying or scaling textures on an object face.
     *
     * @param uvMultiplier The new UV co-ordinate multiplier
     * @since 1.0
     */
    public void setUvMultiplier(Scale2D uvMultiplier) {
        this.uvMultiplier.w = uvMultiplier.w;
        this.uvMultiplier.h = uvMultiplier.h;
    }

    /**
     * Set the UV co-ordinate offset. The UV co-ordinate offsets takes the current texel data
     * UV co-ordinates and offsets them by the stated amount. This can be used to only display a
     * specific portion of a texture.
     *
     * @param sOffset The new offset for the S co-ordinate
     * @param tOffset The new offset for the T co-ordinate
     * @since 1.0
     */
    public void setUvOffset(float sOffset, float tOffset) {
        this.uvOffset.x = sOffset;
        this.uvOffset.y = tOffset;
    }

    /**
     * Get the UV co-ordinate offset
     *
     * @return Vec2 of the UV offset
     * @see Scale2D
     * @since 1.0
     */
    public Vec2 getUvOffset() {
        return this.uvOffset;
    }

    /**
     * Set the UV co-ordinate offset. The UV co-ordinate offsets takes the current texel data
     * UV co-ordinates and offsets them by the stated amount. This can be used to only display a
     * specific portion of a texture.
     *
     * @param uvOffset The new UV co-ordinate offset
     * @since 1.0
     */
    public void setUvOffset(Vec2 uvOffset) {
        this.uvOffset.x = uvOffset.x;
        this.uvOffset.y = uvOffset.y;
    }

    /**
     * Get the texture object attached to the material
     *
     * @return The texture object attached to the material
     * @see Texture
     * @since 1.0
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Set the texture attached to the material
     *
     * @param texture The texture of the material
     * @see Texture
     * @since 1.0
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Check if the material has a texture attached.
     *
     * @return True if the material has a texture, else false
     * @since 1.0
     */
    public boolean hasTexture() {
        return texture != null;
    }

    /**
     * Get the specular map object attached to the material
     *
     * @return The specular map object attached to the material
     * @see Texture
     * @since 1.0
     */
    public Texture getSpecularMap() {
        return this.specularMap;
    }

    /**
     * Set the diffuse map attached to the material
     *
     * @param texture The diffuse map of the material
     * @see Texture
     * @since 1.0
     */
    public void setDiffuseMap(final Texture texture) {
        this.diffuseMap = texture;
    }

    /**
     * Check if the material has a diffuse map attached.
     *
     * @return True if the material has a diffuse map, else false
     * @since 1.0
     */
    public boolean hasDiffuseMap() {
        return diffuseMap != null;
    }

    /**
     * Set the specular map attached to the material
     *
     * @param texture The specular map of the material
     * @see Texture
     * @since 1.0
     */
    public void setSpecularMap(final Texture texture) {
        this.specularMap = texture;
    }

    /**
     * Check if the material has a specular map attached.
     *
     * @return True if the material has a specular map, else false
     * @since 1.0
     */
    public boolean hasSpecularMap() {
        return specularMap != null;
    }

    /**
     * Get the normal map object attached to the material
     *
     * @return The normal map object attached to the material
     * @see Texture
     * @since 1.0
     */
    public Texture getNormalMap() {
        return this.normalMap;
    }

    /**
     * Set the normal map attached to the material
     *
     * @param texture The normal map of the material
     * @see Texture
     * @since 1.0
     */
    public void setNormalMap(Texture texture) {
        this.normalMap = texture;
    }

    /**
     * Check if the material has a normal map attached.
     *
     * @return True if the material has a normal map, else false
     * @since 1.0
     */
    public boolean hasNormalMap() {
        return normalMap != null;
    }

    /**
     * Set the uniform colour of the material. Uniform colour is applied across the entirety of the
     * object that the material is attached to. If the object also has per-attribute colour data,
     * the two colours will be blended.
     *
     * @param colour The uniform colour of the material
     * @see Colour
     * @since 1.0
     */
    public void setColour(final Colour colour) {
        this.colour.red = colour.red;
        this.colour.green = colour.green;
        this.colour.blue = colour.blue;
        this.colour.alpha = colour.alpha;
    }
}
