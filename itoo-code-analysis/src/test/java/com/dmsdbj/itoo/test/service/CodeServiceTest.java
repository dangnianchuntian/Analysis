package com.dmsdbj.itoo.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dmsdbj.itoo.Application;
import com.dmsdbj.itoo.service.CodeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CodeServiceTest {

	@Autowired
	private CodeService codeService;

	@Test
	public void readJavaFiles() throws Exception {
		int i = codeService
				//.readJavaFiles("E:/workspace/EJBFrame/ITOO/ITOO_TEAM/trunk/itoo-freshmen");
				.readJavaFiles("E:/ITOO-Java/code/workspace/EJB/ITOO/ITOO_TEAM/tags/before/itoo-cloud-tag-2016-12-21");
		System.out.println(i);
	}

	public void analysisEntity() throws Exception {
		boolean result = codeService.analysisEntity();
		System.out.println(result);
	}

	@Test
	public void analysisType() throws Exception {
		codeService.analysisType();
	}

	@Test
	public void analysisConCallSerCode() throws Exception {
		codeService.analysisConCallSerCode();
	}

	@Test
	public void analysisSerCallSerCode() throws Exception {
		codeService.analysisSerCallSerCode();
	}

	@Test
	public void getTableWhoCallSerData() throws Exception {
		codeService.getTableWhoCallSerData();
	}

	@Test
	public void getTableConCallSerData() throws Exception {
		codeService.getTableConCallSerData();
	}

}
