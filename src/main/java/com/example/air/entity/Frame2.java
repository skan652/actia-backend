package com.example.air.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
@Data
@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Frame2 {
    @Id
    private String id;
    private int temp;
    private int humidity;
    private Date date;






    public Frame2(int temp, int humidity, Date date) {
        this.temp = temp;
        this.humidity = humidity;
        this.date = date;
    }
}
