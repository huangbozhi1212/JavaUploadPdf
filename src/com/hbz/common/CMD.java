package com.hbz.common;

import com.alibaba.fastjson.JSONObject;

/**
 *	异步请求返回json封装
 *  code 状态
 *  msg 消息
 *  data 数据
 *  @author 刘胜
 *
 */
public class CMD {

	private int code;
	private String msg;
	private Object data;

	public CMD() {
	}

	public CMD(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public CMD(int code) {
		this.code = code;
	}
	
	public CMD(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	public CMD(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static CMD create() {
		return new CMD();
	}

	public int getCode() {
		return this.code;
	}

	public CMD setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return this.msg;
	}

	public CMD setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return this.data;
	}

	public CMD setData(Object data) {
		this.data = data;
		return this;
	}

	public CMD setCM(int code, String msg) {
		this.code = code;
		this.msg = msg;

		return this;
	}

	public CMD setCMD(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}