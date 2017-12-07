package com.pvt.dao.repository;

import com.pvt.dao.entity.MeasurementUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface MeasurementUnitRepository extends CrudRepository<MeasurementUnit,Long> {

    MeasurementUnit findByName(String name);
}
