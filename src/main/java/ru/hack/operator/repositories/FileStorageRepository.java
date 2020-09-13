package ru.hack.operator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hack.operator.models.FileInfo;

import java.util.Optional;

@Repository
public interface FileStorageRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findOneByStorageFileName(String storageFileName);
}
