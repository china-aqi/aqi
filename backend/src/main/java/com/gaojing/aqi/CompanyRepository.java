package com.gaojing.aqi;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "company", path = "company")
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    List<Company> getByCode(@Param("code") String code);
}