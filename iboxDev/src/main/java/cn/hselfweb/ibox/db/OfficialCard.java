package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "officialcard")
public class OfficialCard {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "foodid")
    private Long foodId;

}
