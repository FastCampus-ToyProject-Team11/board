package com.fastcampus.board.report;

import com.fastcampus.board.__core.security.PrincipalUserDetail;
import com.fastcampus.board.__core.util.ApiResponse;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/")
    public ResponseEntity<?> save(@AuthenticationPrincipal PrincipalUserDetail userDetail,
                                  @RequestBody ReportRequest.saveDTO saveDTO) {
        log.info("/board/report POST " + saveDTO);

        User user = userDetail.getUser();
        Report report = reportService.save(saveDTO, user);

        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
