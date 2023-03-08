package com.example.service;

import com.example.service.grpc.Request;
import com.example.service.grpc.Response;
import com.example.service.grpc.ExternalServiceGrpc.ExternalServiceImplBase;

import io.grpc.stub.StreamObserver;

public class ExternalService extends ExternalServiceImplBase {
    
    @Override
    public void toUpperCase(Request request, StreamObserver<Response> responseObserver){
        
        String message = request.getMessage();

        Response response = Response.newBuilder().setUpmessage(message.toUpperCase()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
