package com.example.memorial_app;

import java.util.ArrayList;
import java.util.Comparator;

public class MyClass {
    private int itemIds;
    private String itemUpdated_at;
    private String itemCreated_at;
    private String itemNames;
    private String itemRubys;
    private String itemCaptions;
    private Float itemLatitudes;
    private Float itemLongitudes;
    private String itemImages;
    private int itemMemoryFloats;

    private Float itemDistances;

    public MyClass(int itemIds, String itemUpdated_at, String itemCreated_at, String itemNames, String itemRubys, String itemCaptions, Float itemLatitudes, Float itemLongitudes, String itemImages, Float itemDistances, int itemMemoryFloats){
        this.itemIds = itemIds;
        this.itemUpdated_at = itemUpdated_at;
        this.itemCreated_at = itemCreated_at;
        this.itemNames = itemNames;
        this.itemRubys = itemRubys;
        this.itemCaptions = itemCaptions;
        this.itemLatitudes = itemLatitudes;
        this.itemLongitudes = itemLongitudes;
        this.itemImages = itemImages;
        this.itemDistances = itemDistances;
        this.itemMemoryFloats = itemMemoryFloats;
    }

    public int getItemIds(){
        return itemIds;
    }
    public String getItemUpdated_at(){
        return itemUpdated_at;
    }
    public String getItemCreated_at(){
        return itemCreated_at;
    }
    public String getItemNames(){
        return itemNames;
    }
    public String getItemRubys(){
        return itemRubys;
    }
    public String getItemCaptions(){
        return itemCaptions;
    }
    public Float getItemLatitudes(){
        return itemLatitudes;
    }
    public Float getItemLongitudes(){
        return itemLongitudes;
    }
    public String getItemImages(){
        return itemImages;
    }
    public Float getItemDistances(){
        return itemDistances;
    }
    public int getItemMemoryFloats(){return itemMemoryFloats;}

    public void setItemDistances(Float distance) { this.itemDistances = distance; }
    public void setItemMemoryFloats(int MemoryFloat){ this.itemMemoryFloats = MemoryFloat; }
}

class RubyComp implements Comparator<MyClass> {
    public int compare(MyClass c1, MyClass c2){
        return c1.getItemRubys().compareTo(c2.getItemRubys());
    }
}

class IdComp implements Comparator<MyClass> {
    public int compare(MyClass c1, MyClass c2) {
        if(c1.getItemIds() < c2.getItemIds()) {
            return -1;
        } else if(c1.getItemIds() > c2.getItemIds()) {
            return 1;
        } else {
            return c1.getItemRubys().compareTo(c2.getItemRubys());
        }
    }
}

class DistanceComp implements Comparator<MyClass> {
    public int compare(MyClass c1, MyClass c2) {
        if(c1.getItemDistances() < c2.getItemDistances()) {
            return -1;
        } else if(c1.getItemDistances() > c2.getItemDistances()) {
            return 1;
        } else {
            return c1.getItemRubys().compareTo(c2.getItemRubys());
        }
    }
}

class UpdateComp implements Comparator<MyClass> {
    public int compare(MyClass c1, MyClass c2) {
        return c1.getItemUpdated_at().compareTo(c2.getItemUpdated_at());
    }
}

class MemoryFloatComp implements Comparator<MyClass> {
    public int compare(MyClass c1, MyClass c2) {
        if(c1.getItemMemoryFloats() > c2.getItemMemoryFloats()) {
            return -1;
        } else if(c1.getItemMemoryFloats() < c2.getItemMemoryFloats()) {
            return 1;
        } else {
            return c1.getItemRubys().compareTo(c2.getItemRubys());
        }
    }
}

