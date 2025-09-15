package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddUserDietOptionReq {

    private List<String> diets;
}
