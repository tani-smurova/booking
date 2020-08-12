package com.task.booking.models;


import javax.persistence.*;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="resource_id")
    private Resource resource;

    private String timeBooking;

    private Integer countPerson;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    private String description;

    public Booking(Resource resource, String timeBooking, Integer countPerson, User user, String description) {
        this.resource = resource;
        this.timeBooking = timeBooking;
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

    public String getTimeBooking() {
        return timeBooking;
    }

    public void setTimeBooking(String timeBooking) {
        this.timeBooking = timeBooking;
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
