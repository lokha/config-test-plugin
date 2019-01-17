package test;

import org.bukkit.Material;
import org.bukkit.World;

public class BlockInfo {
    private World world;
    private int x;
    private int y;
    private int z;

    private Material material;
    private String command;

    public BlockInfo(World world, int x, int y, int z, Material material, String command) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.command = command;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "BlockInfo{" +
                "world name=" + world.getName() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
