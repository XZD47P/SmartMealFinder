package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddUserIntoleranceReq {

    private List<String> intolerances;
}
