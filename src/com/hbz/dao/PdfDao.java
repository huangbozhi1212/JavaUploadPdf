package com.hbz.dao;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;

import com.hbz.model.Pdf;
import com.hbz.util.DBUtil;

/**
 * 
 *	Pdf 数据访问类
 */
public class PdfDao {

	public static final PdfDao me = new PdfDao();
	
	/**
	 * 添加
	 */
	public int add(Pdf pdf) {
		String sql = "insert into pdf(id,file_name,file_path,create_user,create_date) values(?,?,?,?,?)";
		return DBUtil.update(sql, pdf.getId(),pdf.getFileName(),pdf.getFilePath(),pdf.getCreateUser(),pdf.getCreateDate());
	}
	
	/**
	 * 删除
	 */
	public int del(Pdf pdf) {
		String sql = "delete from pdf where id = ?";
		return DBUtil.update(sql, pdf.getId());
	}
	
	/**
	 * 修改
	 */
	public int update(Pdf pdf) {
		String sql = "update pdf set file_name = ?,file_path = ?,create_user = ?,create_date = ? where id = ?";
		return DBUtil.update(sql, pdf.getFileName(),pdf.getFilePath(),pdf.getCreateUser(),pdf.getCreateDate(),pdf.getId());
	}
	
	/**
	 * 根据ID获取一条记录
	 */
	public Pdf get(String id) {
		StringBuilder sql = new StringBuilder("select * from pdf");
		sql.append(" where id = ?");
		List<Map<String,Object>> datas = DBUtil.query(sql.toString(), id);
		if(null != datas && datas.size() > 0) {
			Map<String,Object> data = datas.get(0);
			return new Pdf((String)data.get("id"),(String)data.get("file_name"),(String)data.get("file_path"),(String)data.get("create_user"),(Date)data.get("create_date"));
		}
		return null;
	}
	
	/**
	 * 列出所有记录
	 */
	public List<Pdf> list() {
		String sql = "select * from pdf";
		List<Map<String,Object>> datas = DBUtil.query(sql);
		if(null != datas && datas.size() > 0) {
			List<Pdf> list = new ArrayList<Pdf>();
			for(Map<String, Object> data : datas) {
				list.add(new Pdf((String)data.get("id"),(String)data.get("file_name"),(String)data.get("file_path"),(String)data.get("create_user"),(Date)data.get("create_date")));
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 分页搜索key相关记录
	 */
	public JSONObject page(int page,int pageSize,String key) {
		StringBuilder sql = new StringBuilder("select * from pdf");
		StringBuilder sql2 = new StringBuilder("select count(*) count from pdf");
		if(null != key && !"".equals(key)) {
			sql.append(" where pdf.file_name like '%").append(key).append("%'");
			sql2.append(" where pdf.file_name like '%").append(key).append("%'");
		}
		sql.append(" order by create_date desc");
		int offset = pageSize * (page - 1);
		sql.append(" limit ").append(offset).append(",").append(pageSize);
		List<Map<String, Object>> datas = DBUtil.query(sql.toString());
		int count = Integer.parseInt(DBUtil.query(sql2.toString()).get(0).get("count").toString());
		int pages = (count + pageSize - 1) / pageSize;
		JSONObject res = new JSONObject();
		if(null != datas && datas.size() > 0) {
			List<Pdf> list = new ArrayList<Pdf>();
			for(Map<String, Object> data : datas) {
				list.add(new Pdf((String)data.get("id"),(String)data.get("file_name"),(String)data.get("file_path"),(String)data.get("create_user"),(Date)data.get("create_date")));
			}
			res.put("list",list);
		}
		res.put("count",count);
		res.put("pages",pages);
		res.put("page",page);
		res.put("pageSize",pageSize);
		return res;
	}
	
}