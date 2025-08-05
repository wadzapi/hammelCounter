package me.wadzapi.interview

import io.vertx.core.Launcher
import kotlinx.coroutines.runBlocking
import me.wadzapi.interview.vertices.MainVerticle
import me.wadzapi.interview.service.HamsterSensorSimulator

class CounterApplication

fun main(args: Array<String>) {
	val hamsterSimulator = HamsterSensorSimulator()

		runBlocking {
			hamsterSimulator.launchHamJobFlow()
		}
	Launcher.executeCommand("run", MainVerticle::class.java.name)
}
