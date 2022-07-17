package com.move.car.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.move.car.model.Car;
import com.move.car.service.CarService;
import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CarController.class)
class CarControllerTest {
    @MockBean
    private CarService carService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateListOFCars_afterUploadingCorrectFile() {

    }



    @Nested
    class GetAllCars {
        @Test
        void shouldReturnSavedListOfCars() throws Exception {
            // given
            var car1 = new Car(Long.parseLong("1"), "testMake1", "testModel1", Long.parseLong("2300"),
                    "testCondition1", Long.parseLong("600"), "2007-6-21");
            var car2 = new Car(Long.parseLong("2"), "testMake2", "testModel2", Long.parseLong("4500"),
                    "testCondition1", Long.parseLong("1200"), "2022-3-12");
            var carList = new ArrayList<Car>(Arrays.asList(car1, car2));

            // when
            when(carService.getAllCars()).thenReturn(carList);

            // then
            var result = mockMvc.perform(get("/api/csv/cars"))
                    .andExpect(status().isOk())
                    .andReturn();

            var resultJson = result.getResponse().getContentAsString();
            var resultList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Iterable<Car>>() {});
            assertThat(resultList).usingRecursiveComparison().isEqualTo(carList);
        }

        @Test
        void shouldReturnNoContentForEmptyDB() throws Exception {
            // given
            var emptyList = new ArrayList<Car>();

            // when
            when(carService.getAllCars()).thenReturn(emptyList);

            // then
            mockMvc.perform(get("/api/csv/cars"))
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldThrowsExceptionForInternalError() throws Exception {
            // given
            var emptyList = new ArrayList<Car>();

            // when
            when(carService.getAllCars()).thenThrow(InternalException.class);

            // then
            mockMvc.perform(get("/api/csv/cars"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> assertEquals(500, result.getResponse().getStatus()))
                    .andExpect(result -> assertEquals(null, result.getResponse().getContentType()));
        }
    }
}