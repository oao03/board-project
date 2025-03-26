package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "\\board\\src\\main\\resources\\static\\files";

            UUID uuid = UUID.randomUUID();

            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFileName(fileName);
            board.setFilePath("/files/" + fileName);
        }

        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardList(String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return boardRepository.findAll(pageable);
        } else {
            return boardRepository.findByTitleContaining(searchKeyword, pageable);
        }
    }


    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }


}
