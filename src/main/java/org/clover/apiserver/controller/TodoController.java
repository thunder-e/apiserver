package org.clover.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.TodoDTO;
import org.clover.apiserver.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Log4j2
@RequiredArgsConstructor //자동주입받도록
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list................" + pageRequestDTO);

        return todoService.getList(pageRequestDTO);
    }

    @PostMapping("/")  //@RequestBody: JSON데이터로 받기위해
    public Map<String, Long> register( @RequestBody TodoDTO dto ) {

        log.info("register :: todoDTO : " + dto);

        Long tno = todoService.register(dto);

        return Map.of("TNO", tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno,
                                      @RequestBody TodoDTO todoDTO) {

        todoDTO.setTno(tno);

        todoService.modify(todoDTO);

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {

        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
    }

}
