package com.example.fetchtakehome;

public class Item  implements Comparable<Item> {

    private int id;
    private int listId;
    private String name;

    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public int getId() { return id; }

    public int getListId() { return listId; }

    public String getName() { return name; }

    public String allToString() {
        return this.id + " " +  this.listId + " " + this.name;
    }

    @Override
    public int compareTo(Item compItem) {
        int compName = compItem.getId();
        return this.id - compName;
    }

}
