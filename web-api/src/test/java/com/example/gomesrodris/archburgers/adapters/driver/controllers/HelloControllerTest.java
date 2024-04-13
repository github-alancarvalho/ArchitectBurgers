package com.example.gomesrodris.archburgers.app.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void index() {
        assertEquals("Welcome to the ArchitectBurgers WebAPI", controller.index());
    }
}