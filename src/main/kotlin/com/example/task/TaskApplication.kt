package com.example.task

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition
@SpringBootApplication
class TaskApplication

fun main(args: Array<String>) {
	runApplication<TaskApplication>(*args)
}
