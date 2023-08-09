package com.crispin.crispinmobile.SpatialHashing;

import com.crispin.crispinmobile.Physics.BoundBox2D;

public class Client {
    // Cell range for client
    public int[][] indices;

    public BoundedObject boundedObject;

    public Client(BoundedObject boundedObject) {
        this.boundedObject = boundedObject;
    }

    public BoundBox2D getBoundBox() {
        return boundedObject.getBoundBox();
    }
}