package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@IdClass(FamilyKey.class)
@Entity
@Table(name = "family")
public class Family {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;

    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private Long role;

}
