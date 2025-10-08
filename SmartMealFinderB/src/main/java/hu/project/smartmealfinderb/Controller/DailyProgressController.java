package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.WeightLogReq;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class DailyProgressController {

    private final DailyProgressService dailyProgressService;

    @PostMapping("/weight/save")
    public ResponseEntity<?> saveWeightProgress(@RequestBody WeightLogReq weightLogReq) {

        this.dailyProgressService.saveWeight(
                weightLogReq.getWeight(),
                weightLogReq.getComment());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Daily progress successfully saved."));
    }

    @GetMapping
    public ResponseEntity<?> getProgress() {

        DailyProgress dailyProgress = this.dailyProgressService.getTodayProgress();

        return ResponseEntity.status(HttpStatus.OK).body(dailyProgress);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProgressHistory() {

        List<DailyProgress> progressHistoryList = this.dailyProgressService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(progressHistoryList);
    }
}
