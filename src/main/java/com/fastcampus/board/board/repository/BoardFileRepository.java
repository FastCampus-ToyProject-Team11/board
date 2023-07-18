package com.fastcampus.board.board.repository;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.model.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
