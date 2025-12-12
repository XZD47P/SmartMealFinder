package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.UnitType;
import org.springframework.stereotype.Component;

@Component
public class UnitConverterHelper {

    // Kategorizálás
    public UnitType determineType(String unit) {
        if (unit == null) return UnitType.COUNT;
        String u = unit.toLowerCase();
        if (u.matches(".*(g|gram|oz|ounce|lb|pound|kg).*")) return UnitType.WEIGHT;
        if (u.matches(".*(ml|l|cup|tsp|tbsp|pint|quart|gal|fluid).*")) return UnitType.VOLUME;
        return UnitType.COUNT;
    }

    // Normalizáció
    public double normalize(double amount, String unit) {
        String u = unit.toLowerCase();
        switch (u) {
            // Súly-> gramm
            case "oz":
            case "ounce":
            case "ounces":
                return amount * 28.35;
            case "lb":
            case "pound":
            case "pounds":
                return amount * 453.59;
            case "kg":
            case "kilogram":
            case "kilograms":
                return amount * 1000;
            case "mg":
            case "milligram":
                return amount / 1000;

            // Űrtartalom-> ml
            case "cup":
            case "cups":
            case "c":
                return amount * 236.59;
            case "tbsp":
            case "tablespoon":
            case "tablespoons":
                return amount * 15;
            case "tsp":
            case "teaspoon":
            case "teaspoons":
                return amount * 5;
            case "l":
            case "liter":
            case "liters":
                return amount * 1000;
            case "pt":
            case "pint":
                return amount * 473.18;
            case "qt":
            case "quart":
                return amount * 946.35;
            case "gal":
            case "gallon":
                return amount * 3785.41;

            default:
                return amount;
        }
    }

    // Olvashatóvá tétel
    public String getDisplayUnit(UnitType type, double amount) {
        if (type == UnitType.WEIGHT) return amount >= 1000 ? "kg" : "g";
        if (type == UnitType.VOLUME) return amount >= 1000 ? "l" : "ml";
        return "pcs";
    }

    public double getDisplayAmount(UnitType type, double amount) {
        if (type == UnitType.WEIGHT && amount >= 1000) return Math.round((amount / 1000) * 10.0) / 10.0;
        if (type == UnitType.VOLUME && amount >= 1000) return Math.round((amount / 1000) * 10.0) / 10.0;
        if (type == UnitType.COUNT) return Math.ceil(amount); // Round up counts (2.1 onions -> 3 onions)
        return Math.round(amount);
    }

}
