package model.LivingElements;

import enums.EHeroClass;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;

@Entity
@Getter
@Setter
public class Hero extends LiveEntity{
    @Id
    @Column(updatable = false, nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    EHeroClass heroClass;

    @Transient
    Point lastPoint = null;

    public Hero(String name, EHeroClass heroClass) {
        super(name, 100, 1, 1);
        this.heroClass = heroClass;
        this.lastPoint = this.point;
    }

    protected Hero() {
        super();
    }
}
