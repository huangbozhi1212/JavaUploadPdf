package com.hbz.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 数据库操作简单封装
 *
 */
public class DBUtil {

	private static Properties prop = new Properties();
	
	//加载配置文件
	static {
		InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取链接
	 * 
	 * @return
	 */
	public static Connection getconn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = prop.getProperty("jdbcUrl");
		String user = prop.getProperty("user");
		String psw = prop.getProperty("password");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, psw);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭链接
	 * 
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据库更新操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return 更新数目
	 * @throws SQLException
	 */
	public static int update(String sql, Object... params) {
		Connection conn = DBUtil.getconn();
		PreparedStatement ps = null;
		int row = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			row = ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			DBUtil.closeConn(conn);
		}
		return row;
	}

	/**
	 * 数据库查询操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> query(String sql, Object... params) {
		Connection conn = DBUtil.getconn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			stmt = conn.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			DBUtil.closeConn(conn);
		}
		return list;

	}
	
	
	/**
	 * 查询第一条数据
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Map<String, Object> queryFirst(String sql, Object... params) {
		List<Map<String, Object>> list = query(sql, params);
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}

