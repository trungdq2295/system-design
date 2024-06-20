package com.trungnguyen.videocaptureservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProducerMessage<T> {

    private int retry = 0;

    private T data;

    public ProducerMessage(T data) {
        this.data = data;
    }

    public static <T> ProducerMessage<T> buildMessage(T data){
        return new ProducerMessage<T>(data);
    }
}
