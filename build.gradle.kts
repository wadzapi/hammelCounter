buildscript {
	System.setProperty("kotlinVer", "2.1.0")
	System.setProperty("vertxVer", "4.5.11")
}

group = "mel.wadzapi.interview"
version = "0.0.1-SNAPSHOT"

plugins {
	val kotlinVer: String by System.getProperties()
	kotlin("jvm") version kotlinVer
	application
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	implementation("io.klogging:slf4j-klogging:0.11.4")

	val vertxVer: String by System.getProperties()
	implementation("io.vertx:vertx-core:${vertxVer}")
	implementation("io.vertx:vertx-lang-kotlin:${vertxVer}")
	implementation("io.vertx:vertx-web:${vertxVer}")
	implementation("io.vertx:vertx-rx-java3:${vertxVer}")
	implementation("io.vertx:vertx-rx-java3-gen:${vertxVer}")
	implementation("io.vertx:vertx-redis-client:${vertxVer}")

	implementation("org.uncommons:uncommons-maths:1.2")
	implementation("io.ktor:ktor-client-core:3.2.2")
	implementation("io.ktor:ktor-client-cio:3.2.2")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jar {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE

	manifest {
		attributes["Implementation-Title"] = rootProject.name
		attributes["Implementation-Version"] = archiveVersion
		attributes["Main-Class"] = "CounterApplicationKt"
	}

	val sourcesMain = sourceSets.main.get()
	val contents =
		configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } + sourcesMain.output
	from(contents)
}

	tasks.withType<Test> {
	useJUnitPlatform()
}

application {
	mainClass.set("mel.wadzapi.interview.CounterApplication")
}
