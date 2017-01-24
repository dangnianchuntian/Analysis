package com.dmsdbj.itoo.test.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ListControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	private static String ds="";
	
	@Test
	public void listTest() {
		ResponseEntity<String> re = this.restTemplate.getForEntity("/room"+ds, String.class);
		assertTrue(re.getStatusCode().is2xxSuccessful());
		System.out.println(re.getBody());
	}

}
