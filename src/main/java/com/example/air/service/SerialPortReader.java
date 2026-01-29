package com.example.air.service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SerialPortReader {

    private StringBuilder receivedDataBuffer = new StringBuilder(); // Buffer to store received data
    private final Frame1Service frame1Service;
    private final Frame2Service frame2Service;
    private final EmailService emailService ;
    public SerialPortReader(Frame1Service frame1Service, Frame2Service frame2Service, EmailService emailService) {
        this.frame1Service = frame1Service;
        this.frame2Service = frame2Service;
        this.emailService = emailService;

    }

    private int co2Value = 0;
    private int hchoValue = 0;
    private int tvocValue = 0;
    private int tempValue = 0;
    private int humValue = 0;
    @PostConstruct
    public void init() {
        SerialPort comPort = SerialPort.getCommPort("COM9");
        comPort.setBaudRate(115200);
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            System.out.println("Available port: " + port.getSystemPortName());
        }
        System.out.println("Attempting to open port: " + comPort.getSystemPortName());
        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.err.println("Failed to open port!");
            return;
        }
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                byte[] newData = new byte[comPort.bytesAvailable()];
                comPort.readBytes(newData, newData.length);

                String receivedData = new String(newData, StandardCharsets.UTF_8);

                // Append received data to the buffer
                receivedDataBuffer.append(receivedData);

                // Check if the buffer contains a complete line
                int newlineIndex = receivedDataBuffer.indexOf("\n");
                if (newlineIndex != -1) {
                    // Extract and process the complete line
                    String completeLine = receivedDataBuffer.substring(0, newlineIndex);
                    processReceivedData(completeLine);

                    // Remove the processed line from the buffer
                    receivedDataBuffer.delete(0, newlineIndex + 1);
                }
            }
        });
    }
    private void extractValues (String data) {
        if (data.contains("CO2 value:")) {
            String[] parts = data.split(":");
            if (parts.length >= 2) {
                String co2ValueStr = parts[1].trim();
                 co2Value = Integer.parseInt(co2ValueStr);
                 if (co2Value > 1200){
                     EmailRequest emailRequest = new EmailRequest();
                     emailRequest.setTo("mayssa.bensalah@etudiant-isi.utm.tn");
                     emailRequest.setBody("The gas level has exceeded the threshold. Current level: " + co2ValueStr);
                     emailService.sendEmail(emailRequest);
                 }
            } else {
                System.err.println("Invalid data format: " + data);
            }
        } else if (data.contains("HCHO value:")) {
            String[] parts = data.split(":");
            if (parts.length >= 2) {
                String hchoValueStr = parts[1].trim();
                 hchoValue = Integer.parseInt(hchoValueStr);
            } else {
                System.err.println("Invalid data format: " + data);
            }
        } else if (data.contains("TVOC value:")) {
            String[] parts = data.split(":");
            if (parts.length >= 2) {
                String tvocValueStr = parts[1].trim();
                 tvocValue = Integer.parseInt(tvocValueStr);
            } else {
                System.err.println("Invalid data format: " + data);
            }
        } else if (data.contains("Temperature value:")) {
            String[] parts = data.split(":");
            if (parts.length >= 2) {
                String tempValueStr = parts[1].trim();
                tempValue = Integer.parseInt(tempValueStr);
            } else {
                System.err.println("Invalid data format: " + data);
            }
        }else if (data.contains("Humidity value:")) {
            String[] parts = data.split(":");
            if (parts.length >= 2) {
                String humValueStr = parts[1].trim();
                humValue = Integer.parseInt(humValueStr);
            } else {
                System.err.println("Invalid data format: " + data);
            }
        }
    }

    private void processReceivedData(String data) {
        // Process and store the received data as needed (e.g., store in the database)
        System.out.println("Received data: " + data);
        extractValues(data);
        if (co2Value !=0 && hchoValue !=0 && tvocValue !=0){
            frame1Service.addFrameOne(co2Value, hchoValue, tvocValue);


        } else if (tempValue !=0 && humValue!=0) {
            frame2Service.addFrametwo(tempValue,humValue);

        }

    }


}
