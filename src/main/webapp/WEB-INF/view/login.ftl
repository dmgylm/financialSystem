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
   <h1>登录</h1>
   <form id="loginForm" action="./login" method="post" >
              账号：   <input name="name" id="name">
            密码：      <input type="pwd" name="passWord" id="pwd">
            <input type="button" value="登录" onclick="login()"  style="width:70px;height:30px"/>
  </form>
</body>
</html>