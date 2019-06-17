package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface FamilyRepository extends JpaRepository<Family,Long> {
    List<Family> findAllByUid(Long uid);
    List<Family> findAllByFid(Long fid);
    Family save(Family family);
    void deleteByFidAndUid(Long fid, Long uid);
    Family findByFidAndUid(Long fid, Long uid);
    List<Family> findAllByOrderByFidDesc();
}
