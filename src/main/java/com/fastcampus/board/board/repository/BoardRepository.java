package com.fastcampus.board.board.repository;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {


//    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR u.nickname LIKE %:keyword%)")
    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%)")
    Page<Board> findAllByKeyword(String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE b.role = :role")
    Page<Board> findAllByCategory(Role role, Pageable pageable);

//    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR u.nickname LIKE %:keyword%) AND b.role = :role")
    @Query("SELECT b FROM Board b LEFT JOIN b.user u WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.role = :role")
    Page<Board> findAllByKeywordCategory(Role role, String keyword, Pageable pageable);
}
