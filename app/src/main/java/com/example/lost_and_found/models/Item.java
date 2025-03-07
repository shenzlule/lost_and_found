package com.example.lost_and_found.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")
public class Item  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String imageBase64;
    private String description;
    private String userId;
    private String serialNumber;

    private boolean isLost;
    private String contact;

    public Item(String title, String imageBase64, String description, String userId, String serialNumber, boolean isLost, String contact) {
        this.title = title;
        this.imageBase64 = imageBase64;
        this.description = description;
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.isLost = isLost;
        this.contact = contact;
    }


    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public boolean isLost() { return isLost; }
    public void setLost(boolean lost) { isLost = lost; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
