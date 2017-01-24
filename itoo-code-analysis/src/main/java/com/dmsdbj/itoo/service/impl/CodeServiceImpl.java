package com.dmsdbj.itoo.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmsdbj.itoo.dao.CodeDao;
import com.dmsdbj.itoo.dao.RelationDao;
import com.dmsdbj.itoo.entity.po.Files;
import com.dmsdbj.itoo.entity.po.Relation;
import com.dmsdbj.itoo.entity.vo.D3data;
import com.dmsdbj.itoo.entity.vo.D3link;
import com.dmsdbj.itoo.entity.vo.D3node;
import com.dmsdbj.itoo.entity.vo.Data;
import com.dmsdbj.itoo.entity.vo.Edge;
import com.dmsdbj.itoo.entity.vo.Node;
import com.dmsdbj.itoo.entity.vo.Table;
import com.dmsdbj.itoo.entity.vo.TableSerCallSer;
import com.dmsdbj.itoo.entity.vo.TableWhoCallSer;
import com.dmsdbj.itoo.service.CodeService;

@Component("CodeService")
public class CodeServiceImpl implements CodeService {

	@Autowired
	CodeDao codeDao;

	@Autowired
	RelationDao relationDao;

	int count = 0;

	@Override
	public int readJavaFiles(String path) throws Exception {

		traverseFolder(path);
		return 0;
	}

