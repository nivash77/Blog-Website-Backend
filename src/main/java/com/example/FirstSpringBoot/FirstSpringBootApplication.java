package com.example.FirstSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@SpringBootApplication
public class FirstSpringBootApplication {

	public static void main(String[] args) throws IOException {

//		ApplicationContext context= SpringApplication.run(FirstSpringBootApplication.class, args);
//		Student student= context.getBean(Student.class);
//        student.show();
//		student.age=10;
//		Student student1=context.getBean(Student.class);
//
//		System.out.println(student.age+" "+student1.age);
//		student.write();
		SpringApplication.run(FirstSpringBootApplication.class,args);

	}


}
