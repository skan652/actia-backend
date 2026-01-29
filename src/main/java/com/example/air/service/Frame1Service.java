package com.example.air.service;

import com.example.air.entity.Frame1;
import com.example.air.repository.Frame1Repository;
import com.example.air.webSocket.Frame1WebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class Frame1Service {
    @Autowired
    private final Frame1Repository frame1Repository;
    private List<Frame1> latestData;

    @Autowired
    private Frame1WebSocketHandler frame1WebSocketHandler;
    public void addFrameOne(int co2Value, int hchoValue, int tvocValue) {
        Frame1 frame = new Frame1();
        frame.setCO2(co2Value);
        frame.setHCHO(hchoValue);
        frame.setTVOC(tvocValue);
        frame.setDate(new Date());
        frame1Repository.save(frame);
        // Notify WebSocket clients for Frame1
        frame1WebSocketHandler.notifyClients(frame);
    }
    public Frame1 getLatestFrame1() {
        return frame1Repository.findTopByOrderByDateDesc();
    }
    public List<Frame1> getAllFrameone() {
        return frame1Repository.findAll();
    }

    public Frame1 getFrame1(String id) {
        return frame1Repository.findById(id).orElse(null);
    }

    public String deleteFrame1(String id) {
        Frame1 frame1 = frame1Repository.findById(id).orElse(null);
        if (frame1 == null) {
            return "this frame does not exsist!";
        } else {
            frame1Repository.delete(frame1);
            return "frame deleted successufully with id:  " + id;
        }
    }

    /////////////////////////////////// get data after scheduled period///////////
    @Scheduled(fixedRate = 10000) // Polling every 10 seconds (adjust as needed)
    public void pollDatabase() {
        List<Frame1> currentData = frame1Repository.findAll();

        if (latestData == null) {
            latestData = currentData;
            return;
        }

        List<Frame1> newData = currentData.stream()
                .filter(frame -> !latestData.contains(frame))
                .collect(Collectors.toList());

        if (!newData.isEmpty()) {
            System.out.println("New data: " + newData);
           // checkCO2(newData);
        }

        latestData = currentData;
    }

   /* private void checkCO2(List<Frame1> data) {
        for (Frame1 frame : data) {
            if (frame.getCO2() > 1400) {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setTo("mayssa.bensalah@etudiant-isi.utm.tn");
                emailRequest.setBody("The gas level has exceeded the threshold. Current level: " + frame.getCO2());
                emailService.sendEmail(emailRequest);
            }
        }
    }*/


}