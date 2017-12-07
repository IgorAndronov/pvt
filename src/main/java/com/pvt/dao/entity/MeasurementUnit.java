package com.pvt.dao.entity;


/*
 * Height,
 * Weight,
 * Age,
 *
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name="measurement_unit")
public class MeasurementUnit extends BaseEntity<Long> {

    public enum MeasurementUnitType {
        SINGLE,
        RANGE,

    }

    private String name;

    private MeasurementUnitType measurementUnitType = MeasurementUnitType.SINGLE;
}
