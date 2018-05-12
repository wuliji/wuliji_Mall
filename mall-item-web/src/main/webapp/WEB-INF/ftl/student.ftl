<html>
<head><title></title></head>
<body>
	学生信息：<br>
	学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
	姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
	年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
	家庭住址:${student.address}&nbsp;&nbsp;&nbsp;&nbsp;
	<br/>
	<!-- 可以指定日期 ?date,?time,?datetime,?string(parten)-->
	当前时间：${date?string("yyyy/MM/dd HH:mm:ss")}<br>
	null值的处理：${val!"isEmpty"}<br>
	include用法：引入模板<#include "hello.ftl">
</body>
</html>