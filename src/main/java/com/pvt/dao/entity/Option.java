package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@Entity
public class Option extends BaseEntity<Long>{

    private String optionName;

    private OptionGroup optionGroup;


}
