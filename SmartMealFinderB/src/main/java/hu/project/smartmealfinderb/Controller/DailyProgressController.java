package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/progress")
public class DailyProgressController {

    @Autowired
    private DailyProgressService dailyProgressService;

    @PostMapping("/save")
    public ResponseEntity<?> postProgress(@AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Daily progress successfully saved."));
    }
}
