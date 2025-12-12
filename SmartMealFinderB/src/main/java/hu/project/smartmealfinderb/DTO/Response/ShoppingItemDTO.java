package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingItemDTO {

    private Long id;
    private Long ingredientId;
    private String name;

    private double amount;
    private String unit;
    private boolean checked;
}
