package com.cmsz.upay.ioc.springtest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
public class MainApp {
   public static void main(String[] args) {
      ApplicationContext context = new FileSystemXmlApplicationContext
              ("D:/cmcc/IOCDemo_v1/src/main/resources/beans.xml");
      HelloWorld obj = (HelloWorld) context.getBean(HelloWorld.class);
      obj.getMessage();
   }
}
