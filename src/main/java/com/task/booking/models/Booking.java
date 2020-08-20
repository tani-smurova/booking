package com.task.booking.models;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Booking {
    //Модель сущности "Бронирование"

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="resource_id")
    private Resource resource;

    private Date date;

    private Date timeStart;

    private Date timeEnd;


    private Integer countPerson;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    private String description;

    public Booking(Resource resource, Date date, Integer countPerson, User user, String description) {
        this.resource = resource;
        this.date = date;
        this.countPerson = countPerson;
        this.user = user;
        this.description = description;
    }

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getCountPerson() {
        return countPerson;
    }

    public void setCountPerson(Integer countPerson) {
        this.countPerson = countPerson;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getUsername(){
        return user!= null ? user.getUsername() : "";
    }
    public String getNameRosource(){
        return resource!= null ? resource.getNameResource() : "";
    }

}
