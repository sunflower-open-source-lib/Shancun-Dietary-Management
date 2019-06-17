package cn.hselfweb.ibox.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class FamilyKey implements Serializable {
    private Long fid;
    private Long uid;
}
