package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectRepository extends JpaRepository<Connect,Long> {
    Connect save(Connect s);
}
