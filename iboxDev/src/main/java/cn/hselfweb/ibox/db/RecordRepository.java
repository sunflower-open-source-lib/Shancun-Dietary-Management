package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.List;
@RepositoryRestResource(exported = false)
public interface RecordRepository extends JpaRepository<Record, RecordKey> {
    //List<Record> findByIceIdAndOpFlag(String macip,Long opFlag);
    List<Record> findAllByIceId(String macip);

    List<Record> findByUuidOrderByOpDateDesc(String uuid);

    Record findByIceIdAndUuid(String macip,String uuid);
    //List<Record> findByIceIdAndUuidOpDateDesc(String macip,String uuid);

    boolean existsByIceIdAndUuid(String macip,String uuid);

    Record save(Record record);
}
