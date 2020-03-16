<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="icon" href="favicon.ico">
<link rel="stylesheet" href="<%=basePath %>cdn/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="<%=basePath %>cdn/css/font.css" media="all" />
<link rel="stylesheet" href="<%=basePath %>cdn/css/main.css" media="all" />
<script type="text/javascript">
	var _global_ctx = '<%=basePath%>';
	var _global_cdn = '<%=basePath%>cdn/';
</script>
<script type="text/javascript" src="<%=basePath %>cdn/layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath %>cdn/js/index.js"></script>
