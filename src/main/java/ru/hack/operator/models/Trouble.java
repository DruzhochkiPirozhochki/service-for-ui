package ru.hack.operator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trouble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Image image;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Type.class)
    private List<Type> types;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Type {
        TROUBLE_0, TROUBLE_1, TROUBLE_2
    }

    public enum Status {
        TO_DO, IN_PROGRESS, CHECK, DONE
    }
}
