package hu.project.smartmealfinderb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MacroTotals {

    private double calories;
    private double protein;
    private double carbs;
    private double fats;
}
