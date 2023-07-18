package com.fastcampus.board.board.repository;

import com.fastcampus.board.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
