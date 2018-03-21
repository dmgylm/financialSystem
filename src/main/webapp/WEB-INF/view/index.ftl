<html>
<head>
    <meta charset="UTF-8">
    <title>财务系统</title>
    <script type="text/javascript" src="./resources/js/jquery.min.js"></script>
    <script type="text/javascript">
		
	</script>
	
</head>
<body>
	<div>登陆成功</div>
	<div>欢迎[<@shiro.principal/>]</div>
	<div>
	   <#list roleResource as roleResources>
	       <p>name:${roleResources.name}</p>
	   </#list>
    </div>
</body>
</html>