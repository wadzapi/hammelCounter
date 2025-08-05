package me.wadzapi.interview.vertices

import io.vertx.core.Promise
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.core.RxHelper


//TODO: separated Stat, Simulator AbstractVerticles
class MainVerticle : AbstractVerticle() {
    override fun start(promise: Promise<Void>) {
        RxHelper.deployVerticle(vertx, HttpServerVerticle())
            .subscribe(
                { promise.complete() },
                promise::fail)
    }
}