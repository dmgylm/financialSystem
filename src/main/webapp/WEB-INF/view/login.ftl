<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="UTF-8">
    <title>登录</title>
    <script type="text/javascript" src="./resources/js/jquery.min.js"></script>
    <script type="text/javascript">
    	function login(){
            $("#loginForm").action='login';
            $("#loginForm").submit();   
        }
    </script>
</head>
<body>
    <@shiro.user>  
                您已经登陆系统，请选择<a href="./">进入系统</a>，或者<a href="./logout">登出系统来用其它用户登陆</a>
    </@shiro.user> 
   <h1>登录${(msg)!}</h1>
   <form id="loginForm" action="./login" method="post" >
              账号：   <input name="username" id="username">
            密码：      <input type="password" name="password" id="password">
            <input type="button" value="登录" onclick="login()"  style="width:70px;height:30px"/>
  </form>
</body>
</html>