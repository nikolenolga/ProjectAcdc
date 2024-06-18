package com.javarush.lessonsForDelete.lesson16.nativeQ;

import jakarta.persistence.*;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SqlResultSetMapping(
        name = "NameTitleMapping",
        classes = @ConstructorResult(
                targetClass = UnMappedDemo.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name")
                }
        )
)
@Table(name = "mapped_demo")
public class MappedDemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy")
    private NamingStrategy strategy;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "level")
    private Integer level;
}
