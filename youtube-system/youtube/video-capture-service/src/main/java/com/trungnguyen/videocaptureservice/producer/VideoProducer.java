package com.trungnguyen.videocaptureservice.producer;

import com.trungnguyen.videocaptureservice.model.ProducerMessage;
import com.trungnguyen.videocaptureservice.model.mongo.Metadata;

public interface VideoProducer {

    void produceMessage(ProducerMessage<Metadata> message);
}
