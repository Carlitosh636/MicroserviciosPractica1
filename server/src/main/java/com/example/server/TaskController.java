package com.example.server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter; 

@RestController
public class TaskController {
	@Autowired
	RabbitTemplate rabbitTemplate;
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
	private final AtomicLong lastId = new AtomicLong();
    
    @GetMapping("/tasks/")
    public Collection<Task> getAllTasks(){
        return tasks.values();
    }
	
	@GetMapping("/tasks/{id}")
	public ResponseEntity<Integer> getProgress(@PathVariable long id){
		try {
			Task value = tasks.get(id);
			return new ResponseEntity<>(value.getProgress(),HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
		
	}

    @PostMapping("/tasks/")
	public ResponseEntity<Long> createNewTask(@RequestBody Task task) throws JsonProcessingException {
		System.out.println("Se va a realizar un post");
		long id = lastId.incrementAndGet();
		task.setId(id);
		tasks.put(id, task);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(task);
		rabbitTemplate.convertAndSend("newTasks", json);
		return new ResponseEntity<>(id,HttpStatus.CREATED);
	}
	@RabbitListener(queues = "tasksProgress", ackMode = "AUTO")
	public void received(String message) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Task task = mapper.readValue(message, Task.class);
		tasks.put(task.getId(), task);
		System.out.println("Task: "+tasks.get(task.getId()).toString());
	}
}
