package com.example.FirstSpringBoot.Models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection="posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String id;
    private String title;
    private String desc;
    private String img;
    private String author;
    private String date;
    private String category;
    private List<Command> commands=new ArrayList<>();
    private int views;
    private List<String> viewedBy;
    public String getShortDescription() {
        if (desc != null && desc.length() > 60) {
            return desc.substring(0, 60) + "...";
        }
        return desc;
    }



}
