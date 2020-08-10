package com.task.booking.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String nameResource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getNameResource() {
        return nameResource;
    }

    public void setNameResource(String nameResource) {
        this.nameResource = nameResource;
    }

}
