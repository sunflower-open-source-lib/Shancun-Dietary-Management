package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
//@IdClass(RecordKey.class)
@Getter
@Setter
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="record_id")
    private Long recordId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "ice_id")
    private String iceId;

    @Column(name = "fid")
    private Long fid;

    @Column(name = "opflag")
    private Long opFlag;//操作方式

    @Column(name = "opdate")
    private Date opDate;//操作时间

    @Column(name = "tareweight")
    private Long tareWeight;//皮重

    @Column(name = "foodweight")
    private Long foodWeight;//食物重

    @Column(name = "foodphoto")
    private String foodPhoto;//食物照片
}
