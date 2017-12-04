package com.pvt.dao.entity;
import javax.persistence.*;
import java.util.List;

/**
 * Created by admin on 10.09.2017.
 */
@Entity
@Table(name = "External_User")
public class ExternalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name="name")
    private String name;
    @Column(name="url_icon")
    private String urlIcon;
    @Column(name = "priority")
    private Integer priority;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
