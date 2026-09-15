package com.example.indeedaudit.controller;

import com.example.indeedaudit.model.Job;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JobController {

    // Placeholder in-memory data for the first vertical slice.
    // Replace with a real service/repository layer as the project grows.
    @GetMapping("/jobs")
    public List<Job> search(@RequestParam(defaultValue = "") String q) {
        List<Job> all = List.of(
            new Job("1", "DevOps Engineer", "Mohali, Punjab"),
            new Job("2", "Business Development Executive", "Chandigarh, Chandigarh")
        );
        return all.stream()
            .filter(j -> j.getTitle().toLowerCase().contains(q.toLowerCase()))
            .toList();
    }
}
