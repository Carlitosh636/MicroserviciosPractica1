package com.example.service;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServiceApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = ServerBuilder
			.forPort(8888)
			.addService(new ExternalService()).build();
		server.start();
		server.awaitTermination();
	}

}
