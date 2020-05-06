package com.gaojing.aqi;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "Qualityfactor", path = "qualityfactor")
public interface QualityfactorRepository extends PagingAndSortingRepository<Qualityfactor, Long> {
    List<Qualityfactor> getByCode(@Param("code") String code);
}