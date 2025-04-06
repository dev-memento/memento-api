package com.official.memento

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MementoApplication

fun main(args: Array<String>) {
    runApplication<MementoApplication>(*args)
}
