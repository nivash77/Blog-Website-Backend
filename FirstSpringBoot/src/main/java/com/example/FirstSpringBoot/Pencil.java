package com.example.FirstSpringBoot;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Pencil implements Writer{

    public void writer(){
        System.out.println("Writing in pencil");
    }
}
