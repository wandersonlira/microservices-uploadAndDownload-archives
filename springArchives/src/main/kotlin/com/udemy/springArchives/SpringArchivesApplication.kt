package com.udemy.springArchives

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
@ConfigurationPropertiesScan
class SpringArchivesApplication

fun main(args: Array<String>) {
	runApplication<SpringArchivesApplication>(*args)
}
