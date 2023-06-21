package com.crispin.crispinmobile.Utilities;

import android.content.res.Resources;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Rendering.Data.RenderObjectData;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.ModelLoading.FaceIndexData;
import com.crispin.crispinmobile.Utilities.ModelLoading.MeshData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Designed to read OBJ model files and produce a RenderObject that can be drawn on a
 * scene. OBJ models loaded with the class must have triangulated faces. It also doesn't support
 * multiple OBJ models within one file. The class is comprised of static only functions.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class OBJModelLoader {
    // Tag used in logging output
    private static final String TAG = "OBJModelLoader";

    // Identifier for face data
    private static final byte FACE = 0x01;

    // Identifier for position data
    private static final byte POSITION = 0x02;

    // Identifier for texel data
    private static final byte TEXEL = 0x03;

    // Identifier for normal data
    private static final byte NORMAL = 0x04;

    // Identifier for vertex type data (e.g. position, texel and normal)
    private static final byte VERTEX = 0x05;

    // Identifier for comment
    private static final byte COMMENT = 0x06;

    // Identifier for object
    private static final byte OBJECT = 0x07;

    // Identifier for material template library
    private static final byte MATERIAL_TEMPLATE_LIBRARY = 0x08;

    // Identifier for smooth shading
    private static final byte SMOOTH_SHADING = 0x09;

    // Identifier for material usage
    private static final byte USE_MATERIAL = 0x0A;

    // Identifier for polyline
    private static final byte POLYLINE = 0x0B;

    // Identifier for undefined data type
    private static final byte NONE = 0x07;

    // ASCII value of hash-tag character (this appears before comments)
    private static final byte ASCII_HASHTAG = 0x23;

    // ASCII value of the 'f' character
    private static final byte ASCII_F = 0x66;

    // ASCII value of the 'v' character
    private static final byte ASCII_V = 0x76;

    // ASCII value of the space character
    private static final byte ASCII_SPACE = 0x20;

    // ASCII value of the 't' character
    private static final byte ASCII_T = 0x74;

    // ASCII value of the 'n' character
    private static final byte ASCII_N = 0x6E;

    // ASCII value of the '0' character
    private static final byte ASCII_0 = 0x30;

    // ASCII value of the '9' character
    private static final byte ASCII_9 = 0x39;

    // ASCII value of the '.' character
    private static final byte ASCII_POINT = 0x2E;

    // ASCII value of the '-' character
    private static final byte ASCII_MINUS = 0x2D;

    // ASCII value of the '/' character
    private static final byte ASCII_FORWARD_SLASH = 0x2F;

    // ASCII value of the new line character
    private static final byte ASCII_NEW_LINE = 0x0A;

    // ASCII value of the carriage return character
    private static final byte ASCII_CARRIAGE_RETURN = 0x0D;

    // Represent that the data subject (if there is one) hasn't been assigned a start index
    private static final int NO_START_INDEX = -1;

    // The number to divide nanoseconds by to get milliseconds
    private static final int NANOSECONDS_TO_MILLISECONDS_DIVIDE = 1000000;

    // Four face data elements in the face data
    private static final int FOUR_FACE_DATA_ELEMENTS = 4;

    // Three face data elements in the face data
    private static final int THREE_FACE_DATA_ELEMENTS = 3;

    // Two face data elements in the face data
    private static final int TWO_FACE_DATA_ELEMENTS = 2;

    // One face data element in the face data
    private static final int ONE_FACE_DATA_ELEMENT = 1;

    // Two face data separators in the face data
    private static final int TWO_FACE_DATA_SEPARATORS = 2;

    // One face data separator in the face data
    private static final int ONE_FACE_DATA_SEPARATOR = 1;

    /**
     * Read an OBJ file from a resource ID
     *
     * @param resourceId The OBJ model file resource ID
     * @return A RenderObject built from the model data. The model can be rendered to a scene
     * @see Mesh
     * @since 1.0
     */
    public static Mesh readObjFile(int resourceId) {
        // Attempt to open and read an OBJ file
        try {
            // Measure how long it takes to load load and read the model file
            long start = System.nanoTime();

            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream;
            inputStream = resources.openRawResource(resourceId);
            inputStream.reset();

            byte[] theFile = new byte[inputStream.available()];
            inputStream.read(theFile);
            Mesh ro = processObj(theFile);

            // End of time measurement
            long end = System.nanoTime();

            // Log time taken to load the OBJ model
            Logger.debug(TAG, "Model Loaded in: " + ((end - start) /
                    NANOSECONDS_TO_MILLISECONDS_DIVIDE) + "ms");

            return ro;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final int TYPE_INDEX = 0;
    private static final String TYPE_VERTEX_POSITION = "v";
    private static final String TYPE_VERTEX_NORMAL = "vn";
    private static final String TYPE_VERTEX_TEXEL = "vt";
    private static final String TYPE_COMMENT = "#";
    private static final String TYPE_NEW_OBJECT = "o";
    private static final String TYPE_SMOOTH_FACES_FLAG = "s";
    private static final String TYPE_FACE = "f";
    private static final String TYPE_DEFINE_MATERIAL_LIBRARY = "mtllib";
    private static final String TYPE_USE_MATERIAL = "usemtl";
    private static final int FACE_ELEMENT_POS_INDEX = 0;
    private static final int FACE_ELEMENT_TEXEL_INDEX = 1;
    private static final int FACE_ELEMENT_NORMAL_INDEX = 2;
    private static final int UNDEFINED_INDEX = -1;

    private static float[] getFloats(String[] split, int index) {
        if (split.length - index <= 0) {
            return new float[]{};
        }

        float[] floats = new float[split.length - index];
        for (int i = index, n = 0; i < split.length; i++, n++) {
            floats[n] = Float.parseFloat(split[i]);
        }
        return floats;
    }



    public static FaceIndexData parseFaceElements(String[] split, int index) {
        FaceIndexData face = new FaceIndexData();
        for (int i = index, n = 0; i < split.length; i++, n++) {
            String[] vertexElements = split[i].split("/");
            if(i == index) {
                face.positionIndices = new int[split.length - index];
                face.texelIndices = new int[split.length - index];
                face.normalIndices = new int[split.length - index];
            }

            if (FACE_ELEMENT_POS_INDEX < vertexElements.length && !vertexElements[FACE_ELEMENT_POS_INDEX].isEmpty()) {
                face.positionIndices[n] = Integer.parseInt(vertexElements[FACE_ELEMENT_POS_INDEX]);
            }
            if (FACE_ELEMENT_TEXEL_INDEX < vertexElements.length && !vertexElements[FACE_ELEMENT_TEXEL_INDEX].isEmpty()) {
                face.texelIndices[n] = Integer.parseInt(vertexElements[FACE_ELEMENT_TEXEL_INDEX]);
            }
            if (FACE_ELEMENT_NORMAL_INDEX < vertexElements.length && !vertexElements[FACE_ELEMENT_NORMAL_INDEX].isEmpty()) {
                face.normalIndices[n] = Integer.parseInt(vertexElements[FACE_ELEMENT_NORMAL_INDEX]);
            }
        }
        return face;
    }

    private static MeshData[] process(BufferedReader reader) throws IOException {
        class ObjectData {
            String name;
            String material;
            ArrayList<FaceIndexData> faceIndices = new ArrayList<>();
        }

        ArrayList<float[]> positionData = new ArrayList<>();
        ArrayList<float[]> texelData = new ArrayList<>();
        ArrayList<float[]> normalData = new ArrayList<>();
        ArrayList<ObjectData> objectData = new ArrayList<>();
        ObjectData temp = null;
        int positionComponentsPerVertex = 0;
        int texelComponentsPerVertex = 0;
        int normalComponentsPerVertex = 0;
        int verticesPerFace = 0;

        while (reader.ready()) {
            String[] split = reader.readLine().split(" ");
            switch (split[TYPE_INDEX]) {
                case TYPE_NEW_OBJECT:
                    temp = new ObjectData();
                    objectData.add(temp);

                    if(split.length > 1) {
                        temp.name = split[1];
                        Logger.debug(TAG, "Constructing new model from OBJ format '" + temp.name + "'");
                    } else {
                        Logger.debug(TAG, "Constructing new model from OBJ format");
                    }
                    break;
                case TYPE_VERTEX_POSITION:
                    float[] vertexPosData = getFloats(split, TYPE_INDEX + 1);
                    positionComponentsPerVertex = vertexPosData.length;
                    positionData.add(vertexPosData);
                    break;
                case TYPE_VERTEX_TEXEL:
                    float[] vertexTexelData = getFloats(split, TYPE_INDEX + 1);
                    texelComponentsPerVertex = vertexTexelData.length;
                    texelData.add(vertexTexelData);
                    break;
                case TYPE_VERTEX_NORMAL:
                    float[] vertexNormalData = getFloats(split, TYPE_INDEX + 1);
                    normalComponentsPerVertex = vertexNormalData.length;
                    normalData.add(vertexNormalData);
                    break;
                case TYPE_FACE:
                    if (temp == null) {
                        temp = new ObjectData();
                        objectData.add(temp);
                    }

                    FaceIndexData faceIndexData = parseFaceElements(split, TYPE_INDEX + 1);
                    verticesPerFace = faceIndexData.positionIndices.length;
                    temp.faceIndices.add(faceIndexData);
                    break;
                case TYPE_DEFINE_MATERIAL_LIBRARY:

                    break;
                case TYPE_USE_MATERIAL:

                    break;
                // Ignore
                case TYPE_COMMENT:
                case TYPE_SMOOTH_FACES_FLAG:
                    break;
            }
        }

        // Process objects
        MeshData[] meshes = new MeshData[objectData.size()];
        for(int o = 0; o < objectData.size(); o++) {
            ObjectData object = objectData.get(o);
            if(object.faceIndices.size() == 0) {
                continue;
            }

            int numFaces = object.faceIndices.size();
            int numVerticesPerFace = verticesPerFace;
            int positionBufferSize = positionComponentsPerVertex * numVerticesPerFace * numFaces;
            int texelBufferSize = texelComponentsPerVertex * numVerticesPerFace * numFaces;
            int normalBufferSize = normalComponentsPerVertex * numVerticesPerFace * numFaces;

            float[] positionBuffer = new float[positionBufferSize];
            float[] texelBuffer = new float[texelBufferSize];
            float[] normalBuffer = new float[normalBufferSize];

            // Process each face on the object
            for(int f = 0; f < object.faceIndices.size(); f++) {
                FaceIndexData face = object.faceIndices.get(f);
                int positionBufferIndexFaceStart = (f * numVerticesPerFace * positionComponentsPerVertex);
                int texelBufferIndexFaceStart = (f * numVerticesPerFace * texelComponentsPerVertex);
                int normalBufferIndexFaceStart = (f * numVerticesPerFace * normalComponentsPerVertex);

                // Resolve face position data and store in buffer
                for(int i = 0; i < face.positionIndices.length; i++) {
                    int positionBufferVertexIndexStart = (i * positionComponentsPerVertex);
                    float[] vertex = positionData.get(face.positionIndices[i] - 1);
                    for(int n = 0; n < vertex.length; n++) {
                        positionBuffer[positionBufferIndexFaceStart + positionBufferVertexIndexStart + n] = vertex[n];
                    }
                }

                // Resolve face texel data and store in buffer
                if(texelComponentsPerVertex > 0) {
                    for (int i = 0; i < face.texelIndices.length; i++) {
                        int texelBufferVertexIndexStart = (i * texelComponentsPerVertex);
                        float[] texel = texelData.get(face.texelIndices[i] - 1);
                        for (int n = 0; n < texel.length; n++) {
                            texelBuffer[texelBufferIndexFaceStart + texelBufferVertexIndexStart + n] = texel[n];
                        }
                    }
                }

                // Resolve face normal data and store in buffer
                if(normalComponentsPerVertex > 0) {
                    for(int i = 0; i < face.normalIndices.length; i++) {
                        int normalBufferVertexIndexStart = (i * normalComponentsPerVertex);
                        float[] normal = normalData.get(face.normalIndices[i] - 1);
                        for(int n = 0; n < normal.length; n++) {
                            normalBuffer[normalBufferIndexFaceStart + normalBufferVertexIndexStart + n] = normal[n];
                        }
                    }
                }
            }

            // Set the render method depending on how much face data appears in a line
            Mesh.RenderMethod renderMethod;
            switch (numVerticesPerFace) {
                case ONE_FACE_DATA_ELEMENT:
                    renderMethod = Mesh.RenderMethod.POINTS;
                    break;
                case TWO_FACE_DATA_ELEMENTS:
                    renderMethod = Mesh.RenderMethod.LINES;
                    break;
                case THREE_FACE_DATA_ELEMENTS:
                    renderMethod = Mesh.RenderMethod.TRIANGLES;
                    break;
                case FOUR_FACE_DATA_ELEMENTS:
                    // todo: resolve this by supporting GLES30 (which limits the amount of devices supported) or by resolving QUADS into 2 triangles)
                    Logger.error(TAG, "QUADS are not supported as they require GLES30");
                    break;
                default:
                    Logger.error(TAG, "Unsupported number of face data per vertex: " + numVerticesPerFace);
                    break;
            }

            MeshData meshData = new MeshData();
            meshData.name = object.name;
            meshData.material = object.material;
            meshData.mesh = new Mesh(positionBuffer, texelBuffer, normalBuffer,
                    Mesh.RenderMethod.TRIANGLES, positionComponentsPerVertex,
                    texelComponentsPerVertex, normalComponentsPerVertex);
            meshes[o] = meshData;
        }

        return meshes;
    }


    /**
     * Read an OBJ file from a resource ID
     *
     * @param resourceId The OBJ model file resource ID
     * @return A RenderObject built from the model data. The model can be rendered to a scene
     * @see Mesh
     * @since 1.0
     */
    public static MeshData[] readObjFile2(int resourceId) {
        // Attempt to open and read an OBJ file
        try {
            // Measure how long it takes to load load and read the model file
            long start = System.nanoTime();

            Resources resources = Crispin.getApplicationContext().getResources();
            InputStream inputStream;
            inputStream = resources.openRawResource(resourceId);
            inputStream.reset();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            MeshData[] meshes = process(reader);

            // End of time measurement
            long end = System.nanoTime();

            // Log time taken to load the OBJ model
            Logger.debug(TAG, "Model Loaded in: " + ((end - start) / NANOSECONDS_TO_MILLISECONDS_DIVIDE) + "ms");

            return meshes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


//    private static Mesh[] processWavefront(byte[] bytes) {
//        String type = null;
//        StringBuilder t = new StringBuilder();
//        int lineStartIndex = -1;
//
//        // Get line
//        for(int i = 0; i < bytes.length; i++) {
//            byte c = bytes[i];
//            if(lineStartIndex == -1) {
//                lineStartIndex = i;
//            }
//
//            if(c == ASCII_SPACE && t.length() == 0) {
//                type = new String(bytes, lineStartIndex, i - lineStartIndex);
//            }
//
//            // Check if the byte represents line feed or new line '/r' or '/n'
//            if (c == ASCII_NEW_LINE || c == ASCII_CARRIAGE_RETURN) {
//                type = null;
//                lineStartIndex = -1;
//            }
//        }
//    }


    private static boolean isNumericChar(byte c) {
        return (c >= ASCII_0 && c <= ASCII_9) || c == ASCII_POINT || c == ASCII_MINUS;
    }

    private static int findFloatEndIndex(byte[] bytes, int startIndex) {
        // Find the end index of the number
        for (int n = startIndex; n < bytes.length; n++) {
            if (!isNumericChar(bytes[n])) {
                return n;
            }
        }
        return bytes.length - 1;
    }

    private static float indexRangeToFloat(byte[] bytes, int startIndex, int endIndex) {
        return Float.parseFloat(new String(bytes, startIndex, endIndex - startIndex));
    }

    /**
     * Process the OBJ model
     *
     * @param bytes The model file as an array of bytes
     * @return A Mesh built from the model data. The model can be rendered to a scene
     * @see Mesh
     * @since 1.0
     */
    private static Mesh processObj2(byte[] bytes) {
        RenderObjectData renderObjectData = new RenderObjectData();

        // Keep track of the type of data we are looking at
        byte lineType = 0;

        // Keep the number of different position elements in the face data
        int numberPositionDataElements = 0;

        // Whether or not to count the number of data elements in the position data
        boolean countPositionDataElements = true;

        // Keep the number of different normal elements in the face data
        int numberNormalDataElements = 0;

        // Whether or not to count the number of data elements in the normal data
        boolean countNormalDataElements = true;

        // Keep the number of different texel elements in the face data
        int numberTexelDataElements = 0;

        // Whether or not to count the number of data elements in the texel data
        boolean countTexelDataElements = true;

        // Keep the number of different elements in the face data
        int numberFaceDataElements = 0;

        // Keep the number of slashes in the face data
        int numberFaceDataSeparators = 0;

        // Keep the number of face data per line (this will help to determine the render method)
        int numberFaceDataPerLine = 0;

        // Whether or not to count the number of data elements and separators in the face data
        boolean countFaceDataElements = true;

        // Whether or not to count the number of face data per line
        boolean countFaceDataPerLine = true;

        // Iterate through all the bytes in the file
        for (int i = 0; i < bytes.length; i++) {
            final byte c = bytes[i];

            // Check if the byte represents line feed or new line '/r' or '/n'
            if (c == ASCII_NEW_LINE || c == ASCII_CARRIAGE_RETURN) {
                // Reset the line type for a new line
                lineType = 0;
                if (numberPositionDataElements > 0) {
                    countPositionDataElements = false;
                }
                if (numberTexelDataElements > 0) {
                    countTexelDataElements = false;
                }
                if (numberNormalDataElements > 0) {
                    countNormalDataElements = false;
                }
                if (numberFaceDataPerLine > 0) {
                    countFaceDataPerLine = false;
                }
                continue;
            }

            // Look at the line type so we know what we are processing
            switch (lineType) {
                case 0:
                    lineType = c;
                    break;
                case 'v':
                    // Normal or texel in use instead
                    if (bytes[i - 1] == 'v' && (c == 't' || c == 'n')) {
                        lineType = c;
                    } else if (isNumericChar(c)) {
                        int floatEndIndex = findFloatEndIndex(bytes, i);
                        float f = indexRangeToFloat(bytes, i, floatEndIndex);
                        i = floatEndIndex - 1; // attempt to pass non-numerical char next time

                        renderObjectData.addPositionData(f);
                        if (countPositionDataElements) {
                            numberPositionDataElements++;
                        }
                    }
                    break;
                case 't':

                    break;
                case 'n':

                    break;
            }
        }

        return null;
    }


//                case TEXEL:
//                    if (isNumericChar(c)) {
//                        // Check that a start index hasn't already been defined
//                        if (dataStartIndex == NO_START_INDEX) {
//                            dataStartIndex = i;
//
//                            if (!countTexelDataElements && numberTexelDataElements == 0) {
//                                countTexelDataElements = true;
//                            }
//                        }
//                    } else {
//                        // Check if there is a start index before continuing
//                        if (dataStartIndex != NO_START_INDEX) {
//                            // We are processing a float and have found the end of it, parse it
//                            renderObjectData.addTexelData(Float.parseFloat(new String(bytes,
//                                    dataStartIndex,
//                                    i - dataStartIndex)));
//                            dataStartIndex = NO_START_INDEX;
//
//                            if (countTexelDataElements) {
//                                numberTexelDataElements++;
//                            }
//                        }
//                    }
//                    break;
//                case NORMAL:
//                    if (isNumericChar(c)) {
//                        // Check that a start index hasn't already been defined
//                        if (dataStartIndex == NO_START_INDEX) {
//                            dataStartIndex = i;
//
//                            if (!countNormalDataElements && numberNormalDataElements == 0) {
//                                countNormalDataElements = true;
//                            }
//                        }
//                    } else {
//                        // Check if there is a start index before continuing
//                        if (dataStartIndex != NO_START_INDEX) {
//                            // We are processing a float and have found the end of it, parse it
//                            renderObjectData.addNormalData(Float.parseFloat(new String(bytes,
//                                    dataStartIndex,
//                                    i - dataStartIndex)));
//                            dataStartIndex = NO_START_INDEX;
//
//                            if (countNormalDataElements) {
//                                numberNormalDataElements++;
//                            }
//                        }
//                    }
//                    break;
//                case FACE:
//                    // Now we have to parse integers
//                    if (c >= ASCII_0 && c <= ASCII_9) {
//                        // Check that a start index hasn't already been defined
//                        if (dataStartIndex == NO_START_INDEX) {
//                            dataStartIndex = i;
//
//                            // Check if we should position counting the amount of face data per line
//                            if (!countFaceDataPerLine && numberFaceDataPerLine == 0) {
//                                countFaceDataPerLine = true;
//                            }
//
//                            // Check if we should position counting the number of face data elements
//                            // and separators
//                            if (!countFaceDataElements &&
//                                    numberFaceDataElements == 0 &&
//                                    numberFaceDataSeparators == 0) {
//                                countFaceDataElements = true;
//                            }
//                        }
//                    } else {
//                        // Increment the number of face data separators if one is found
//                        if (c == ASCII_FORWARD_SLASH && countFaceDataElements) // forward slash
//                        {
//                            numberFaceDataSeparators++;
//                        }
//
//                        // Check if there is a start index before continuing
//                        if (dataStartIndex != NO_START_INDEX) {
//                            // We are processing an int and have found the end of it, parse it
//                            renderObjectData.addFaceData(Integer.parseInt(new String(bytes,
//                                    dataStartIndex,
//                                    i - dataStartIndex)));
//
//                            dataStartIndex = NO_START_INDEX;
//
//                            // If we have finished processing a chunk of face data
//                            if (c != ASCII_FORWARD_SLASH && countFaceDataPerLine) {
//                                numberFaceDataPerLine++;
//                            }
//
//                            // Add face data elements if counting is enabled as we have found one
//                            if (countFaceDataElements) {
//                                numberFaceDataElements++;
//                            }
//                        }
//
//                        // If a space has been detected, stop counting face data elements
//                        if (c == ASCII_SPACE) // space
//                        {
//                            countFaceDataElements = false;
//                        }
//                    }
//                    break;
//            }
//        }
//    }


    /**
     * Process the OBJ model
     *
     * @param theFile The model file as an array of bytes
     * @return A Mesh built from the model data. The model can be rendered to a scene
     * @see Mesh
     * @since 1.0
     */
    private static Mesh processObj(byte[] theFile) {
        RenderObjectData renderObjectData = new RenderObjectData();

        // Keep track of the type of data we are looking at
        byte lineType = NONE;

        // Index associated to a float when processing
        int dataStartIndex = NO_START_INDEX;

        // Keep the number of different position elements in the face data
        int numberPositionDataElements = 0;

        // Whether or not to count the number of data elements in the position data
        boolean countPositionDataElements = false;

        // Keep the number of different normal elements in the face data
        int numberNormalDataElements = 0;

        // Whether or not to count the number of data elements in the normal data
        boolean countNormalDataElements = false;

        // Keep the number of different texel elements in the face data
        int numberTexelDataElements = 0;

        // Whether or not to count the number of data elements in the texel data
        boolean countTexelDataElements = false;

        // Keep the number of different elements in the face data
        int numberFaceDataElements = 0;

        // Keep the number of slashes in the face data
        int numberFaceDataSeparators = 0;

        // Keep the number of face data per line (this will help to determine the render method)
        int numberFaceDataPerLine = 0;

        // Whether or not to count the number of data elements and separators in the face data
        boolean countFaceDataElements = false;

        // Whether or not to count the number of face data per line
        boolean countFaceDataPerLine = false;

        // Iterate through all the bytes in the file
        for (int i = 0; i < theFile.length; i++) {
            final byte theByte = theFile[i];

            // Look at the line type so we know what we are processing
            switch (lineType) {
                case NONE:
                    // Discover what type the line that we are working on is
                    switch (theByte) {
                        case ASCII_HASHTAG: // '#' for comment
                            lineType = COMMENT;
                            break;
                        case ASCII_F: // 'f' for face
                            lineType = FACE;
                            break;
                        case ASCII_V: // 'v' for vertex
                            lineType = VERTEX;
                            break;
                    }
                    break;
                case VERTEX:
                    // We know that the line type is vertex related but we still don't know what it
                    // is, discover its type
                    switch (theByte) {
                        case ASCII_SPACE: // SPACE for vertex position
                            lineType = POSITION;
                            break;
                        case ASCII_T: // 't' for vertex texel
                            lineType = TEXEL;
                            break;
                        case ASCII_N: // 'n' for vertex direction
                            lineType = NORMAL;
                            break;
                        default: // Unsupported or error. Reset the line type
                            lineType = NONE;
                            break;
                    }
                    break;
                case POSITION:
                    // This is float data relevant to vertex position
                    if ((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS) {
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX) {
                            dataStartIndex = i;

                            if (!countPositionDataElements && numberPositionDataElements == 0) {
                                countPositionDataElements = true;
                            }
                        }
                    } else {
                        // Check if there is a start index before continuing
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile, dataStartIndex, i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if (countPositionDataElements) {
                                numberPositionDataElements++;
                            }
                        }
                    }
                    break;
                case TEXEL:
                    // This is float data relevant to vertex position
                    if ((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS) {
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX) {
                            dataStartIndex = i;

                            if (!countTexelDataElements && numberTexelDataElements == 0) {
                                countTexelDataElements = true;
                            }
                        }
                    } else {
                        // Check if there is a start index before continuing
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if (countTexelDataElements) {
                                numberTexelDataElements++;
                            }
                        }
                    }
                    break;
                case NORMAL:
                    // This is float data relevant to vertex position
                    if ((theByte >= ASCII_0 && theByte <= ASCII_9) ||
                            theByte == ASCII_POINT || theByte == ASCII_MINUS) {
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX) {
                            dataStartIndex = i;

                            if (!countNormalDataElements && numberNormalDataElements == 0) {
                                countNormalDataElements = true;
                            }
                        }
                    } else {
                        // Check if there is a start index before continuing
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));
                            dataStartIndex = NO_START_INDEX;

                            if (countNormalDataElements) {
                                numberNormalDataElements++;
                            }
                        }
                    }
                    break;
                case FACE:
                    // Now we have to parse integers
                    if (theByte >= ASCII_0 && theByte <= ASCII_9) {
                        // Check that a start index hasn't already been defined
                        if (dataStartIndex == NO_START_INDEX) {
                            dataStartIndex = i;

                            // Check if we should position counting the amount of face data per line
                            if (!countFaceDataPerLine && numberFaceDataPerLine == 0) {
                                countFaceDataPerLine = true;
                            }

                            // Check if we should position counting the number of face data elements
                            // and separators
                            if (!countFaceDataElements &&
                                    numberFaceDataElements == 0 &&
                                    numberFaceDataSeparators == 0) {
                                countFaceDataElements = true;
                            }
                        }
                    } else {
                        // Increment the number of face data separators if one is found
                        if (theByte == ASCII_FORWARD_SLASH && countFaceDataElements) // forward slash
                        {
                            numberFaceDataSeparators++;
                        }

                        // Check if there is a start index before continuing
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile,
                                    dataStartIndex,
                                    i - dataStartIndex)));

                            dataStartIndex = NO_START_INDEX;

                            // If we have finished processing a chunk of face data
                            if (theByte != ASCII_FORWARD_SLASH && countFaceDataPerLine) {
                                numberFaceDataPerLine++;
                            }

                            // Add face data elements if counting is enabled as we have found one
                            if (countFaceDataElements) {
                                numberFaceDataElements++;
                            }
                        }

                        // If a space has been detected, stop counting face data elements
                        if (theByte == ASCII_SPACE) // space
                        {
                            countFaceDataElements = false;
                        }
                    }
                    break;
            }

            // Check if the byte represents line feed or new line '/r' or '/n'
            if (theByte == ASCII_NEW_LINE || theByte == ASCII_CARRIAGE_RETURN) // '\n' or '\r'
            {
                // Reset the line type for a new line
                lineType = NONE;
                dataStartIndex = NO_START_INDEX;

                countPositionDataElements = false;
                countTexelDataElements = false;
                countNormalDataElements = false;
                countFaceDataPerLine = false;
            }

            // If we are processing the last byte, process last bits of data
            if (i == theFile.length - 1) {
                // Look at the line type so we know what we are processing
                switch (lineType) {
                    case POSITION:
                        // Check that a start index has been found before attempting to add position
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addPositionData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case TEXEL:
                        // Check that a start index has been found before attempting to add texel
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addTexelData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case NORMAL:
                        // Check that a start index has been found before attempting to add normal
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing a float and have found the end of it, parse it
                            renderObjectData.addNormalData(Float.parseFloat(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));
                        }
                        break;
                    case FACE:
                        // Check that a start index has been found before attempting to add face
                        // data (because there will not have been any in the string)
                        if (dataStartIndex != NO_START_INDEX) {
                            // We are processing an int and have found the end of it, parse it
                            renderObjectData.addFaceData(Integer.parseInt(new String(theFile,
                                    dataStartIndex,
                                    theFile.length - dataStartIndex)));

                            // If we have finished processing a chunk of face data
                            if (theByte != ASCII_FORWARD_SLASH && countFaceDataPerLine) {
                                numberFaceDataPerLine++;
                            }
                        }
                        break;
                }
            }
        }

        // Determine what type of face data the OBJ model file has
        if (numberFaceDataElements == THREE_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == TWO_FACE_DATA_SEPARATORS) {
            // Position, texel and direction data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.
                    POSITION_AND_TEXEL_AND_NORMAL);
        } else if (numberFaceDataElements == TWO_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == TWO_FACE_DATA_SEPARATORS) {
            // Position and direction data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_NORMAL);
        } else if (numberFaceDataElements == TWO_FACE_DATA_ELEMENTS &&
                numberFaceDataSeparators == ONE_FACE_DATA_SEPARATOR) {
            // Position and texel data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_AND_TEXEL);
        } else if (numberFaceDataElements == ONE_FACE_DATA_ELEMENT &&
                numberFaceDataSeparators == 0) {
            // Only position data has been provided
            renderObjectData.setFaceDataType(RenderObjectData.FaceData.POSITION_ONLY);
        } else {
            // error
            Logger.error(TAG, "Failed to determine how many face data elements in an OBJ" +
                    "model files face data");
        }

        // Set the render method depending on how much face data appears in a line
        switch (numberFaceDataPerLine) {
            case ONE_FACE_DATA_ELEMENT:
                renderObjectData.setRenderMethod(Mesh.RenderMethod.POINTS);
                break;
            case TWO_FACE_DATA_ELEMENTS:
                renderObjectData.setRenderMethod(Mesh.RenderMethod.LINES);
                break;
            case THREE_FACE_DATA_ELEMENTS:
                renderObjectData.setRenderMethod(Mesh.RenderMethod.TRIANGLES);
                break;
            case FOUR_FACE_DATA_ELEMENTS:
                Logger.error(TAG, "QUADS are not supported as they require GLES 30");
                break;
            default:
                Logger.error(TAG, "Unsupported number of face data per vertex: " +
                        numberFaceDataPerLine);
                break;
        }

        renderObjectData.setNumPositionComponents((byte) numberPositionDataElements);
        renderObjectData.setNumNormalComponents((byte) numberNormalDataElements);
        renderObjectData.setNumTexelComponents((byte) numberTexelDataElements);
        return renderObjectData.processData();
    }
}
