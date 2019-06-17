package cn.hselfweb.ibox.db;


import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface IceBoxRepository extends Repository<IceBox,String> {
    IceBox getIceBoxByIceId(String macip);

    IceBox save(IceBox iceBox);
    int deleteByIceId(String iceId);
    List<IceBox> getAllByFid(Long fid);
}
