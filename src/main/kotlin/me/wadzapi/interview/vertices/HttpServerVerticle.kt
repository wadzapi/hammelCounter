package me.wadzapi.interview.vertices

import Config
import io.reactivex.rxjava3.core.Observable
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.redis.client.RedisOptions
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router
import io.vertx.rxjava3.ext.web.RoutingContext
import io.vertx.rxjava3.ext.web.handler.BodyHandler
import io.vertx.rxjava3.redis.client.Redis
import io.vertx.rxjava3.redis.client.RedisAPI
import io.vertx.rxjava3.redis.client.Response

//TODO: implement ktor-server instead
class HttpServerVerticle  : AbstractVerticle() {
    private lateinit var redisApi: RedisAPI

    override fun start(promise: Promise<Void>) {
        val config = Config()
        val redisOptions = RedisOptions()
            .setConnectionString("redis://:${config.password}@${config.host}/${config.database}")

        Redis(io.vertx.redis.client.Redis.createClient(vertx.delegate, redisOptions))
            .rxConnect()
            .subscribe(
                { redisConnection ->
                    redisApi = RedisAPI.api(redisConnection)
                },
                { failure -> promise.fail(failure.cause) })

        val router = Router.router(vertx).apply {
            get("/api/hamsters").handler(this@HttpServerVerticle::getHamsters)
            post("/api/event").handler(BodyHandler.create()).handler(this@HttpServerVerticle::processEvent)
        }

        vertx
            .createHttpServer()
            .requestHandler(router)
            .rxListen(8082)
            .subscribe(
                { promise.complete() },
                { failure -> promise.fail(failure.cause) })
    }

    private fun getHamsters(context: RoutingContext) {
        var response: JsonObject
        var count = 0
        var dataSize = 0
        val hamsters = ArrayList<JsonObject>()

        redisApi
            .rxKeys("hamsters:*")
            .toObservable()
            .map { keys ->
                ArrayList<Observable<Response>>().apply {
                    keys.forEach {
                        add(redisApi
                            .rxHmget(ArrayList<String>().apply {
                                add(it.toString())
                                add("id")
                                add("name")
                                add("group")
                            })
                            .toObservable())
                    }
                }
            }
            .flatMap {
                dataSize = it.size
                Observable.concat(it)
            }
            .subscribe(
                {
                    count++
                    val data = it.iterator()
                    hamsters.add(JsonObject().apply {
                        put("id", data.next().toString())
                        put("name", data.next().toString())
                        put("nameAlias", data.next().toString())
                        put("group", data.next().toString())
                    })

                    if (count >= dataSize) {
                        response = JsonObject().apply {
                            put("success", true)
                            put("data", hamsters)
                        }

                        putResponse(context, 200, response)
                    }
                },
                {
                    response = JsonObject().apply {
                        put("success", false)
                        put("message", it.message)
                    }

                    putResponse(context, 500, response)
                })
    }

    private fun processEvent(context: RoutingContext) {
        var response: JsonObject

        val id = context.request().getParam("id")
        val name = context.request().getParam("name")
        val group = context.request().getParam("group")

        redisApi
            .rxHmset(ArrayList<String>().apply {
                add("hamsters:${id}")
                add("hamsterId")
                add(id)
                add("name")
                add(name)
                add("group")
                add(group)
            })
            .subscribe(
                {
                    response = JsonObject().apply {
                        put("success", true)
                        put("action", "insert")
                    }

                    putResponse(context, 200, response)
                },
                {
                    response = JsonObject().apply {
                        put("success", false)
                        put("message", it.message)
                    }

                    putResponse(context, 500, response)

                })
    }

    private fun putResponse(context: RoutingContext, statuscode: Int, response: JsonObject) {
        context.response().statusCode = statuscode

        context.response().putHeader("Content-Type", "application/json")
        context.response().end(response.encode())
    }
}