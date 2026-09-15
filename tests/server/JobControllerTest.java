package com.example.indeedaudit.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JobControllerTest {
    @Test
    void searchFiltersByTitle() {
        JobController controller = new JobController();
        var results = controller.search("devops");
        assertEquals(1, results.size());
        assertEquals("DevOps Engineer", results.get(0).getTitle());
    }
}
