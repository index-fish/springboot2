package com.example.springboot2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProjectControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(print()).build();
    }

    @Test
    void addProject() throws Exception {
        mockMvc.perform(get("/project/addProject")
                .param("userName", "test01")
                .param("password", "123")
                .param("projectName", "tp1")).andExpect(status().isOk());

    }

    @Test
    void deleteProject() {
    }

    @Test
    void addFile() {
    }

    @Test
    void addDevelopers() {
    }

    @Test
    void deleteDevelopers() {
    }

    @Test
    void upload() {
    }

    @Test
    void getAuthority() {
    }

    @Test
    void getProject() {
    }
}