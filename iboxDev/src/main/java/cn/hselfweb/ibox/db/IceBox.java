package cn.hselfweb.ibox.db;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "icebox")
public class IceBox {
    @Id
    @Column(name = "ice_id")
    private String iceId;

    @Column(name = "fid")
    private Long fid;

    @Column(name = "ice_name")
    private String iceName;

    public IceBox(){

    }

    public IceBox(String iceId,Long fid,String iceName){
        this.iceId = iceId;
        this.fid = fid;
        this.iceName = iceName;
    }

}
