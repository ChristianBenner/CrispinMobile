package com.crispin.crispinmobile.Rendering.Utilities;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Utilities.TextureCache;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

/**
 * The material class is designed to hold rendering information that can be used on objects.
 * Rendering information such as colour, texture, direction maps and UV multiplier. This allows you
 * to configure how rendered objects appear.
 *
 * @author      Christian Benner
 * @see         RenderObject
 * @see         Texture
 * @see         Vec4
 * @version     %I%, %G%
 * @since       1.0
 */
public class Material
{
    // todo: Add a texture for the normal map. This will be used eventually by lighting shaders

    // Tag used in logging output
    private static final String TAG = "Material";

    // Default uv multiplier
    private static final Vec2 DEFAULT_UV_MULTIPLIER = new Vec2();

    // Default uv offset
    private static final Vec2 DEFAULT_UV_OFFSET = new Vec2();

    // Default colour
    private static final Vec4 DEFAULT_COLOUR = Colour.WHITE;

    // Default shininess
    public static final float DEFAULT_SHININESS = 32.0f;

    // Flag position for ignoring position data
    public static final int IGNORE_POSITION_DATA_FLAG = 1;

    // Flag position for ignoring texel texel data
    public static final int IGNORE_TEXEL_DATA_FLAG = 2;

    // Flag position for ignoring colour data
    public static final int IGNORE_COLOUR_DATA_FLAG = 4;

    // Flag position for ignoring normal data
    public static final int IGNORE_NORMAL_DATA_FLAG = 8;

    // The material texture
    public Texture texture;

    // The specular map texture
    public Texture specularMap;

    // The normal map texture
    public Texture normalMap;

    // The UV multiplier for the texture
    public Vec2 uvMultiplier;

    // UV offset for the texture
    public Vec2 uvOffset;

    // The colour
    public Vec4 colour;

    // Strength of the ambient lighting
    public Vec3 ambientStrength;

    // Strength of the diffuse lighting
    public Vec3 diffuseStrength;

    // Strength of the specular/reflective lighting
    public Vec3 specularStrength;

    // How shiny the object appears when lit
    public float shininess;

    // Ignore position data in rendering
    private boolean ignorePositionData;

    // Ignore texel data in rendering
    private boolean ignoreTexelData;

    // Ignore colour data in rendering
    private boolean ignoreColourData;

    // Ignore normal data in rendering
    private boolean ignoreNormalData;

