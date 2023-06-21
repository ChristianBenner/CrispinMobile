package com.crispin.crispinmobile;

import static com.crispin.crispinmobile.Utilities.OBJModelLoader.parseFaceElements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Physics.Collision;
import com.crispin.crispinmobile.Physics.HitboxPolygon;

import org.junit.Test;

import java.util.ArrayList;

public class ObjModelLoadTest {
    public ObjModelLoadTest() {

    }

    @Test
    public void parseFaceElements2DPosTexNormTest() {
        ArrayList<int[]> expected = new ArrayList<int[]>();
        expected.add(new int[]{1, 2, 3});
        expected.add(new int[]{4, 5, 6});


        ArrayList<int[]> actual = parseFaceElements(new String[]{"f", "1/2/3", "4/5/6"}, 1);

        if(actual.size() != expected.size()) {
            fail("mismatch on number of vertices");
        }

        for(int i = 0; i < expected.size(); i++) {
            int[] e = expected.get(i);
            int[] a = actual.get(i);
            if(e.length != a.length) {
                fail("mismatch on number of index elements");
            }

            for(int n = 0; n < e.length; n++) {
                if(e[n] != a[n]) {
                    fail("unexpected value. got: " + a[n] + ", expected: " + e[n]);
                }
            }
        }
    }

    @Test
    public void parseFaceElements3DPosNormTest() {
        ArrayList<int[]> expected = new ArrayList<int[]>();
        expected.add(new int[]{1, 0, 3});
        expected.add(new int[]{4, 0, 6});
        expected.add(new int[]{7, 0, 9});

        ArrayList<int[]> actual = parseFaceElements(new String[]{"f", "1//3", "4//6", "7//9"}, 1);

        if(actual.size() != expected.size()) {
            fail("mismatch on number of vertices");
        }

        for(int i = 0; i < expected.size(); i++) {
            int[] e = expected.get(i);
            int[] a = actual.get(i);
            if(e.length != a.length) {
                fail("mismatch on number of index elements");
            }

            for(int n = 0; n < e.length; n++) {
                if(e[n] != a[n]) {
                    fail("unexpected value. got: " + a[n] + ", expected: " + e[n]);
                }
            }
        }
    }
}