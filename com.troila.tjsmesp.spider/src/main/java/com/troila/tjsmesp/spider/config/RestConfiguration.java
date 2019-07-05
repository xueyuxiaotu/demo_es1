package com.troila.tjsmesp.spider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration 
public class RestConfiguration { 
	
	@Autowired 
	RestTemplateBuilder builder; 
	
	@Bean 
	public RestTemplate restTemplate(){ 	
		builder.setConnectTimeout(10*1000);
		builder.setReadTimeout(60*1000);
		return builder.build(); 
	} 
	
} 