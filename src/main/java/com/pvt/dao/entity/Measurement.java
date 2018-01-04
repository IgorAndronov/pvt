package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

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
public class Measurement extends BaseEntity<Long> implements Function<RoutineMeasurement, Boolean> {

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

    public Boolean match(RoutineMeasurement measurement) {
        return apply(measurement);
    }

    @Override
    public Boolean apply(RoutineMeasurement measurement) {
        if(measurement.getMeasurementUnit().getMeasurementUnitType() == MeasurementUnit.MeasurementUnitType.SINGLE) {
            switch (measurement.getMeasurementOperationType()) {
                case EQUALS:
                    return getValueStart().equalsIgnoreCase(measurement.getValueStart());
                case GT:
                    return Long.valueOf(getValueStart()) > Long.valueOf(measurement.getValueStart());
                case LT:
                    return Long.valueOf(getValueStart()) < Long.valueOf(measurement.getValueStart());
            }
        }else if(measurement.getMeasurementUnit().getMeasurementUnitType() == MeasurementUnit.MeasurementUnitType.RANGE){
            switch (measurement.getMeasurementOperationType()) {
                case EQUALS:
                case GT:
                case LT:
                    final Long start = Long.valueOf(getValueStart());
                    final Long end = StringUtils.isEmpty(getValueEnd()) ? Long.MAX_VALUE : Long.valueOf(getValueEnd());

                    return start >= Long.valueOf(measurement.getValueStart()) && end <=
                            (StringUtils.isEmpty(measurement.getValueEnd()) ? Long.MAX_VALUE : Long.valueOf(measurement.getValueEnd()));
            }
        }
        return false;
    }
}
