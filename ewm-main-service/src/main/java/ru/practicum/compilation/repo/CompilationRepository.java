package ru.practicum.compilation.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

//    @Query(
//            "SELECT c FROM Compilation c " +
//            "WHERE ((:pinned) IS NULL OR c.pinned = :pinned)"
//    )
    Page<Compilation> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}