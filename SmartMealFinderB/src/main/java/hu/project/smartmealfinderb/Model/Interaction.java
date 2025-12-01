package hu.project.smartmealfinderb.Model;

import lombok.Getter;

@Getter
public enum Interaction {
    SEEN(0.0),
    VIEW(1.0),
    LIKE(4.0),
    FAVOURITE(8.0),
    ATE(10.0);

    private final String type;
    private final double weight;

    Interaction(double weight) {
        this.type = name().toLowerCase();
        this.weight = weight;
    }


}
