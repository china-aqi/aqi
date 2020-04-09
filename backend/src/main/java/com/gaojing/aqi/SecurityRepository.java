package com.gaojing.aqi;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "Security", path = "security")
public interface SecurityRepository extends PagingAndSortingRepository<Security, Long> {
    List<Security> findFirst7ByMatchContaining(@Param("match") String match);

    @Query(value = "select s.code from Security s where s.displayname = :name")
    String getByName(String name);
}