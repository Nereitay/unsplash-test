package com.example.test.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test")
public class Unsplash {
    @Id
    private String id;
    private String description;
    private String cover_photo_id;
    private String title;
}
