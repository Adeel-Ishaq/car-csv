package com.move.car.service;

import com.move.car.helper.CSVHelper;
import com.move.car.model.Car;
import com.move.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
//@EnableMongoRepositories
public class CarService {

    @Autowired
    private CarRepository repository;


    public void save(MultipartFile file) {
        try {
            List<Car> carList = CSVHelper.csvToCars(file.getInputStream());
            Iterable<Car> cars = carList;
            repository.saveAll(cars);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public List<Car> getAllCars() {
        Iterable<Car> cars = this.repository.findAll();
        List<Car> carList = new ArrayList<>();
        cars.forEach(guest -> carList.add(guest));
        carList.sort(new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                return Long.compare(o1.getId(), o2.getId());
            }
        });
        return carList;
    }
}