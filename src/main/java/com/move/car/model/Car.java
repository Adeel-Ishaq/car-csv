package com.move.car.model;

import javax.persistence.*;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @Column(name = "car_id")
    private long id;
    @Column(name = "make")
    private String make;
    @Column(name = "model")
    private String model;
    @Column(name = "price")
    private Long price;
    @Column(name = "condition_car")
    private String condition;
    @Column(name = "discount")
    private Long discountPrice;
    @Column(name = "date")
    private String date;

    protected Car() {}

    public Car( Long id, String make, String model, Long price, String condition, Long discountPrice, String date) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.price = price;
        this.condition = condition;
        this.discountPrice = discountPrice;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
