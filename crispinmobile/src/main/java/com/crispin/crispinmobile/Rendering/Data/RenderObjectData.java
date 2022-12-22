package com.crispin.crispinmobile.Rendering.Data;

import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.Logger;

import java.util.ArrayList;


/**
 * RenderObjectData is a class designed to build vertex data based on position, texel, normal and
 * face data. This makes it suitable for building vertex data for the GPU from OBJ data formats.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class RenderObjectData {
    // Tag used for logging
    private static final String TAG = "RenderObjectData";

    // Start index offset
    private static final int START_INDEX_OFFSET = 1;

    // The face data index offset
    private static final int FACE_DATA_INDEX_OFFSET = -1;

    // The number of data elements in position only face data
    private static final int NUM_DATA_ELEMENTS_POSITION_ONLY = 1;

    // The number of data elements in position and normal face data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_NORMAL = 2;

    // The number of data elements in position and texel data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_TEXEL = 2;

    // The number of data elements in position, texel and normal data
    private static final int NUM_DATA_ELEMENTS_POSITION_AND_TEXEL_AND_NORMAL = 3;

    // The start index for the position data
    private static final int POSITION_START_INDEX = 0;

    // Represent a data start index as unused
    private static final int UNUSED_DATA_ELEMENT = -1;

    // The number of components in XY data format
    private static final int NUM_COMPONENTS_XY = 2;

    // The number of components in XYZ data format
    private static final int NUM_COMPONENTS_XYZ = 3;

    // The number of components in XYZW data format
    private static final int NUM_COMPONENTS_XYZW = 4;

    // The number of components in ST data format
    private static final int NUM_COMPONENTS_ST = 2;

    // The number -1 signals that the attribute is invalid or doesn't associate to any data
    private static final int INVALID_ATTRIBUTE_INDEX = -1;

    // Array holding the position vertex data
    private final ArrayList<Float> positionDataArray;

    // Array holding the texel vertex data
    private final ArrayList<Float> texelDataArray;

    // Array holding the normal vertex data
    private final ArrayList<Float> normalDataArray;

    // Array holding the face data
    private final ArrayList<Integer> faceDataArray;

    // The type of face data that is to be loaded
    private FaceData faceData;

    // The render method that has been determined
    private Mesh.RenderMethod renderMethod;

    // The data stride
    private int dataStride;

    // The start index of the position data
    private int positionStartIndex;

    // The start index of the texel data
    private int texelStartIndex;

    // The start index of the normal data
    private int normalStartIndex;

    // Number of position components
    private byte numberOfPositionComponents;

    // Number of normal components
    private byte numberOfNormalComponents;

    // Number of texel components
    private byte numberOfTexelComponents;

    /**
     * Construct the RenderObjectData object
     *
     * @since 1.0
     */
    public RenderObjectData() {
        positionDataArray = new ArrayList<>();
        texelDataArray = new ArrayList<>();
        normalDataArray = new ArrayList<>();
        faceDataArray = new ArrayList<>();
        faceData = FaceData.NONE;
        renderMethod = Mesh.RenderMethod.NONE;
        positionStartIndex = UNUSED_DATA_ELEMENT;
        texelStartIndex = UNUSED_DATA_ELEMENT;
        normalStartIndex = UNUSED_DATA_ELEMENT;
        dataStride = 0;
        numberOfPositionComponents = NUM_COMPONENTS_XYZ;
        numberOfNormalComponents = NUM_COMPONENTS_XYZ;
        numberOfTexelComponents = NUM_COMPONENTS_ST;
    }

    /**
     * Set the number of position components. This is the number of dimensions in a position vertex.
     * For example XYZ data contains three components and XY contains two.
     *
     * @param numPositionComponents The number of position components in a vertex
     * @since 1.0
     */
    public void setNumPositionComponents(byte numPositionComponents) {
        this.numberOfPositionComponents = numPositionComponents;
    }

    /**
     * Set the number of normal components. This is the number of dimensions in a normal vertex.
     * For example XYZ data contains three components and XY contains two.
     *
     * @param numNormalComponents The number of normal components in a vertex
     * @since 1.0
     */
    public void setNumNormalComponents(byte numNormalComponents) {
        this.numberOfNormalComponents = numNormalComponents;
    }

    /**
     * Set the number of texel components. This is the number of dimensions in a texel vertex.
     * For example ST data contains two components.
     *
     * @param numTexelComponents The number of texel components in a vertex
     * @since 1.0
     */
    public void setNumTexelComponents(byte numTexelComponents) {
        this.numberOfTexelComponents = numTexelComponents;
    }

    /**
     * Set the render method type
     *
     * @param renderMethod The new render method to set
     * @return True if render method changed successfully, else false. The function will return
     * false if the data has already been assigned a render method
     * @since 1.0
     */
    public boolean setRenderMethod(Mesh.RenderMethod renderMethod) {
        // Check if the render method has been set yet
        if (this.renderMethod == Mesh.RenderMethod.NONE) {
            this.renderMethod = renderMethod;
            return true;
        } else if (this.renderMethod == renderMethod) {
            return true;
        } else {
            Logger.error(TAG, "RenderObjectData already has a different Render Method type");
            return false;
        }
    }

    /**
     * Set the face data type
     *
     * @param faceDataType The new face data type
     * @return True if the face data type has been set successfully, else false. The function will
     * return false if the data has already been assigned a face data type
     * @since 1.0
     */
    public boolean setFaceDataType(FaceData faceDataType) {
        // Check if the face data has been set yet
        if (this.faceData == FaceData.NONE) {
            this.faceData = faceDataType;

            // Determine the data start index for different face data types
            switch (faceDataType) {
                case POSITION_ONLY:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_ONLY;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case POSITION_AND_NORMAL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_NORMAL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = UNUSED_DATA_ELEMENT;
                    normalStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    break;
                case POSITION_AND_TEXEL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_TEXEL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    normalStartIndex = UNUSED_DATA_ELEMENT;
                    break;
                case POSITION_AND_TEXEL_AND_NORMAL:
                    dataStride = NUM_DATA_ELEMENTS_POSITION_AND_TEXEL_AND_NORMAL;
                    positionStartIndex = POSITION_START_INDEX;
                    texelStartIndex = positionStartIndex + START_INDEX_OFFSET;
                    normalStartIndex = texelStartIndex + START_INDEX_OFFSET;
                    break;
            }

            return true;
        } else if (this.faceData == faceDataType) {
            return true;
        } else {
            Logger.error(TAG, "RenderObjectData already has a different FaceData type");
            return false;
        }
    }

    /**
     * Add some position data to be processed later
     *
     * @param vertexData The position data as a float
     * @since 1.0
     */
    public void addPositionData(float vertexData) {
        positionDataArray.add(vertexData);
    }

    /**
     * Add some texel data to be processed later
     *
     * @param texelData The texel data as a float
     * @since 1.0
     */
    public void addTexelData(float texelData) {
        texelDataArray.add(texelData);
    }

    /**
     * Add some normal data to be processed later
     *
     * @param normalData The normal data as a float
     * @since 1.0
     */
    public void addNormalData(float normalData) {
        normalDataArray.add(normalData);
    }

    /**
     * Add some face data to be processed later
     *
     * @param faceData The face data as a float
     * @since 1.0
     */
    public void addFaceData(int faceData) {
        faceDataArray.add(faceData);
    }

    /**
     * Process the data. The function produces the vertex data based on positional, texel and normal
     * face data.
     *
     * @return A RenderObject built from the processed data
     * @since 1.0
     */
    public Model processData() {
        final int NUMBER_OF_FACE_DATA = faceDataArray.size() / dataStride;

        final int NUMBER_OF_POSITION_ELEMENTS = numberOfPositionComponents;
        final int POSITION_BUFFER_SIZE = NUMBER_OF_POSITION_ELEMENTS * NUMBER_OF_FACE_DATA;

        final int NUMBER_OF_TEXEL_ELEMENTS = numberOfTexelComponents;
        final int TEXEL_BUFFER_SIZE = texelStartIndex == INVALID_ATTRIBUTE_INDEX ? 0 :
                NUMBER_OF_TEXEL_ELEMENTS * NUMBER_OF_FACE_DATA;

        final int NUMBER_OF_NORMAL_ELEMENTS = numberOfNormalComponents;
        final int NORMAL_BUFFER_SIZE = normalStartIndex == INVALID_ATTRIBUTE_INDEX ? 0 :
                NUMBER_OF_NORMAL_ELEMENTS * NUMBER_OF_FACE_DATA;

        // Create a float buffer for all of the vertex data
        float[] vertexDataBuffer = new float[POSITION_BUFFER_SIZE + TEXEL_BUFFER_SIZE +
                NORMAL_BUFFER_SIZE];

        int vertexDataBufferIndex = 0;

        // Determine how many elements the face data includes
        int faceDataStride = 1;
        if(texelStartIndex != INVALID_ATTRIBUTE_INDEX) {
            faceDataStride++;
        }
        if(normalStartIndex != INVALID_ATTRIBUTE_INDEX) {
            faceDataStride++;
        }

        // Iterate through the face data adding each position, texel and normal data for each
        // vertex. This should result in a data structure of:
        // v0: posX, posY, posZ, texU, texV, normX, normY, normZ
        // v1: posX, posY, posZ, texU, texV, normX, normY, normZ
        for (int fi = 0; fi < faceDataArray.size(); fi += faceDataStride) {
            // Add the vertex positions
            for (int pi = 0; pi < NUMBER_OF_POSITION_ELEMENTS; pi++) {
                vertexDataBuffer[vertexDataBufferIndex] = positionDataArray.get(((
                        faceDataArray.get(fi + positionStartIndex) + FACE_DATA_INDEX_OFFSET) *
                        NUMBER_OF_POSITION_ELEMENTS) + pi);
                vertexDataBufferIndex++;
            }

            // Add the vertex texel data
            for (int ti = 0; ti < NUMBER_OF_TEXEL_ELEMENTS; ti++) {
                vertexDataBuffer[vertexDataBufferIndex] = texelDataArray.get((((
                        faceDataArray.get(fi + texelStartIndex) + FACE_DATA_INDEX_OFFSET) *
                        NUMBER_OF_TEXEL_ELEMENTS) + ti));
                vertexDataBufferIndex++;
            }

            // Add the vertex normal data
            for (int ni = 0; ni < NUMBER_OF_NORMAL_ELEMENTS; ni++) {
                vertexDataBuffer[vertexDataBufferIndex] = normalDataArray.get((((
                        faceDataArray.get(fi + normalStartIndex) + FACE_DATA_INDEX_OFFSET) *
                        NUMBER_OF_NORMAL_ELEMENTS) + ni));
                vertexDataBufferIndex++;
            }
        }

        // Create and return a render object using the data format and the vertex data
        return new Model(vertexDataBuffer,
                Mesh.RenderMethod.TRIANGLES,
                Mesh.AttributeOrder_t.POSITION_THEN_TEXEL_THEN_NORMAL,
                numberOfPositionComponents,
                numberOfTexelComponents,
                numberOfNormalComponents);
    }

    /**
     * The type of vertex data that the face data contains.
     * POSITION_ONLY: The face data only includes positional vertex data
     * POSITION_AND_TEXEL: The face data includes positional and texel vertex data
     * POSITION_AND_NORMAL: The face data includes positional and normal vertex data
     * POSITION_AND_TEXEL_AND_NORMAL: The face data includes positional, texel and normal vertex
     * data
     * NONE: FaceData has not been determined to contain any vertex data
     *
     * @since 1.0
     */
    public enum FaceData {
        POSITION_ONLY,
        POSITION_AND_TEXEL,
        POSITION_AND_NORMAL,
        POSITION_AND_TEXEL_AND_NORMAL,
        NONE
    }

    /**
     * The position components that the positional vertex data is comprised of.
     *
     * @since 1.0
     */
    public enum PositionComponents {
        XYZW,
        XYZ,
        XY,
        NONE
    }
}
