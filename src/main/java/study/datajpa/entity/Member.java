package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA spec의 요구에 따라, parameter 없는 기본 생성자가 하나 있어야 한다. 아무나 호출하지 못하게 protected로.
@ToString(of = { "id", "username", "age" })
@NamedQuery(
        name = "Member.findByUsername",
        query = "Select m From Member m WHERE m.username = :username"
)
public class Member extends JpaBaseEntity {

    //-- 22강 Auditing : extends JpaBaseEntity 추가
    
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")  // 말하자면 foreign key
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
