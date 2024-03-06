package com.ssafy.gallery.image.service;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.examples.lib.ImageRequest;
import net.devh.boot.grpc.examples.lib.ImageResponse;
import net.devh.boot.grpc.examples.lib.ImageServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService()
public class grpcImageService extends ImageServiceGrpc.ImageServiceImplBase {


    @Override
    public void imageSend(ImageRequest request, StreamObserver<ImageResponse> responseObserver) {
        super.imageSend(request, responseObserver);
    }
}
