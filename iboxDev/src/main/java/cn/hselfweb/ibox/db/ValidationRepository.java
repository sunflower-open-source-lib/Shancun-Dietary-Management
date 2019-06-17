package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface ValidationRepository extends JpaRepository<Validation,Long> {

    List<Validation> findAllByTelOrderByDuedateDesc(String tel);

    List<Validation> findAllByTel(String tel);

}
