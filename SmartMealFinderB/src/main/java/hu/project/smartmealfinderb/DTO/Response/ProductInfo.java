package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

@Data
public class ProductInfo {

    private Long id;
    private String title;
    private Nutrition nutrition;
}
