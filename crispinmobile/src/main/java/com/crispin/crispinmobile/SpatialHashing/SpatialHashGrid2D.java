package com.crispin.crispinmobile.SpatialHashing;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.HashSet;

public class SpatialHashGrid2D {
    protected Cell[][] cells;
    private float cellWidth;
    private float cellHeight;
    private int cellsPerRow;
    private int cellsPerColumn;

    public SpatialHashGrid2D(Vec2 start, Scale2D size, int cellsPerRow, int cellsPerColumn) {
        cellWidth = size.w / cellsPerRow;
        cellHeight = size.h / cellsPerColumn;
        this.cellsPerRow = cellsPerRow;
        this.cellsPerColumn = cellsPerColumn;

        cells = new Cell[cellsPerColumn][cellsPerRow];
        for (int x = 0; x < cellsPerRow; x++) {
            for (int y = 0; y < cellsPerColumn; y++) {
                cells[y][x] = new Cell();
            }
        }
    }

    private int[] getCellIndex(float x, float y) {
        int ix = (int) (x / cellWidth);
        int iy = (int) (y / cellHeight);

        // Clamp to only range of cells
        ix = Math.max(0, Math.min(ix, cellsPerRow - 1));
        iy = Math.max(0, Math.min(iy, cellsPerColumn - 1));

        return new int[]{ix, iy};
    }

    public void add(Client client) {
        int[] i1 = getCellIndex(client.getBoundBox().x, client.getBoundBox().y);
        int[] i2 = getCellIndex(client.getBoundBox().x + client.getBoundBox().w, client.getBoundBox().y + client.getBoundBox().h);
        client.indices = new int[][]{i1, i2};

        // For the range of cells that the client is in, add the client to the cells
        for (int x = i1[0], xn = i2[0]; x <= xn; x++) {
            for (int y = i1[1], yn = i2[1]; y <= yn; y++) {
                cells[y][x].add(client);
            }
        }
    }

    public HashSet<Client> findNear(float x, float y, float w, float h) {
        // Get the cell co-ordinates for the bounds of the query
        int[] i1 = getCellIndex(x, y);
        int[] i2 = getCellIndex(x + w, y + h);

        HashSet<Client> clients = new HashSet<>();

        // For the range of cells that the bounds are in, add the clients of the cells to the set
        for (int ix = i1[0], xn = i2[0]; ix <= xn; ix++) {
            for (int iy = i1[1], yn = i2[1]; iy <= yn; iy++) {
                clients.addAll(cells[iy][ix]);
            }
        }

        return clients;
    }

    public HashSet<Client> findNear(Vec2 position, Scale2D size) {
        return findNear(position.x, position.y, size.w, size.h);
    }

    public HashSet<Client> findNear(Camera2D camera2D) {
        float x = camera2D.getPosition().x + camera2D.getLeft();
        float y = camera2D.getPosition().y + camera2D.getBottom();
        float w = camera2D.getRight() - camera2D.getLeft();
        float h = camera2D.getTop() - camera2D.getBottom();
        return findNear(x, y, w, h);
    }

    public void remove(Client client) {

    }

    public void update(Client client) {
        remove(client);
        add(client);
    }
}