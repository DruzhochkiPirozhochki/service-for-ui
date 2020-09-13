package ru.hack.operator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hack.operator.models.Trouble;

import java.util.List;

@Repository
public interface TroubleRepository extends JpaRepository<Trouble, Long> {
    List<Trouble> findAllByStatus(Trouble.Status status);

    default List<Trouble> findAllToDo() {
        return findAllByStatus(Trouble.Status.TO_DO);
    }
}
