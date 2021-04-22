package com.sree.ppm;

import com.sree.ppm.api.v1.controller.ProjectController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class PpmApplicationTests {

    @Autowired
    ProjectController controller;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
