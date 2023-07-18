package com.fastcampus.board.board.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "board_file_tb")
@Entity
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static BoardFile toBoardFile(Board board, String originalFileName, String storedFileName) {
        return BoardFile.builder()
                .board(board)
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .build();
    }

}
