package com.example.config;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Config {
   
   @Autowired
   private Environment environment;
   
  @Bean
   public SparkContext sparkContext() {
       SparkConf conf = new SparkConf();
       conf.setAppName(environment.getProperty("spring.application.name") + "SparkContext")
       .setMaster(environment.getProperty("spark.master.endpoint"))
       .set("spark.cassandra.connection.host", environment.getProperty("cassandra.contact.endpoints"));
       for(String option:environment.getProperty("spark.settings").split(",")) {
           String[] keyVal = option.trim().split("=");
           conf.set(keyVal[0], keyVal[1]);
       }
       return new SparkContext(conf);
   }
}
