<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.hbz.model.Pdf"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/pages/common/header.jsp"></jsp:include>
	<style type="text/css">
		.layui-form-item .layui-inline{ width:33.333%; float:left; margin-right:0; }
		@media(max-width:1240px){
			.layui-form-item .layui-inline{ width:100%; float:none; }
		}
	</style>
	<%
		Pdf data = (Pdf) request.getAttribute("data");
	%>
</head>
<body class="childrenBody">
	<form class="layui-form" style="width:90%;">
		<input type="hidden" name="id" value="<%=null == data ? "" : data.getId() %>">
			<div class="layui-form-item">
				<label class="layui-form-label">文件</label>
				<div class="layui-input-block">
					<input disabled type="text" name="fileName" id="fileNameVal" value="<%=null == data || null == data.getFileName() ? "" : data.getFileName() %>" class="layui-input" lay-verify="file" placeholder="请上传PDF文件">
				</div>
				<div class="layui-input-block" style="margin-top:10px;">
					<a class="layui-btn layui-btn-radius layui-btn-primary" id="fileNameUp"><i class="layui-icon">&#xe608;</i> 文件上传</a>
				</div>
				<div style="display:none;">
					<input type="file" name="file" id="fileNameFile"/>
				</div>
			</div>	
			<div class="layui-form-item">
				<label class="layui-form-label">上传者</label>
				<div class="layui-input-block">
					<input type="text" name="createUser" value="<%=null == data ? "" : data.getCreateUser() %>" class="layui-input" lay-verify="required" placeholder="请输入上传者">
				</div>
			</div>	
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="save">确定</button>
				<button class="layui-btn layui-btn-primary" id="cancel">取消</button>
		    </div>
		</div>
	</form>
</body>
<script type="text/javascript">
	layui.use(['form','layer','laydate','stools','upload'],function(){
		var form = layui.form(),
		laydate = layui.laydate,
		layer = layui.layer,
		stools = layui.stools,
		upload = layui.upload;
		
		//表单验证
		form.verify({
		  file: function(value, item){
		    if(value == ""){
		      return '请上传PDF文件';
		    }
		  }
		});
		
		//保存
		form.on("submit(save)",function(data){
			stools.request({
	      		url: _global_ctx + "pdf?action=save",
	      		data: data.field,
	      		scb:function(d){
	      			if(d.code==200){
	      				stools.toastS("保存成功",function() {
	      					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	      					parent.layer.close(index); //再执行关闭   
	      					parent.location.reload();
	      				});
	      			}else{
	      				stools.toastE(d.msg);
	      			}
	      		},
	      		fcb:function(){
	      			stools.toastE("出错");
	      		}
			});
			return false;
		});
		
		$('#fileNameUp').on('click',function() {
			$('#fileNameFile').click();
		});
		
		//文件上传
		upload({
	    	elem:$('#fileNameFile'),
	    	type: 'file',
	    	ext: 'pdf',
			url:  _global_ctx + "pdf?action=upload",
			success: function(res){
				if(res.code == 200) {
					var filename = res.data;
					filename = filename.substring(filename.lastIndexOf("/") + 1);
					$("#fileNameVal").val(filename);
					stools.toastS('上传成功');
				} else {
					stools.toastE(res.msg);
				}
			}
		});
		
		//取消.关闭窗口
		$('#cancel').on('click',function() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			return false;
		});
		
	});
</script>
</html>