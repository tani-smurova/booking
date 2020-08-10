package com.task.booking.repo;

import com.task.booking.models.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Long> {

    List<Resource> findByNameResourceContains(String nameResource);
}
