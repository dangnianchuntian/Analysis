package com.dmsdbj.itoo.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dmsdbj.itoo.entity.po.Relation;

@Repository
public interface RelationDao extends CrudRepository<Relation, Long> {
	List<Relation> findAll();

	List<Relation> findBySource(Long source);

	List<Relation> findByTarget(Long target);

	List<Relation> findBySourceAndType(Long source, String type);

}
