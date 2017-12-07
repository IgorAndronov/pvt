package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name="routine_measurement")
public class RoutineMeasurement extends BaseEntity<Long> {


    @ManyToOne()
    @JoinColumn(name = "measurement_unit_id")
    private MeasurementUnit measurementUnit;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private Measurement.MeasurementOperationType measurementOperationType = Measurement.MeasurementOperationType.EQUALS;

    private String valueStart;

    private String valueEnd;

}
