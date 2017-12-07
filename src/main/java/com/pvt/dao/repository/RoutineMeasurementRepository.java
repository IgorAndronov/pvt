package com.pvt.dao.repository;


import com.pvt.dao.entity.RoutineMeasurement;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Set;

public interface RoutineMeasurementRepository extends CrudRepository<RoutineMeasurement, Long> {

    Iterable<RoutineMeasurement> findByMeasurementUnitNameIsIn(Collection<String> unitNames);
}
