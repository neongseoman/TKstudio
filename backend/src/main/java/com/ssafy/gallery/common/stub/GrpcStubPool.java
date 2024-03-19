package com.ssafy.gallery.common.stub;

import com.ssafy.pjt.grpc.CreateImageGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class GrpcStubPool {
    private final ManagedChannel channel;
    private final BlockingQueue<CreateImageGrpc.CreateImageBlockingStub> stubPool;
    private final String aiUrl = System.getenv("AI_URL");
    private final int poolSize = 5;
    public GrpcStubPool() {
        this.channel = ManagedChannelBuilder.forTarget(aiUrl).usePlaintext().build();
        this.stubPool = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            CreateImageGrpc.CreateImageBlockingStub stub = CreateImageGrpc.newBlockingStub(channel);
            stubPool.offer(stub);
        }
    }

    public CreateImageGrpc.CreateImageBlockingStub getStub() throws InterruptedException {
        return stubPool.take();
    }

    public void returnStub(CreateImageGrpc.CreateImageBlockingStub stub) {
        stubPool.offer(stub);
    }

    @PreDestroy
    public void close() {
        channel.shutdown();
    }
}