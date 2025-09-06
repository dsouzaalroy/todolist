package com.todolist.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories//("com.todolist.todolist.repository.*")
@ComponentScan//(basePackages = "com.todolist.todolist.*")
public class TodolistApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

}
