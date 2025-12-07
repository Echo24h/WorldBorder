package org.echo.worldborder;

import org.bukkit.Location;

public class Border {

    private int borderSize;
    private int centerX;
    private int centerZ;

    public Border(int borderSize, int centerX, int centerZ) {
        this.borderSize = borderSize;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public boolean isInBorder(Location location) {

        int x = location.getBlockX();
        int z = location.getBlockZ();

        if (x > centerX + (borderSize / 2) - 1)
            return true;
        if (x < centerX - (borderSize / 2) + 1)
            return true;
        if (z > centerZ + (borderSize / 2) - 1)
            return true;
        if (z < centerZ - (borderSize / 2) + 1)
            return true;
        return false;
    }
}
