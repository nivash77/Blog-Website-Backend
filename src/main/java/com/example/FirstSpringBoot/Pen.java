package com.example.FirstSpringBoot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Pen implements Writer {
    public void writer(){
        System.out.println("Writing in Pen");
    }
}
