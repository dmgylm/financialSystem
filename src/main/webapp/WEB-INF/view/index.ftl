<html>
<head>
    <meta charset="UTF-8">
    <title>财务系统</title>
    <script type="text/javascript" src="./resources/js/jquery.min.js"></script>
    <script type="text/javascript">
		function roleClick(){
		  $('#tabs').toggle();
		}
	</script>
	
</head>
<body>
	<div>登陆成功</div>
	<div>欢迎[<@shiro.principal/>]</div>
	<div>
	   <#list roleResource as roleResources>
	       <#if (roleResources.parentId == "1") >
	           <#if (roleResources.name == "系统设置") >
	               <p onclick="roleClick()">name:${roleResources.name}</p>
	           <#else>
	               <p>name:${roleResources.name}</p>
               </#if>
	       </#if>
	       
	   </#list>
    </div>
    <div id="tabs" style="display:none">
       <#list roleResource as roles>
           <#if (roles.parentId == "1/33") >
               <p>name:${roles.name}</p>
           </#if>
       </#list>
    </div>
    
</body>
</html>