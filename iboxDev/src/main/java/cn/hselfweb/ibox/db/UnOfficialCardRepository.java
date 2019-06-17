package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UnOfficialCardRepository extends JpaRepository<UnOfficialCard,String>{
    UnOfficialCard getByUuid(String uuid);

    boolean existsByUuid(String uuid);
}
