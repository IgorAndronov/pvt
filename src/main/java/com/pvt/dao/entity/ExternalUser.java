package com.pvt.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by admin on 10.09.2017.
 */
@Entity
@Table(name = "External_User")
@Getter
@Setter
@ToString
public class ExternalUser extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "url_icon")
    private String urlIcon;

    @Column(name = "priority")
    private Integer priority;
}



