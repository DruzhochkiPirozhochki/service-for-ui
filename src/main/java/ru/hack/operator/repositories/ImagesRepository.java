package ru.hack.operator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hack.operator.models.Image;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
}
