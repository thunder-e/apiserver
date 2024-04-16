package org.clover.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.TodoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;


    @Test
    public void testGet() {

        Long tno = 50L;
        log.info(todoService.get(tno));
    }

    @Test
    public void testRegister() {
        TodoDTO dto = TodoDTO.builder()
                .title("Title..")
                .content("Content..")
                .dueDate(LocalDate.of(2024,4,1))
                .build();

        log.info(todoService.register(dto));
    }


    @Test
    public void testGetlist() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(11).build();

        log.info(todoService.getList(pageRequestDTO));

    }


}
