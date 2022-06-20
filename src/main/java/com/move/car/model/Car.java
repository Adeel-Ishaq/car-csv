package com.move.car.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "car")
public class Car {
    @Id
    public long id;
    public String make;
    public String model;
    public Long price;
    public String condition;
    public Long discountPrice;
    public String date;

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
}