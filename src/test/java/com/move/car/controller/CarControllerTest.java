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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    @Nested
    class SaveCars {

        @Test
        void shouldSaveListOfCarsForUploadedFile() throws Exception {
            // given
            var correctFile = getFileFor("/idealData_car.csv", "text/csv");

            // then
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload")
                            .file(correctFile))
                            .andExpect(status().isOk());
        }

        @Test
        void shouldRetrunBadRequestForBadFormatFile() throws Exception {
            // given
            var corruptFile = getFileFor("/badFormat_car.json", "text/json");

            // then
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload").file(corruptFile))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertEquals("{\"message\":\"Please upload a csv file!\"}",
                            result.getResponse().getContentAsString()));
        }

        @Test
        void shouldRetrunExpectationFailedErrorForBrokenDataFile() throws Exception {
            // given
            var fileName = "brokenData_car.csv";
            var corruptFile = getFileFor("/" + fileName, "text/csv");

            // when
            doThrow(InternalException.class)
                    .when(carService)
                    .save(any());

            // then
            mockMvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload").file(corruptFile))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(result ->
                            assertEquals("{\"message\":\"Could not upload the file: " + fileName + "!\"}",
                            result.getResponse().getContentAsString()));
        }

        private MockMultipartFile getFileFor(String filePath, String contentType) throws IOException {
            var fileResource = new ClassPathResource(filePath);
            var file = new MockMultipartFile("file", fileResource.getFilename(), contentType,
                    fileResource.getInputStream());
            return file;
        }
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