package com.example.air.controller;

import com.example.air.entity.Frame2;
import com.example.air.service.Frame2Service;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frame2")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class Frame2Controller {
    @Autowired
    private final Frame2Service frame2service;


    @PostMapping("/add")
    public String addFrameOne(@RequestBody Frame2 frame){
        frame2service.addFrametwo(frame.getTemp(), frame.getHumidity());
        return "frame saved successufully! " ;
    }
    @GetMapping
    public List<Frame2> fetchAllFrames(){
        return frame2service.getAllFrames();
    }
    @GetMapping("/latest")
    public Frame2 getLatestFrame(){
        Frame2 latestFrame = frame2service.getLatestFrame1();
        System.out.println("Latest Frame: " + latestFrame);
        return  latestFrame;
    }
    @GetMapping("/getOne/{id}")
    public Frame2 getFrame2(@PathVariable String id){
        return frame2service.getFrame2(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFrame2(@PathVariable String id){
        return frame2service.deleteFrame2(id);
    }
}
