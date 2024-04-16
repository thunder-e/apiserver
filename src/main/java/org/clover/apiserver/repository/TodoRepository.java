package org.clover.apiserver.repository;

import org.clover.apiserver.domain.Todo;
import org.clover.apiserver.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoSearch {


}
