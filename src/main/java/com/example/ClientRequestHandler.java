package com.example;

import com.example.calculate.Calculator;
import com.example.calculate.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientRequestHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);
    private final Socket clientSocket;
    public ClientRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {

        logger.info("[clientRequestHandler] new client {} started.", Thread.currentThread().getName());
        try(InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

//                    // 아래 코드로 http 구조 확인할 수 있음
//                    String line;
//                    while((line = br.readLine()) != "") {
//                        System.out.println(line);
//                    }

            HttpRequest httpRequest = new HttpRequest(br);

            // GET /calculate?operand1=11&operator=*&operand2=55  HTTP/1.1
            if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
                QueryStrings queryStrings = httpRequest.getQueryStrings();

                int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
                String operator = queryStrings.getValue("operator");
                int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

                int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
                byte[] body = String.valueOf(result).getBytes();

                HttpResponse response = new HttpResponse(dos);
                response.response200Header("application/json", body.length);
                response.responseBody(body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
