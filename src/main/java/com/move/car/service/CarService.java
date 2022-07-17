package com.move.car.service;

import com.move.car.model.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {
    void save(MultipartFile file);
    List<Car> getAllCars();
}
