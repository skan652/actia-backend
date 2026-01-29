package com.example.air.controller;

import com.example.air.entity.Frame1;
import com.example.air.service.Frame1Service;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frame1")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class Frame1Controller {
    @Autowired
    private final Frame1Service frame1service;
    @PostMapping("/add")
    public String addFrameOne(@RequestBody Frame1 frame){
        frame1service.addFrameOne(frame.getCO2(), frame.getHCHO(), frame.getTVOC());
        return "frame saved successufully! " ;
    }
    @GetMapping("/latest")
    public Frame1 getLatestFrame(){
        Frame1 latestFrame = frame1service.getLatestFrame1();
        System.out.println("Latest Frame: " + latestFrame);
        return  latestFrame;
    }

    @GetMapping
    public List<Frame1> fetchAllFrameone(){
        return frame1service.getAllFrameone();
    }

    @GetMapping("/getOne/{id}")
    public Frame1 getFrame1(@PathVariable String id){
        return frame1service.getFrame1(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFrame1(@PathVariable String id){
        return frame1service.deleteFrame1(id);
    }
}
