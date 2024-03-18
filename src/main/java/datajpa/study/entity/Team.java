package datajpa.study.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor
@ToString(of = {"id","name"})
public class Team extends BaseEntity{
    @Id@GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")//1대 다로 양방향
    private List<Member> members=new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
