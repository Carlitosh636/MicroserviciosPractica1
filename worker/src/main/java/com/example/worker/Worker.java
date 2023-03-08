package com.example.worker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.example.service.grpc.*;
import com.example.service.grpc.ExternalServiceGrpc.ExternalServiceBlockingStub;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Worker {
	@Autowired
	RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "newTasks", ackMode = "AUTO")
	public void received(String message) throws InterruptedException, JsonProcessingException {
		int progress = 0;
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		System.out.println("Message: "+message);
		ObjectMapper mapper = new ObjectMapper();
		Task task = mapper.readValue(message, Task.class);
		ManagedChannel channel = ManagedChannelBuilder.forAddress("service",8888).usePlaintext().build();

		ExternalServiceBlockingStub stub = ExternalServiceGrpc.newBlockingStub(channel);

		Response response = stub.toUpperCase(Request.newBuilder()
			.setId(String.valueOf(task.getId()))
			.setMessage(task.getContent())
			.build());
		while(progress<100){
			Thread.currentThread().sleep(100);
			progress+=1;
			task.setProgress(progress);
			if((progress % 10 == 0) && (progress<100)){
				rabbitTemplate.convertAndSend("tasksProgress",ow.writeValueAsString(task));
			}
		}
		System.out.println("Progreso al 100%");
		
		task.setContent(response.getUpmessage());
		task.setCompleted(true);
		String json = ow.writeValueAsString(task);
		rabbitTemplate.convertAndSend("tasksProgress", json);
		channel.shutdown();
	}
}
