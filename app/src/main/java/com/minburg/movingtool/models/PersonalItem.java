package com.minburg.movingtool.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "personal_items_table")
public class PersonalItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int estimatedValue;
    @TypeConverters(Converters.class)
    private Category category;
    @TypeConverters(Converters.class)
    private Ownership ownership;
    private String localStorage;
    private String firebaseStorage;
    @TypeConverters(Converters.class)
    private OffsetDateTime date;

    public PersonalItem(PersonalItem item){
        this.id = item.getId();
        this.name = item.getName();
        this.estimatedValue = item.getEstimatedValue();
        this.category = item.getCategory();
        this.ownership = item.getOwnership();
        this.localStorage = item.getLocalStorage();
        this.firebaseStorage = item.getFirebaseStorage();
        this.date = item.getDate();
    }

    public PersonalItem(String name, int estimatedValue, Category category, Ownership ownership, String localStorage, String firebaseStorage) {
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.category = category;
        this.ownership = ownership;
        this.localStorage = localStorage;
        this.firebaseStorage = firebaseStorage;
        this.date = OffsetDateTime.now();
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(int estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public String getLocalStorage() {
        return localStorage;
    }

    public void setLocalStorage(String localStorage) {
        this.localStorage = localStorage;
    }

    public String getFirebaseStorage() {
        return firebaseStorage;
    }

    public void setFirebaseStorage(String firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

}
