package com.hbz.service;

import java.util.List;
import com.alibaba.fastjson.JSONObject;

import com.hbz.model.Pdf;
import com.hbz.dao.PdfDao;

/**
 * 
 *	Pdf 服务类
 */
public class PdfService {

	public static final PdfService me = new PdfService();
	
	/**
	 * 添加
	 */
	public int add(Pdf pdf) {
		return PdfDao.me.add(pdf);
	}
	
	/**
	 * 删除
	 */
	public int del(Pdf pdf) {
		return PdfDao.me.del(pdf);
	}
	
	/**
	 * 更新
	 */
	public int update(Pdf pdf) {
		return PdfDao.me.update(pdf);
	}
	
	/**
	 * 根据ID获取一条记录
	 */
	public Pdf get(String id) {
		return PdfDao.me.get(id);
	}
	
	/**
	 * 列出所有记录
	 */
	public List<Pdf> list() {
		return PdfDao.me.list();
	}
	
	/**
	 * 分页搜索key相关记录
	 */
	public JSONObject page(int page,int pageSize,String key) {
		return PdfDao.me.page(page,pageSize,key);
	}
	
}