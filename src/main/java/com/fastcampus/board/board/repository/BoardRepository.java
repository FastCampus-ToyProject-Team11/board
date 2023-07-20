package com.fastcampus.board.board.repository;

import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByHideIsFalse(Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE b.hide = false AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR u.nickName LIKE %:keyword%)")
    Page<Board> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE b.hide = false AND b.role = :role")
    Page<Board> findAllByRole(@Param("role") Role role, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE b.hide = false AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR u.nickName LIKE %:keyword%) AND b.role = :role")
    Page<Board> findAllByKeywordRole(@Param("role") Role role, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.fastcampus.board.board.dto.BoardResponse$UserSummary(b.user.id, b.user.role, COUNT(b)) " +
            "FROM Board b " +
            "WHERE b.user.role = 'SESAC' " +
            "GROUP BY b.user.id, b.user.role " +
            "HAVING COUNT(b) >= 10")
    List<BoardResponse.UserSummary> getUserSummary();
}
