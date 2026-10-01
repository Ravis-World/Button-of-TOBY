package com.ravi.items;

public class Item {
    private final String id;
    private final String name;
    private final ItemColor color;
    private final ItemShape shape;

    public Item(String id, String name, ItemColor color, ItemShape shape) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.shape = shape;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemColor getColor() {
        return color;
    }

    public ItemShape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return name;
    }
}