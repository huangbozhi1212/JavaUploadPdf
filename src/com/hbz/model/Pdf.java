package com.hbz.model;

import java.util.Date;

/**
 * 
 *	Pdf 实体类
 */
public class Pdf {
	
	private String id;
	private String fileName;
	private String filePath;
	private String createUser;
	private Date createDate;

	/***
	 * 构造方法
	 */
	public Pdf() {}

	public Pdf(String id, String fileName, String filePath, String createUser, Date createDate) {
		this.id = id;
		this.fileName = fileName;
		this.filePath = filePath;
		this.createUser = createUser;
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}