package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "diet_option")
public class DietOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String label;

    @Column(unique = true, name = "api_value")
    private String apiValue;

    public DietOption(String label, String apiValue) {
        this.label = label;
        this.apiValue = apiValue;
    }
}
