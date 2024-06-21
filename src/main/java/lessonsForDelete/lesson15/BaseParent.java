package lessonsForDelete.lesson15;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseParent extends AbstractEntity<Long>{
    @Column(name = "name")
    String name;
    @Column(name = "email")
    String email;

}
