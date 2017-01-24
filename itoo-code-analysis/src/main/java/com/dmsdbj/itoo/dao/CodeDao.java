package com.dmsdbj.itoo.dao;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dmsdbj.itoo.entity.po.Files;

@Repository
public interface CodeDao extends CrudRepository<Files, Long>{
	List<Files> findByContentLike(String key);
	
	List<Files> findByPathLike(String key);
	
	List<Files> findAll();
	
	List<Files> findByType(String type);
	
	List<Files> findByName(String name);
}
