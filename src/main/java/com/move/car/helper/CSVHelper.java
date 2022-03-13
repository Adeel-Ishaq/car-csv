package com.move.car.helper;

import com.move.car.model.Car;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String TYPE = "text/csv";
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

        public static List<Car> csvToCars(InputStream is) {

            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

                List<Car> cars = new ArrayList<Car>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    Car car = new Car (
                            Long.parseLong(csvRecord.get("Id")),
                            csvRecord.get("Make"),
                        csvRecord.get("Model"),
                            Long.parseLong(csvRecord.get("Price")),
                        csvRecord.get("Condition"),
                            Long.parseLong(csvRecord.get("DiscountPrice")),
                        csvRecord.get("Date")
                );
                    cars.add(car);
            }
            return cars;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}