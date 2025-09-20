package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Intolerance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String label;

    @Column(unique = true)
    private String apiValue;

    public Intolerance(String label, String apiValue) {
        this.label = label;
        this.apiValue = apiValue;
    }
}
