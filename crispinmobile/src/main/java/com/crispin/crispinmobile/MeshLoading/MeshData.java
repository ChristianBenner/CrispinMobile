package com.crispin.crispinmobile.MeshLoading;

import com.crispin.crispinmobile.Physics.BoundBox2D;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

public class MeshData {
    public String name;
    public Mesh mesh;
    public Mesh shadowMesh;
    public HitboxPolygon hitboxPolygon;
    public String materialLibrary;
    public String materialName;
    public BoundBox2D boundBox2D;
}