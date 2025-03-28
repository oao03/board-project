package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/board/write") //localhost:8080/board/write 이 주소로 접속을 하면 boardwrite(html)를 보여주겠다.
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardwritePro(Board board, Model model, @RequestParam("file") MultipartFile file) throws Exception {
        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.DESC)
                            Pageable pageable, String searchKeyword) {

        Page<Board> list = null;
        if(searchKeyword == null) {
            list = boardService.boardList(searchKeyword, pageable);
        }
        else{
            list = boardService.boardList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4,1);
        int endPage = Math.min(nowPage + 5,list.getTotalPages());

        model.addAttribute("list", boardService.boardList(searchKeyword, pageable));
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, @RequestParam(name = "id") Long id) {

        model.addAttribute("board", boardService.boardView(Math.toIntExact(id)));
        return "boardview";
    }


    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", boardService.boardView(Math.toIntExact(id)));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, @ModelAttribute Board board, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{

        Board boardTemp = boardService.boardView(Math.toIntExact(id));
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        if (file != null && !file.isEmpty()) { // 파일이 있을 경우만 처리
            boardService.write(boardTemp, file);
        } else {
            boardService.write(boardTemp, null);
        }

        return "redirect:/board/list";

    }

    @GetMapping("/board/download/{fileName}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileName") String fileName) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\board\\src\\main\\resources\\static\\files";
        Path filePath = Paths.get(projectPath).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        // 파일 원래 이름 추출 (uuid 제거)
        String originalFileName = fileName.substring(fileName.indexOf("_") + 1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam(name = "id") Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/list";
    }



}
