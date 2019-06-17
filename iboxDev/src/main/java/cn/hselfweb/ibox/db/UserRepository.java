package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //List<User> findAllByFid(Long fid);
    User save(User user);
    User findByTelAndPassword(String tel,String password);
    User findByTel(String tel);
    User findByUid(Long uid);
}
