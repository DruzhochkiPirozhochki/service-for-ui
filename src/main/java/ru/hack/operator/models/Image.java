package ru.hack.operator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lng;
    private Double ltd;

    private LocalDateTime savedAt;
    private LocalDateTime lastUpdate;

    @OneToOne
    private FileInfo fileInfo;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public enum Status {
        CHECKING, CHECKED, TROUBLED, RESOLVED, GOOD
    }

}
