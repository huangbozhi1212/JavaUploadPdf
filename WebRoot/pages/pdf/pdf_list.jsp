<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.hbz.model.Pdf"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/pages/common/header.jsp"></jsp:include>
	<%
		JSONObject pageData = (JSONObject)request.getAttribute("page");
		int pageNo = pageData.getIntValue("page");
		int pages = pageData.getIntValue("pages");
		List<Pdf> list = (List<Pdf>) pageData.get("list");
		String key = (String) request.getAttribute("key");
	%>
</head>
<body class="childrenBody">
	<blockquote class="layui-elem-quote news_search">
		<div class="layui-inline">
		    <div class="layui-input-inline">
		    	<input type="text" placeholder="请输入文件名搜索" class="layui-input search_input" id="key" value="<%=key%>">
		    </div>
		    <a class="layui-btn search_btn">搜索</a>
		</div>
		<div class="layui-inline">
			<a class="layui-btn layui-btn-normal usersAdd_btn">上传PDF</a>
			<a class="layui-btn layui-btn-danger" id="batchdel">批量删除</a>
		</div>
	</blockquote>
	<div class="layui-form users_list">
	  	<table class="layui-table">
		    <colgroup>
				<col width="30">
				<col>
				<col>
				<col>
				<col width="25%">
		    </colgroup>
		    <thead>
				<tr>
					<th><input type="checkbox" name="ckAll"lay-skin="primary" lay-filter="ck"></th>
					<th>文件名</th>
					<th>上传者</th>
					<th>上传时间</th>
					<th>操作</th>
				</tr> 
		    </thead>
		    <tbody class="content">
		    	<%
		    		if(null != list) { 
						for(Pdf data : list) {		    			
		    	%>
		    	<tr>
		    		<td><input type="checkbox" name="ck" lay-skin="primary" value="<%=data.getId()%>"></td>
					<td><a style="text-decoration:underline;" href="javascript:;" class="do-action"  data-type="view" data-path="<%=data.getFilePath() %>"><%=data.getFileName() %></a></td>
					<td><%=data.getCreateUser() %></td>
					<td><%=data.getCreateDate() %></td>
					<td>
						<a class="layui-btn layui-btn-mini do-action" data-type="view" data-path="<%=data.getFilePath() %>"><i class="iconfont">&#xe705;</i> 预览</a>
						<a class="layui-btn layui-btn-mini do-action" data-type="edit" data-id="<%=data.getId() %>"><i class="iconfont icon-edit"></i> 修改</a>
						<a class="layui-btn layui-btn-mini do-action" data-type="down" data-id="<%=data.getId() %>"><i class="layui-icon">&#xe601;</i> 下载</a>
						<a class="layui-btn layui-btn-danger layui-btn-mini do-action" data-type="del" data-id="<%=data.getId() %>"><i class="layui-icon">&#xe640;</i> 删除</a>
					</td>
				</tr> 
				<%}} %>
		    </tbody>
		</table>
	</div>
	<div id="page" style="float:right;"></div>
</body>

<script type="text/javascript">
	layui.use(['form','layer','laypage','stools','laytpl'],function(){
		var form = layui.form(),
		laypage = layui.laypage,
		layer = layui.layer,
		laytpl = layui.laytpl,
		stools = layui.stools;
		
		//新建
		$(".usersAdd_btn").click(function(){
			index = layer.open({
				title : "上传PDF",
				type : 2,
				area: ['620px', '600px'],
				content : _global_ctx + "pdf?action=goEdit"
			})
			//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
			$(window).resize(function(){
				layui.layer.full(index);
			})
			layui.layer.full(index);
		})
		
		//操作
		$('.content').on('click','.do-action',function(){
			var thisObj = $(this);
			var id = thisObj.data('id');
			var type = thisObj.data('type');
			if("edit" == type) {
				//编辑
				index = layer.open({
					title : "修改PDF",
					type : 2,
					area: ['620px', '600px'],
					content : _global_ctx + "pdf?action=goEdit&id=" + id
				})
				//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
				$(window).resize(function(){
					layui.layer.full(index);
				})
				layui.layer.full(index);
			} else if("del" == type) {
				stools.confirm('确定删除该PDF文件?',{title: "请确认"},function() {
					stools.request({
			      		url: _global_ctx + "pdf?action=del&id=" + id,
			      		scb:function(d){
			      			if(d.code=="200"){
			      				stools.toastS("删除成功",function() {
			      					location.reload();
			      				});
			      			}else{
			      				stools.toastE(d.msg);
			      			}
			      		},
			      		fcb:function(){
			      			stools.toastE("出错");
			      		}
					});
				});
			} else if("down" == type) {
				location.href = _global_ctx + "pdf?action=down&id=" + id
			} else if("view" == type) {
				var path = $(this).data('path');
				window.open(_global_ctx + "cdn/pdfjs/web/viewer.html?file=" + _global_ctx + path);
			}
		})
		
		//搜索
		$('.search_btn').on('click',function() {
			var key = $('#key').val();
			location.href = _global_ctx + "pdf?action=goIndex&key=" + key;
		});
		
		//渲染分页
		laypage({
	        cont: $("#page"),
	        pages: <%=pages%>,
	        curr: <%=pageNo%>,
	        groups: 5,
	        first:1,
	        last:<%=pages%>,
	        jump: function(obj, first){ 
	        	if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
	        		var key = $('#key').val();
	        		location.href = _global_ctx + "pdf?action=goIndex&key=" + key + "&page=" + obj.curr;
  		        }
	        }
		});
		
		//全选
		form.on('checkbox(ck)', function(data){
	        $('input[name=ck]').each(function (index, item) {
	            item.checked = data.elem.checked;
	        });
	        form.render('checkbox');
		});      
		
		//批量删除
		$('#batchdel').on('click',function() {
			var ids = new Array();
			$('input[name=ck]:checked').each(function () {
		    	ids.push($(this).val());
		    });
			if(ids.length < 1) {
				stools.toastE('请选择文件');
				return;
			}
			layer.confirm('确认要删除选中文件?',{
		        title: '提示',
		        closeBtn: true,
		        shade: [0.5, '#000', true],
		        skin:'skin-1',
		        area: ['400px'],
		        btn:['确定','取消']
		    },function(index){
		    	layer.close(index);
		    	stools.request({
					url: _global_ctx + "pdf?action=batchDel",
					data:{
						ids:ids,
					},
					scb:function(data, textStatus) {
						if(data.code == 200) {
							stools.toastI('删除成功',function(){
								window.location.reload();
							});
			    		} else {
			    			stools.toastE(data.msg);
			    		}
					}
				});
	        });
		});
	});
</script>
</html>