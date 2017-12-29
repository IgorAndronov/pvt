package com.pvt.dao.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
@Table(name="option_group")
public class OptionGroup extends BaseEntity<Long> {

    private String name;


    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Option> options =  new ArrayList<>();

}
