package com.unio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UnioApplication

fun main(args: Array<String>) {
    runApplication<UnioApplication>(*args)
}
