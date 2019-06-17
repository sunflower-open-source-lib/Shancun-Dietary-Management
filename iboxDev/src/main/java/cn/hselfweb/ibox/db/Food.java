package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "food")
public class Food {
    @Id
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_time")
    private Long foodTime;

    @Column(name = "food_url")
    private String foodUrl;

    @Column(name = "type")
    private Long type;

    @Column(name = "food_weight")
    private Long foodWeight;

    @Column(name = "food_parent")
    private Long foodParent;

    @Column(name = "comment")
    private String comment;

    @Column(name = "percent")
    private double percent;
}
