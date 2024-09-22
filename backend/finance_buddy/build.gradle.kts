plugins {
	kotlin("jvm") version "1.9.25" // Kotlin JVM 플러그인 설정
	kotlin("plugin.spring") version "1.9.25" // Spring 지원 Kotlin 플러그인
	kotlin("plugin.jpa") version "1.9.25" // JPA 관련 플러그인
	kotlin("plugin.noarg") version "1.9.25" // No-arg 플러그인
	id("org.springframework.boot") version "3.3.4" // Spring Boot 플러그인 설정
	id("io.spring.dependency-management") version "1.1.6" // Spring 의존성 관리
}

group = "com.klolarion"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17) // Java 17 버전 사용 설정
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral() // 중앙 저장소 사용
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA 사용
	implementation("org.springframework.boot:spring-boot-starter-jdbc") // JDBC 사용
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // OAuth2 클라이언트
	implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security
	implementation("org.springframework.boot:spring-boot-starter-validation") // 유효성 검사
	implementation("org.springframework.boot:spring-boot-starter-web") // Web Starter
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Kotlin을 위한 Jackson 모듈
	implementation("org.springframework.boot:spring-boot-starter-webflux") //ai호출

	developmentOnly("org.springframework.boot:spring-boot-devtools") // 개발 전용 도구
	runtimeOnly("com.h2database:h2") // H2 데이터베이스, 테스트 및 개발 환경용
	runtimeOnly("com.mysql:mysql-connector-j") // MySQL 연결 드라이버

	testImplementation("org.springframework.boot:spring-boot-starter-test") // 테스트 지원
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Kotlin JUnit5 지원
	testImplementation("org.springframework.security:spring-security-test") // Security 테스트 지원
	testRuntimeOnly("org.junit.platform:junit-platform-launcher") // JUnit 플랫폼 런처

	// JWT dependencies
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
}

tasks.withType<Test> {
	useJUnitPlatform() // JUnit Platform 사용
}

noArg {
	annotation("jakarta.persistence.Entity") // @Entity를 가진 클래스에 no-arg 생성자를 추가
}
