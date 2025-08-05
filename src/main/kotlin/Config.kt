import java.io.FileInputStream
import java.util.Properties

//TODO: ktlint_official  https://kotlinlang.org/docs/coding-conventions.html#function-names
class Config {
    val host: String
    val password: String
    val database: String

    init {
        val props = Properties()
        props.load(FileInputStream("env.properties"))

        host = props.getProperty("redis.host")
        password = props.getProperty("redis.password")
        database = props.getProperty("redis.database")
    }
}