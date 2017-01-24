package com.dmsdbj.itoo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmsdbj.itoo.entity.vo.D3data;
import com.dmsdbj.itoo.entity.vo.Data;
import com.dmsdbj.itoo.entity.vo.Table;
import com.dmsdbj.itoo.entity.vo.TableSerCallSer;
import com.dmsdbj.itoo.entity.vo.TableWhoCallSer;
import com.dmsdbj.itoo.service.CodeService;

@Controller
@RequestMapping("/")
public class CodeController {

	@Autowired
	CodeService codeService;

	@GetMapping("/")
	public String index(Map<String, Object> model) {

		return "index";
	}

	@GetMapping("/PTPSerCallSer")
	public String PTPSerCallSer(Map<String, Object> model) {

		return "PTPSerCallSer";
	}

	@GetMapping("/tableConCallSer")
	public String tableConCallSer(Map<String, Object> model) throws Exception {
		List<Table> list = codeService.getTableConCallSerData();
		model.put("list", list);
		return "tableConCallSer";
	}

	@GetMapping("/tableSerCallSer")
	public String tableSerCallSer(Map<String, Object> model) throws Exception {
		List<TableSerCallSer> list = codeService.getTableSerCallSerData();
		model.put("list", list);
		return "tableSerCallSer";
	}

	@GetMapping("/tableWhoCallSer")
	public String tableWhoCallSer(Map<String, Object> model) throws Exception {
		List<TableWhoCallSer> list = codeService.getTableWhoCallSerData();
		model.put("list", list);
		return "tableWhoCallSer";
	}

	@GetMapping("/d3")
	public String d3(Map<String, Object> model) {
		System.out.println("888");
		return "d3";
	}

	@GetMapping("/d3data")
	@ResponseBody
	public D3data d3data() throws Exception {

		return codeService.getD3data();
	}

	@GetMapping("/data")
	@ResponseBody
	public Data data() throws Exception {

		System.out.println("6666");

		return codeService.getData();
	}

	@GetMapping("/PTPSerCallSerData")
	@ResponseBody
	public Data PTPSerCallSerData() throws Exception {

		System.out.println("7777");

		return codeService.getDataSerCallSer();
	}

}
