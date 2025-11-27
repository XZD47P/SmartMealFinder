package hu.project.smartmealfinderb.Model;

public enum Interaction {
    VIEW,
    LIKE,
    ADDINGRIDIENT,
    FAVOURITE,
    ATE;


    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