    /**
     * Construct the material object. Includes all of the material data elements. The constructor
     * sets the texture, uv multiplier and colour of the material.
     *
     * @param texture       A texture to apply over the render object
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Texture
     * @see                 Vec2
     * @see                 Vec4
     * @since               1.0
     */
    public Material(Texture texture,
                    Vec2 uvMultiplier,
                    Vec2 uvOffset,
                    Vec4 colour)
    {
        this.uvMultiplier = new Vec2();
        this.uvOffset = new Vec2();
        this.colour = new Vec4();
        this.ambientStrength = new Vec3();
        this.diffuseStrength = new Vec3();
        this.specularStrength = new Vec3();
        this.shininess = DEFAULT_SHININESS;
        this.ignorePositionData = false;
        this.ignoreTexelData = false;
        this.ignoreColourData = false;
        this.ignoreNormalData = false;
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
     * @param texture       A texture to apply over the render object
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Texture
     * @see                 Vec4
     * @since               1.0
     */
    public Material(Texture texture, Vec4 colour)
    {
        this(texture,
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_UV_OFFSET,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the texture and uv multiplier.
     *
     * @param texture       A texture to apply over the render object
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @see                 Texture
     * @see                 Vec2
     * @since               1.0
     */
    public Material(Texture texture, Vec2 uvMultiplier)
    {
        this(texture, uvMultiplier, DEFAULT_UV_OFFSET, DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier and colour of the
     * material.
     *
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @param colour        A colour to apply uniformly over the render object
     * @see                 Vec2
     * @see                 Vec4
     * @since               1.0
     */
    public Material(Vec2 uvMultiplier, Vec4 colour)
    {
        this(null,
                uvMultiplier,
                DEFAULT_UV_OFFSET,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the texture of the material.
     *
     * @param texture       A texture to apply over the render object
     * @see                 Texture
     * @since               1.0
     */
    public Material(Texture texture)
    {
        this(texture,
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_UV_OFFSET,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. The constructor sets the texture of the material.
     *
     * @param resourceId    The resource ID for the texture to load
     * @see                 Texture
     * @since               1.0
     */
    public Material(int resourceId)
    {
        this(TextureCache.loadTexture(resourceId),
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_UV_OFFSET,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. The constructor sets the colour of the material.
     *
     * @param colour        A texture to apply over the render object
     * @see                 Texture
     * @since               1.0
     */
    public Material(Vec4 colour)
    {
        this(null,
                DEFAULT_UV_MULTIPLIER,
                DEFAULT_UV_OFFSET,
                colour);
    }

    /**
     * Construct the material object. The constructor sets the uv multiplier of the material.
     *
     * @param uvMultiplier  The amount to multiply the texture UV co-ordinates (can be used for
     *                      tiling)
     * @see                 Vec2
     * @since               1.0
     */
    public Material(Vec2 uvMultiplier)
    {
        this(null,
                uvMultiplier,
                DEFAULT_UV_OFFSET,
                DEFAULT_COLOUR);
    }

    /**
     * Construct the material object. Sets up the material data elements with the default values
     *
     * @since 1.0
     */
    public Material()
    {
        this(null, DEFAULT_UV_MULTIPLIER, DEFAULT_UV_OFFSET, DEFAULT_COLOUR);
    }

    /**
     * Specify what data elements should be ignored by the renderer. Elements ignored by the
     * renderer may prevent some material preferences from being considered e.g. not being able to
     * display a texture because the texture co-ordinate data is ignored
     *
     * @param dataFlags The flags for the data elements that should be ignored
     * @since           1.0
     */
    public void ignoreData(final int dataFlags)
    {
        // Check if the data flag contains ignore position data flag
        if((dataFlags & Material.IGNORE_POSITION_DATA_FLAG) == Material.IGNORE_POSITION_DATA_FLAG)
        {
            setIgnorePositionData(true);
        }

        // Check if the data flag matches the ignore texel data flag
        if((dataFlags & Material.IGNORE_TEXEL_DATA_FLAG) == Material.IGNORE_TEXEL_DATA_FLAG)
        {
            setIgnoreTexelData(true);
        }

        // Check if the data flag matches the ignore colour data flag
        if((dataFlags & Material.IGNORE_COLOUR_DATA_FLAG) == Material.IGNORE_COLOUR_DATA_FLAG)
        {
            setIgnoreColourData(true);
        }

        // Check if the data flag matches the ignore normal data flag
        if((dataFlags & Material.IGNORE_NORMAL_DATA_FLAG) == Material.IGNORE_NORMAL_DATA_FLAG)
        {
            setIgnoreNormalData(true);
        }
    }

    /**
     * Set the state of ignoring position data
     *
     * @param state The state of ignoring position data
     * @since       1.0
     */
    public void setIgnorePositionData(boolean state)
    {
        ignorePositionData = state;
    }

    /**
     * Get the state of ignoring position data
     *
     * @return  A boolean of the state of ignoring position data
     * @since   1.0
     */
    public boolean isIgnoringPositionData()
    {
        return ignorePositionData;
    }

    /**
     * Set the state of ignoring texel data
     *
     * @param state The state of ignoring texel data
     * @since       1.0
     */
    public void setIgnoreTexelData(boolean state)
    {
        ignoreTexelData = state;
    }

    /**
     * Get the state of ignoring texel data
     *
     * @return  A boolean of the state of ignoring texel data
     * @since   1.0
     */
    public boolean isIgnoringTexelData()
    {
        return ignoreTexelData;
    }

    /**
     * Set the state of ignoring colour data
     *
     * @param state The state of ignoring colour data
     * @since       1.0
     */
    public void setIgnoreColourData(boolean state)
    {
        ignoreColourData = state;
    }

    /**
     * Get the state of ignoring colour data
     *
     * @return  A boolean of the state of ignoring colour data
     * @since   1.0
     */
    public boolean isIgnoringColourData()
    {
        return ignoreColourData;
    }

    /**
     * Set the state of ignoring normal data
     *
     * @param state The state of ignoring normal data
     * @since       1.0
     */
    public void setIgnoreNormalData(boolean state)
    {
        ignoreNormalData = state;
    }

    /**
     * Get the state of ignoring normal data
     *
     * @return  A boolean of the state of ignoring normal data
     * @since   1.0
     */
    public boolean isIgnoringNormalData()
    {
        return ignoreNormalData;
    }

    /**
     * Set the UV co-ordinate multiplier. The UV co-ordinate multiplier takes the current texel data
     * UV co-ordinates and multiplies them by the stated amount. This can be used in scaling
     * multiplying or scaling textures on an object face.
     *
     * @param sMultiplier   The new multiplier for the S co-ordinate
     * @param tMultiplier   The new multiplier for the T co-ordinate
     * @since               1.0
     */
    public void setUvMultiplier(float sMultiplier, float tMultiplier)
    {
        this.uvMultiplier.x = sMultiplier;
        this.uvMultiplier.y = tMultiplier;
    }

    /**
     * Set the UV co-ordinate multiplier. The UV co-ordinate multiplier takes the current texel data
     * UV co-ordinates and multiplies them by the stated amount. This can be used in scaling
     * multiplying or scaling textures on an object face.
     *
     * @param uvMultiplier  The new UV co-ordinate multiplier
     * @since               1.0
     */
    public void setUvMultiplier(Vec2 uvMultiplier)
    {
        this.uvMultiplier.x = uvMultiplier.x;
        this.uvMultiplier.y = uvMultiplier.y;
    }

    /**
     * Get the UV co-ordinate multiplier
     *
     * @return  Vec2 of the UV multiplier
     * @see     Vec2
     * @since   1.0
     */
    public Vec2 getUvMultiplier()
    {
        return this.uvMultiplier;
    }

    /**
     * Set the UV co-ordinate offset. The UV co-ordinate offsets takes the current texel data
     * UV co-ordinates and offsets them by the stated amount. This can be used to only display a
     * specific portion of a texture.
     *
     * @param sOffset   The new offset for the S co-ordinate
     * @param tOffset   The new offset for the T co-ordinate
     * @since           1.0
     */
    public void setUvOffset(float sOffset, float tOffset)
    {
        this.uvOffset.x = sOffset;
        this.uvOffset.y = tOffset;
    }

    /**
     * Set the UV co-ordinate offset. The UV co-ordinate offsets takes the current texel data
     * UV co-ordinates and offsets them by the stated amount. This can be used to only display a
     * specific portion of a texture.
     *
     * @param uvOffset  The new UV co-ordinate offset
     * @since           1.0
     */
    public void setUvOffset(Vec2 uvOffset)
    {
        this.uvOffset.x = uvOffset.x;
        this.uvOffset.y = uvOffset.y;
    }

    /**
     * Get the UV co-ordinate offset
     *
     * @return  Vec2 of the UV offset
     * @see     Vec2
     * @since   1.0
     */
    public Vec2 getUvOffset()
    {
        return this.uvOffset;
    }

    /**
     * Set the texture attached to the material
     *
     * @param texture   The texture of the material
     * @see             Texture
     * @since           1.0
     */
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    /**
     * Get the texture object attached to the material
     *
     * @return  The texture object attached to the material
     * @see     Texture
     * @since   1.0
     */
    public Texture getTexture()
    {
        return this.texture;
    }

    /**
     * Check if the material has a texture attached.
     *
     * @return  True if the material has a texture, else false
     * @since   1.0
     */
    public boolean hasTexture()
    {
        return texture != null;
    }

    /**
     * Set the specular map attached to the material
     *
     * @param texture   The specular map of the material
     * @see             Texture
     * @since           1.0
     */
    public void setSpecularMap(final Texture texture)
    {
        this.specularMap = texture;
    }

    /**
     * Get the specular map object attached to the material
     *
     * @return  The specular map object attached to the material
     * @see     Texture
     * @since   1.0
     */
    public Texture getSpecularMap()
    {
        return this.specularMap;
    }

    /**
     * Check if the material has a specular map attached.
     *
     * @return  True if the material has a specular map, else false
     * @since   1.0
     */
    public boolean hasSpecularMap()
    {
        return specularMap != null;
    }

    /**
     * Set the normal map attached to the material
     *
     * @param texture   The normal map of the material
     * @see             Texture
     * @since           1.0
     */
    public void setNormalMap(Texture texture)
    {
        this.normalMap = texture;
    }

    /**
     * Get the normal map object attached to the material
     *
     * @return  The normal map object attached to the material
     * @see     Texture
     * @since   1.0
     */
    public Texture getNormalMap()
    {
        return this.normalMap;
    }

    /**
     * Check if the material has a normal map attached.
     *
     * @return  True if the material has a normal map, else false
     * @since   1.0
     */
    public boolean hasNormalMap()
    {
        return normalMap != null;
    }

    /**
     * Set the uniform colour of the material. Uniform colour is applied across the entirety of the
     * object that the material is attached to. If the object also has per-attribute colour data,
     * the two colours will be blended.
     *
     * @param colour    The uniform colour of the material
     * @see             Vec4
     * @since           1.0
     */
    public void setColour(final Vec4 colour)
    {
        this.colour.x = colour.x;
        this.colour.y = colour.y;
        this.colour.z = colour.z;
        this.colour.w = colour.w;
    }
}
