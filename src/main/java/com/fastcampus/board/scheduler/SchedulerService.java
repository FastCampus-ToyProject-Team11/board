package com.fastcampus.board.scheduler;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.repository.BoardRepository;
import com.fastcampus.board.user.Role;
import com.fastcampus.board.user.User;
import com.fastcampus.board.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(fixedDelay = 1000 * 10)
    public void run() {
        log.info("스케줄러 잘 동작해 : " + LocalDateTime.now());

        List<BoardResponse.UserSummary> userSummaries = boardRepository.getUserSummary();
        log.info("user Summaries : " + Arrays.toString(userSummaries.toArray()));

        userSummaries.forEach(userSummary -> {
            Optional<User> userOptional = userRepository.findById(userSummary.getUserId());
            User user = userOptional.orElseThrow(() -> new Exception500(ErrorMessage.NOT_FOUND_USER));
            user.setRole(Role.EXCELLENT);
        });
    }
}
