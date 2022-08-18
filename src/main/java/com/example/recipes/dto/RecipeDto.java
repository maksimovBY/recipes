package com.example.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.recipes.entity.UserEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {


    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotEmpty
    @Size(min = 1)
    private List<String> ingredients;
    @NotEmpty
    @Size(min = 1)
    private List<String> directions;
    @NotBlank
    private String category;
    private LocalDateTime date;


}
