package com.dmsdbj.itoo.service;

import java.util.List;

import com.dmsdbj.itoo.entity.vo.D3data;
import com.dmsdbj.itoo.entity.vo.Data;
import com.dmsdbj.itoo.entity.vo.Table;
import com.dmsdbj.itoo.entity.vo.TableSerCallSer;
import com.dmsdbj.itoo.entity.vo.TableWhoCallSer;

public interface CodeService {
	public int readJavaFiles(String path) throws Exception;

	public boolean analysisEntity() throws Exception;

	public void analysisConCallSerCode() throws Exception;

	public void analysisSerCallSerCode() throws Exception;

	public void analysisType() throws Exception;

	public D3data getD3data() throws Exception;

	public Data getData() throws Exception;

	public List<Table> getTableConCallSerData() throws Exception;

	public List<TableSerCallSer> getTableSerCallSerData() throws Exception;

	public List<TableWhoCallSer> getTableWhoCallSerData() throws Exception;

	public Data getDataSerCallSer() throws Exception;

}
