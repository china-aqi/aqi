package com.gaojing.aqi;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "indicator", path = "indicator")
public interface IndicatorRepository extends PagingAndSortingRepository<Indicator, Long> {
    List<Indicator> getByCode(@Param("code") String code);
}