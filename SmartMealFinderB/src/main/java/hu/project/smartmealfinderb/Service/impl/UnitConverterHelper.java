package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.UnitType;
import org.springframework.stereotype.Component;

@Component
public class UnitConverterHelper {

    // Kategorizálás
    public UnitType determineType(String unit) {
        if (unit == null || unit.isEmpty()) return UnitType.COUNT;
        String u = unit.toLowerCase().trim();

        // \\b teljes egyezés kérése
        // egyezik "g", "gram", "grams" ... de a "large" már nem
        if (u.matches(".*\\b(g|gram|grams|oz|ounce|ounces|lb|pound|pounds|kg|kilogram|kilograms|mg|milligram|milligrams)\\b.*")) {
            return UnitType.WEIGHT;
        }

        if (u.matches(".*\\b(ml|l|liter|liters|cup|cups|tsp|teaspoon|teaspoons|tbsp|tablespoon|tablespoons|pint|pints|quart|quarts|gal|gallon|gallons|fluid)\\b.*")) {
            return UnitType.VOLUME;
        }

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
        if (type == UnitType.VOLUME) {
            // Kisebb mennyiségek visszaalakítása kanál mértékegységre
            if (amount < 15) return "tsp";
            if (amount < 60) return "tbsp";
            if (amount >= 1000) return "l";
            return "ml";
        }
        return "pcs";
    }

    public double getDisplayAmount(UnitType type, double amount) {
        // súly
        if (type == UnitType.WEIGHT) {
            if (amount >= 1000) {
                return round(amount / 1000); // kg
            }
            return Math.round(amount);
        }

        // űrtartalom
        if (type == UnitType.VOLUME) {
            if (amount < 15) {
                return round(amount / 5.0); //ml -> tsp (5ml)
            }
            if (amount < 60) {
                return round(amount / 15.0); //ml -> tbsp (15ml)
            }
            if (amount >= 1000) {
                return round(amount / 1000.0); // liter
            }
            return Math.round(amount); // ml
        }

        return Math.ceil(amount);
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
