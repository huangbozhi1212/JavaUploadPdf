package com.hbz.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;
import com.hbz.common.CMD;
import com.hbz.model.Pdf;
import com.hbz.service.PdfService;
import com.hbz.util.DBUtil;
import com.hbz.util.DateUtil;

/**
 * Pdf 控制器
 */
@WebServlet("/pdf")
public class PdfServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//分页默认每页20条
	private static final int pageSize = 8;
	//文件上传路径
	private static final String uploadPath = "uploads/";
	//文件大小限制
	private static final int maxSize = 1024 * 1024 * 30;
	//文件名正则验证
	private static final String regex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])湖南工业大学OSPS.pdf$";
	
       
	/**
	 * 根据action,处理不同请求
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if("goIndex".equals(action)) {
			goIndex(request, response);
		} else if("save".equals(action)) {
			save(request, response);
		} else if("goEdit".equals(action)) {
			goEdit(request, response);
		} else if("del".equals(action)) {
			del(request, response);
		} else if("upload".equals(action)) {
			upload(request, response);
		} else if("batchDel".equals(action)) {
			batchDel(request, response);
		} else if("down".equals(action)) {
			down(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * 分页显示
	 */
	private void goIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (Exception e) {
		}
		request.setAttribute("key", key == null ? "" : key);
		request.setAttribute("page", PdfService.me.page(page,pageSize,key));
		request.getRequestDispatcher("/pages/pdf/pdf_list.jsp").forward(request, response);
	}

	/**
	 * 跳转到编辑页面
	 */
	private void goEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		request.setAttribute("data", PdfService.me.get(id));
		request.getRequestDispatcher("/pages/pdf/pdf.jsp").forward(request, response);
	}

	/**
	 * 新增/修改
	 */
	private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String fileName = request.getParameter("fileName");
		String filePath = uploadPath + fileName;
		String createUser = request.getParameter("createUser");
		String createDate = DateUtil.format(new Date());
		Pdf pdfBean = new Pdf(id,fileName,filePath,createUser,DateUtil.parse(createDate));
		if(null == id || "".equals(id)) {
			//新增
			pdfBean.setId(UUID.randomUUID().toString().replace("-",""));
			PdfService.me.add(pdfBean);
		} else {
			//修改
			PdfService.me.update(pdfBean);
		}
		response.getWriter().write(new CMD(200).toString());
	}
	
	/**
	 * 删除
	 */
	private void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		PdfService.me.del(PdfService.me.get(id));
		response.getWriter().write(new CMD(200).toString());
	}

	/**
	 * 批量删除
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void batchDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] ids = request.getParameterValues("ids");
		StringBuilder inStr = new StringBuilder("(");
		for (String id : ids) {
			inStr.append("'").append(id).append("',");
		}
		inStr.deleteCharAt(inStr.length()-1);
		inStr.append(")");
		DBUtil.update("delete from pdf where id in " + inStr);
		response.getWriter().write(new CMD(200).toString());
	}
	
	/**
	 * 下载
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void down(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Pdf pdf = PdfService.me.get(id);
		String path = this.getServletContext().getRealPath(uploadPath) + pdf.getFileName();
		FileInputStream is = new FileInputStream(new File(path));
		String filename=new String(pdf.getFileName().getBytes("utf-8"),"ISO-8859-1");
		response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition","attachment; filename=" + filename);  
        OutputStream outputStream = response.getOutputStream();
		byte[] buffer = new byte[1024];
		int i = -1;
		while ((i = is.read(buffer)) != -1) {
			outputStream.write(buffer, 0, i);
		}
		outputStream.flush();
		outputStream.close();
        is.close();
	}
	
	/**
	 * 文件上传
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//上传文件保存路径
		String path = this.getServletContext().getRealPath(uploadPath);
		File fpath = new File(path);
		if(!fpath.exists()){
			fpath.mkdirs();
		}
		
		//初始化上传组件
		SmartUpload su = new SmartUpload();
		su.initialize(this.getServletConfig(), request, response);
		
		try {
			//设置文件上传最大限制
	        su.setMaxFileSize(maxSize); 
	        //文件上传
	        su.upload();
	        com.jspsmart.upload.File file = su.getFiles().getFile(0);
	        //验证文件名
	        String filename = new String(file.getFileName().getBytes("GBK"),"utf-8");
	        //System.out.println(filename);
	        if(!filename.matches(regex)) {
	        	response.getWriter().write(new CMD(500,"文件名格式错误").toString());
	        	return;
	        }
	        file.saveAs(path+"/"+filename);
	        response.getWriter().write(new CMD(200,"",uploadPath+filename).toString());
		} catch (Exception e) {
			if(e.getMessage().startsWith("Size exceeded for this file")) {
				//文件大小超过限制
				response.getWriter().write(new CMD(500,"文件大小超过限制(30M)").toString());
			} else {
				response.getWriter().write(new CMD(500,"文件上传失败").toString());
			}
		}
	}
	
}