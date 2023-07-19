package com.fastcampus.board.report;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Report save(ReportRequest.saveDTO saveDTO, User user) {
        if (saveDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_REPORT_SAVE);

        try {
            Report report = saveDTO.toEntityWith(user);
            return reportRepository.save(report);
        } catch (DataIntegrityViolationException exception) {
            throw new Exception500(ErrorMessage.DUPLICATE_REPORT);
        }
    }
}
