 package com.example.shoppingapi;

 import org.junit.jupiter.api.BeforeAll;
 import org.junit.jupiter.api.Test;
 import org.springframework.boot.test.context.SpringBootTest;

 @SpringBootTest(classes = ShoppingapiApplicationTests.class)
 class ShoppingapiApplicationTests {


     @BeforeAll
     public static void setUp() {
         DotenvLoader.load();
     }

 	@Test
 	void contextLoads() {
 	}

 }