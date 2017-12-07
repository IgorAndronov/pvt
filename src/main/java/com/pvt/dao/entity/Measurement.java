package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.function.Function;

@Getter
@Setter
@ToString
@Entity
@Table(name="measurement")
public class Measurement extends BaseEntity<Long> implements Function<Measurement, Boolean> {

    public enum MeasurementOperationType {
        EQUALS,
        GT,
        LT,
    }

    @ManyToOne
    @JoinColumn(name = "measurement_unit_id")
    private MeasurementUnit measurementUnit;

    private MeasurementOperationType measurementOperationType = MeasurementOperationType.EQUALS;

    private String valueStart;

    private String valueEnd;

    public Boolean match(Measurement measurement) {
        return apply(measurement);
    }

    @Override
    public Boolean apply(Measurement measurement) {
        //TODO implements logic to compare measurmenets
        return null;
    }
}
