package com.maijiantian.wsdemo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class WsdemoApplication {
    public static String port;

    public static void main(String[] args) {

        //SpringApplication.run(WsdemoApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        port = scanner.nextLine();
        new SpringApplicationBuilder(WsdemoApplication.class).properties("server.port=" + port).run(args);
    }
}
