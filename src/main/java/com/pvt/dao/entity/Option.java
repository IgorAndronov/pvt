package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name="option")
public class Option extends BaseEntity<Long>{

    @Column
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;


}
