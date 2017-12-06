package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@Entity
public class OptionGroup extends BaseEntity<Long> {

    private String name;

}
