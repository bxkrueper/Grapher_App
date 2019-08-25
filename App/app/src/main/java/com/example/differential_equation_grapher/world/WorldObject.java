package com.example.differential_equation_grapher.world;

public abstract class WorldObject {
    private static final String CLASS_NAME = "WorldObject";
    private static int nextID = 0;

    private World world;

    private String name;//a way for other world objects to find this one. Should be unique
    private final int id;

    public WorldObject(World world){
        this.world = world;
        this.id = nextID++;
    }

    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void add(WorldObject newWorldObject){
        world.add(newWorldObject);
    }
    public void delete(){
        world.delete(this);
    }


}
