package com.sree.ppm.repositories;

import com.sree.ppm.domains.BackLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackLogRepository extends CrudRepository<BackLog,Long> {
    BackLog findByProjectIdentifier(String projectId);
}
