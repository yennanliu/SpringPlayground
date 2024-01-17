package com.yen.FlinkRestService.Repository;

import com.yen.FlinkRestService.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository  extends JpaRepository<Notebook, Integer> {
}
