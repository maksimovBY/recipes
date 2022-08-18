package com.example.recipes.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @ElementCollection
    private List<String> ingredients;
    @ElementCollection
    private List<String> directions;
    private String category;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity user;


}
