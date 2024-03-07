package com.ssafy.gallery.image.service;


import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.examples.lib.ImageRequest;
import net.devh.boot.grpc.examples.lib.ImageServiceGrpc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class grpcImageClientService {
    private static final Logger logger = LogManager.getLogger(grpcImageClientService.class);

    @GrpcClient("fastapi")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    public void sendImage(String name){
        String response = String.valueOf(this.imageServiceBlockingStub.imageSend(ImageRequest.newBuilder().setReqMessage("this is java").build()));
        logger.info(response);

    }
}
