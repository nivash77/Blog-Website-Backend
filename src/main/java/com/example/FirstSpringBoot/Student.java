package com.example.FirstSpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Component
@Scope("prototype")
@RestController

public class Student  {
//    @Autowired
//    public void setPen(Pen pen) {
//        this.pen = pen;
//    }

    @Value("21")
    int age;


    @Value("20")
    public void setAge(int age){
        this.age=age;
    }


    private Writer writer;
    @Autowired
    @Qualifier("pencil")
    public void setWriter(Writer writer) {
        this.writer = writer;
    }
    @GetMapping("/")
    public String show(){

        //System.out.println("Student object injected");
        return "Hello World!";
    }
    @GetMapping("/getObject")
    public String getObjects(){
        return "Student object";

    }
    public void write(){
        writer.writer();
    }

}
