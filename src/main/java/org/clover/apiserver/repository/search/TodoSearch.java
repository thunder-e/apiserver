package org.clover.apiserver.repository.search;

import org.clover.apiserver.domain.Todo;
import org.clover.apiserver.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
