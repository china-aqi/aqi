package com.gaojing.aqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

@SpringBootApplication
public class AqiApplication {

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(AqiApplication.class);
		Map<String, Object> pro = newHashMap();
//		String ip = InetAddress.getLocalHost().getHostAddress();
//		pro.put("server.address", "192.168.0.7");
		pro.put("server.port", 8080);
		app.setDefaultProperties(pro);

		app.run(args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/security").allowedOrigins("http://192.168.0.7:8080");
//			}
//		};
//	}
}
