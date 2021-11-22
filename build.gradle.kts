import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
}

group = "com.server"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.slf4j:slf4j-api")
	implementation("com.google.code.gson:gson:2.8.8")
    implementation("io.github.hcoona:native-named-mutex:1.3.0")
	implementation("no.ssb.locking:gcs-mutex:0.1")
    implementation("com.dorkbox:Coroutines:0.2.2")
	implementation("pl.clareo.coroutines:coroutines:1.2")
	implementation("de.sciss:coroutines_2.11:0.1.0")
	implementation("de.sciss:coroutines_2.12:0.1.0")
	implementation("com.storm-enroute:coroutines_2.11:0.7")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
