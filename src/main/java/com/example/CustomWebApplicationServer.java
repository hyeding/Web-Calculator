package com.example;

import com.example.calculate.Calculator;
import com.example.calculate.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CustomWebApplicationServer {
    private final int port;

    private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);
    public CustomWebApplicationServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("[CustomWebApplicationServer] started {} port.", port);
            Socket clientSocket;
            logger.info("[CustomWebApplicationServer] waiting for client.");

            while((clientSocket = serverSocket.accept()) != null) {
                logger.info("[CustomWebApplicationServer] client connected!");

                /**
                 * Step2 - 사용자 요청이 들어올 때마다 Thread를 새로 생성하여 사용자 요청을 처리하도록 한다.
                 * 단점 : 요청이 있을때마다 쓰레드를 생성하다보면 나중에는 서버가 다운될 수 있으므로 Thread 풀을 정해놓고 사용하는 것이 좋다.
                 */
                new Thread(new ClientRequestHandler(clientSocket)).start();
                }
            }
        }
    }
