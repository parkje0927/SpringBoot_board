package com.example.project.Service;

import com.example.project.domain.entity.Board;
import com.example.project.domain.repository.BoardRepository;
import com.example.project.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Page<Board> getBoardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();
        BoardDto boardDto = convertEntitytoDto(board);
        return boardDto;
    }

    @Transactional
    public Boolean getPageCheck(Pageable pageable) {
        Page<Board> saved = getBoardList(pageable);
        Boolean check = saved.hasNext();

        return check;
    }

    @Transactional
    public List<BoardDto> searchPost(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boards.isEmpty()) {
            return boardDtoList;
        }

        for (Board board : boards) {
            boardDtoList.add(this.convertEntitytoDto(board));
        }

        return boardDtoList;
     }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    public BoardDto convertEntitytoDto(Board board) {
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();

        return boardDto;
    }
}
