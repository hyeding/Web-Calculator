package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryStringTest {

    // 여러개인 경우 List<QueryString>
    @Test
    void createTest(){
        QueryString queryString = new QueryString("operand","11");
        assertThat(queryString).isNotNull();

    }
}
