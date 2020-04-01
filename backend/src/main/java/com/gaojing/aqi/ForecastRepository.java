package com.gaojing.aqi;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "forecast", path = "forecast")
public interface ForecastRepository extends PagingAndSortingRepository<Forecast, Long> {
    List<Forecast> getByCode(@Param("code") String code);
}