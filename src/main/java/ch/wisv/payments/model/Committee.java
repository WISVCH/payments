package ch.wisv.payments.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Committee {

    public Committee(CommitteeEnum name, int year) {
        this.name = name;
        this.year = year;
    }

    @Id
    @GeneratedValue
    @Getter
    int id;

    @Getter
    @Setter
    CommitteeEnum name;

    @Getter
    @Setter
    int year;
}
