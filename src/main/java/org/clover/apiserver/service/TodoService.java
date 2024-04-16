package org.clover.apiserver.service;

import jakarta.transaction.Transactional;
import org.clover.apiserver.domain.Todo;
import org.clover.apiserver.dto.PageRequestDTO;
import org.clover.apiserver.dto.PageResponseDTO;
import org.clover.apiserver.dto.TodoDTO;

@Transactional
public interface TodoService {

    //interface니까 다 public -> 생략
    TodoDTO get(Long tno);

    Long register(TodoDTO dto);

    void modify(TodoDTO dto);

    void remove(Long tno);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);


    //인터페이스 : 실체가 없는 기능X
    //default : 기능이나 메서드를 선언
    default TodoDTO entityToDTO(Todo todo) {

        return TodoDTO.builder()
                        .tno(todo.getTno())
                        .title(todo.getTitle())
                        .content(todo.getContent())
                        .complete(todo.isComplete())
                        .dueDate(todo.getDueDate())
                        .build();

    }


    default Todo dtoToEntity(TodoDTO todoDTO) {

        return Todo.builder()
                    .tno(todoDTO.getTno())
                    .title(todoDTO.getTitle())
                    .content(todoDTO.getContent())
                    .complete(todoDTO.isComplete())
                    .dueDate(todoDTO.getDueDate())
                    .build();

    }

}
