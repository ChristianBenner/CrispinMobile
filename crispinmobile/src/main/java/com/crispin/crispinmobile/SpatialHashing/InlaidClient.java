package com.crispin.crispinmobile.SpatialHashing;

import com.crispin.crispinmobile.Physics.BoundBox2D;
import com.crispin.crispinmobile.SpatialHashing.BoundedObject;

public class InlaidClient {
    // Cell range for client
    public int[][] indices;
    public BoundedObject boundedObject;
    public int layer;

    public InlaidClient(BoundedObject boundedObject, int layer) {
        this.boundedObject = boundedObject;
        this.layer = layer;
    }

    public BoundBox2D getBoundBox() {
        return boundedObject.getBoundBox();
    }
}