package com.example.air.service;

import com.example.air.entity.Frame2;
import com.example.air.repository.Frame2Repository;
import com.example.air.webSocket.Frame2WebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class Frame2Service {
    @Autowired
    private final Frame2Repository frame2Repository;
    @Autowired
    private Frame2WebSocketHandler frame2WebSocketHandler;
    public void addFrametwo(int tempValue, int humValue) {
        Frame2 frame = new Frame2();
        frame.setTemp(tempValue);
        frame.setHumidity(humValue);
        frame.setDate(new Date());
        frame2Repository.save(frame);
        // Notify WebSocket clients for Frame2
        frame2WebSocketHandler.notifyClients(frame);
    }

    public List<Frame2> getAllFrames() {
        return frame2Repository.findAll();
    }

    public Frame2 getFrame2(String id) {
        return frame2Repository.findById(id).orElse(null);
    }
    public Frame2 getLatestFrame1() {
        return frame2Repository.findTopByOrderByDateDesc();
    }
    public String deleteFrame2(String id) {
        Frame2 frame2 = frame2Repository.findById(id).orElse(null);
        if (frame2 == null) {
            return "this frame does not exsist!";
        } else {
            frame2Repository.delete(frame2);
            return "frame deleted successufully with id:  " + id;
        }
    }


}