	private void traverseFolder(String path) throws Exception {

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				// System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						// System.out.println("文件夹:" + file2.getAbsolutePath());
						traverseFolder(file2.getAbsolutePath());
					} else {
						if (file2.getName().endsWith(".java")) {
							String name = file2.getName();
							name = name.substring(0, name.indexOf("."));
							String content = FileUtils.readFileToString(file2);
							String fpath = file2.getAbsolutePath();
							fpath = fpath.substring(fpath.indexOf("trunk") + 6);
							System.out.println(fpath);

							Files f = new Files();
							f.setContent(content);
							f.setName(name);
							f.setPath(fpath);

							codeDao.save(f);
						}
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}

	}

	@Override
	public boolean analysisEntity() throws Exception {
		List<Files> list = codeDao.findByPathLike("%entity%");
		for (Files file : list) {

			String name = file.getName();
			name = "%<" + name + ">%";
			List<Files> targets = codeDao.findByContentLike(name);

			for (Files tar : targets) {
				Relation rel = new Relation();
				rel.setSource(file.getId());
				rel.setTarget(tar.getId());
				relationDao.save(rel);
			}
		}
		return true;
	}

	@Override
	public D3data getD3data() throws Exception {

		D3data data = new D3data();

		List<D3node> nodes = new ArrayList<>();

		data.setNodes(nodes);

		List<Files> list = codeDao.findByType("controller");
		for (Files file : list) {
			D3node node = new D3node();
			node.setId(file.getId().toString());
			node.setGroup("controller");
			node.setName(file.getName());
			nodes.add(node);
		}

		List<Files> listservice = codeDao.findByType("serviceImpl");
		for (Files file : listservice) {
			D3node node = new D3node();
			node.setId(file.getId().toString());
			node.setGroup("serviceImpl");
			node.setName(file.getName());
			nodes.add(node);
		}

		List<D3link> links = new ArrayList<>();
		List<Relation> relation = relationDao.findAll();
		for (Relation r : relation) {
			D3link link = new D3link();
			link.setSource(r.getSource().toString());
			link.setTarget(r.getTarget().toString());
			link.setValue("1");
			links.add(link);
		}

		data.setLinks(links);
		return data;
	}

	@Override
	public void analysisType() throws Exception {
		List<Files> list = codeDao.findAll();

		for (Files file : list) {
			String path = file.getPath();
			if (path.contains("entity")) {
				file.setType("entity");
			} else if (path.contains("service") || path.contains("Service")) {
				if (path.contains("impl") || path.contains("Impl")) {
					file.setType("serviceImpl");
				} else {
					file.setType("service");
				}

			} else if (path.contains("eao")) {
				if (path.contains("impl")) {
					file.setType("eaoImpl");
				} else {
					file.setType("eao");
				}
			} else if (path.contains("controller")) {
				file.setType("controller");
			}
			codeDao.save(file);
		}

	}

	@Override
	public void analysisConCallSerCode() throws Exception {
		// 分析Controller
		List<Files> conList = codeDao.findByType("controller");

		for (Files con : conList) {
			String content = con.getContent();
			// 得到使用了那些serviceBean
			// System.out.println(con.getName());
			String impor = content.substring(0, content.indexOf("public"));
			String[] imps = impor.split(";");
			for (String imp : imps) {
				if (imp.contains("service")) {
					// System.out.println(imp);
					if (imp.contains("service") && imp.contains("Bean")) {
						int serviceIndex = imp.indexOf("service") + 8;
						int beanIndex = imp.indexOf("Bean") + 4;
						if (serviceIndex > beanIndex) {
							System.out.println("类" + imp + "在注释和Pack中不符合要求");
							break;
						}
						String beanName = imp
								.substring(serviceIndex, beanIndex);
						System.out.println(beanName);
						// 找到相应的实现类
						List<Files> bean = codeDao
								.findByContentLike("%@Remote(" + beanName
										+ ".class)%");
						// List<Files>
						// bean=codeDao.findByContentLike("%implements "+beanName+"%");
						if (bean.size() > 0) {
							// System.out.println(bean.get(0).getName());
						} else {
							bean = codeDao
									.findByContentLike("%@Remote(value = "
											+ beanName + ".class)%");
							if (bean.size() > 0) {
								System.out.println(bean.get(0).getName());

							} else {
								bean = null;
								System.out.println("未找到");
							}
						}
						if (bean != null) {
							// 插入关系
							Relation rel = new Relation();
							rel.setSource(con.getId());
							rel.setTarget(bean.get(0).getId());
							rel.setType("concallser");
							relationDao.save(rel);
						}
					}
				}
			}
		}

	}

	@Override
	public void analysisSerCallSerCode() throws Exception {
		// 分析Service
		List<Files> serList = codeDao.findByType("serviceImpl");
		// List<Files> serList1 = codeDao.findByType("service");
		// serList.addAll(serList1);

		for (Files ser : serList) {
			String content = ser.getContent();
			// 得到使用了那些serviceBean
			// System.out.println(con.getName());
			String impor = content.substring(0, content.indexOf("public"));
			String[] imps = impor.split(";");
			for (String imp : imps) {
				if (imp.contains("service")) {
					// System.out.println(imp);
					if (imp.contains("service") && imp.contains("Bean")) {
						int serviceIndex = imp.indexOf("service") + 8;
						int beanIndex = imp.indexOf("Bean") + 4;
						if (serviceIndex > beanIndex) {
							System.out.println("类" + imp + "在注释和Pack中不符合要求");
							break;
						}
						String beanName = imp
								.substring(serviceIndex, beanIndex);
						System.out.println(beanName);
						// 找到相应的实现类
						List<Files> bean = codeDao
								.findByContentLike("%@Remote(" + beanName
										+ ".class)%");
						// List<Files>
						// bean=codeDao.findByContentLike("%implements "+beanName+"%");
						if (bean.size() > 0) {
							// System.out.println(bean.get(0).getName());
						} else {
							bean = codeDao
									.findByContentLike("%@Remote(value = "
											+ beanName + ".class)%");
							if (bean.size() > 0) {
								System.out.println(bean.get(0).getName());

							} else {
								bean = null;
								System.out.println("未找到");
							}
						}
						if (bean != null) {
							// 插入关系
							Relation rel = new Relation();
							long sourcedId = ser.getId();
							long targetId = bean.get(0).getId();
							if (sourcedId != targetId) {
								rel.setSource(sourcedId);
								rel.setTarget(targetId);
								rel.setType("sercallser");
								relationDao.save(rel);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Data getData() throws Exception {
		int y = 0;
		int y2 = 0;

		Data data = new Data();
		List<Node> nodes = new ArrayList<>();
		List<Files> list = codeDao.findByType("controller");
		for (Files file : list) {
			Node node = new Node();
			node.setId(file.getId());
			node.setLabel(file.getName());
			node.setSize(1);
			node.setY(y);
			nodes.add(node);
			y = y + 30;
		}

		List<Files> listcon = codeDao.findByType("serviceImpl");
		for (Files file : listcon) {
			Node node = new Node();
			node.setId(file.getId());
			node.setLabel(file.getName());
			node.setSize(1);
			node.setX(1000);
			node.setY(y2);
			nodes.add(node);
			y2 = y2 + 30;
		}

		List<Edge> links = new ArrayList<>();
		List<Relation> relation = relationDao.findAll();
		for (Relation r : relation) {
			Edge link = new Edge();
			link.setSource(r.getSource());
			link.setTarget(r.getTarget());
			link.setId(r.getId());
			links.add(link);
		}

		data.setEdges(links);

		data.setNodes(nodes);
		return data;
	}

	@Override
	public Data getDataSerCallSer() throws Exception {
		int y = 0;
		int y2 = 0;

		Data data = new Data();
		List<Node> nodes = new ArrayList<>();
		List<Files> list = codeDao.findByType("serviceImpl");
		for (Files file : list) {
			Node node = new Node();
			node.setId(file.getId());
			node.setLabel(file.getName());
			node.setSize(1);
			node.setY(y);
			nodes.add(node);
			y = y + 30;
		}

		List<Files> listcon = codeDao.findByType("serviceImpl");
		for (Files file : listcon) {
			Node node = new Node();
			node.setId(file.getId());
			node.setLabel(file.getName());
			node.setSize(1);
			node.setX(1000);
			node.setY(y2);
			nodes.add(node);
			y2 = y2 + 30;
		}

		List<Edge> links = new ArrayList<>();
		List<Relation> relation = relationDao.findAll();
		for (Relation r : relation) {
			Edge link = new Edge();
			link.setSource(r.getSource());
			link.setTarget(r.getTarget());
			link.setId(r.getId());
			links.add(link);
		}

		data.setEdges(links);

		data.setNodes(nodes);
		return data;
	}

	@Override
	public List<Table> getTableConCallSerData() throws Exception {
		List<Table> tableList = new ArrayList<>();
		List<Files> list = codeDao.findByType("controller");
		String type = "concallser";
		for (Files file : list) {
			Table table = new Table();
			table.setSource(file.getName());
			String sourcePath_Beforesrc = file.getPath().substring(0,
					file.getPath().indexOf("\\src"));
			String sourcePackage = sourcePath_Beforesrc.substring(
					sourcePath_Beforesrc.lastIndexOf("\\") + 1,
					sourcePath_Beforesrc.length());
			table.setSourcePackage(sourcePackage);
			List<String> tarlist = new ArrayList<>();
			List<String> tarPackagelist = new ArrayList<>();
			// List<Relation> rellist = relationDao.findBySource(file.getId());
			List<Relation> rellist = relationDao.findBySourceAndType(
					file.getId(), type);
			String tarPath_Beforesrc = null;
			String tarPackage = null;
			for (Relation rel : rellist) {
				Files tar = codeDao.findOne(rel.getTarget());
				tarlist.add(tar.getName());

				tarPath_Beforesrc = null;
				tarPackage = null;
				if (StringUtils.isNotBlank(tar.getPath())) {
					tarPath_Beforesrc = tar.getPath().substring(0,
							tar.getPath().indexOf("\\src"));
				}
				if (StringUtils.isNotBlank(tarPath_Beforesrc)) {
					tarPackage = tarPath_Beforesrc.substring(
							tarPath_Beforesrc.lastIndexOf("\\") + 1,
							tarPath_Beforesrc.length());
				}
				tarPackagelist.add(tarPackage);
			}
			table.setTarget(tarlist);
			table.setTargetPackage(tarPackagelist);
			tableList.add(table);
		}
		return tableList;
	}

	@Override
	public List<TableSerCallSer> getTableSerCallSerData() throws Exception {
		List<TableSerCallSer> tableList = new ArrayList<>();
		List<Files> list = codeDao.findByType("serviceImpl");
		String type = "sercallser";
		for (Files file : list) {
			TableSerCallSer table = new TableSerCallSer();
			table.setSource(file.getName());

			String sourcePath_Beforesrc = file.getPath().substring(0,
					file.getPath().indexOf("\\src"));
			String sourcePackage = sourcePath_Beforesrc.substring(
					sourcePath_Beforesrc.lastIndexOf("\\") + 1,
					sourcePath_Beforesrc.length());
			table.setSourcePackage(sourcePackage);

			List<String> tarlist = new ArrayList<>();
			List<String> tarPackagelist = new ArrayList<>();
			List<Relation> rellist = relationDao.findBySourceAndType(
					file.getId(), type);
			String tarPath_Beforesrc = null;
			String tarPackage = null;
			for (Relation rel : rellist) {
				Files tar = codeDao.findOne(rel.getTarget());
				tarlist.add(tar.getName());

				tarPath_Beforesrc = null;
				tarPackage = null;
				if (StringUtils.isNotBlank(tar.getPath())) {
					tarPath_Beforesrc = tar.getPath().substring(0,
							tar.getPath().indexOf("\\src"));
				}
				if (StringUtils.isNotBlank(tarPath_Beforesrc)) {
					tarPackage = tarPath_Beforesrc.substring(
							tarPath_Beforesrc.lastIndexOf("\\") + 1,
							tarPath_Beforesrc.length());
				}
				tarPackagelist.add(tarPackage);
			}
			table.setTarget(tarlist);
			table.setTargetPackage(tarPackagelist);
			tableList.add(table);
		}
		return tableList;
	}

	@Override
	public List<TableWhoCallSer> getTableWhoCallSerData() throws Exception {
		// 1.从Files表中检索出Type=serviceImpl的记录
		// 2.从Relation表中根据Target=第1步的记录ID 的所有Source
		List<TableWhoCallSer> tableList = new ArrayList<>();
		List<Files> list = codeDao.findByType("serviceImpl");
		for (Files file : list) {
			TableWhoCallSer table = new TableWhoCallSer();
			table.setTarget(file.getName());

			String tarPath_Beforesrc = file.getPath().substring(0,
					file.getPath().indexOf("\\src"));
			String tarPackage = tarPath_Beforesrc.substring(
					tarPath_Beforesrc.lastIndexOf("\\") + 1,
					tarPath_Beforesrc.length());
			table.setTargetPackage(tarPackage);

			List<String> sourlist = new ArrayList<>();
			List<String> sourPackagelist = new ArrayList<>();
			List<Relation> rellist = relationDao.findByTarget(file.getId());
			String sourPath_Beforesrc = null;
			String sourPackage = null;
			for (Relation rel : rellist) {
				Files sour = codeDao.findOne(rel.getSource());
				sourlist.add(sour.getName());

				sourPath_Beforesrc = null;
				sourPackage = null;
				if (StringUtils.isNotBlank(sour.getPath())) {
					sourPath_Beforesrc = sour.getPath().substring(0,
							sour.getPath().indexOf("\\src"));
				}
				if (StringUtils.isNotBlank(tarPath_Beforesrc)) {
					sourPackage = sourPath_Beforesrc.substring(
							sourPath_Beforesrc.lastIndexOf("\\") + 1,
							sourPath_Beforesrc.length());
				}
				sourPackagelist.add(sourPackage);
			}
			table.setSource(sourlist);
			table.setSourcePackage(sourPackagelist);
			tableList.add(table);
		}
		return tableList;
	}
}
