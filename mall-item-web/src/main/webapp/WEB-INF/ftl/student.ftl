<html>
<head><title></title></head>
<body>
	ѧ����Ϣ��<br>
	ѧ��:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
	����:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
	����:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
	��ͥסַ:${student.address}&nbsp;&nbsp;&nbsp;&nbsp;
	<br/>
	<!-- ����ָ������ ?date,?time,?datetime,?string(parten)-->
	��ǰʱ�䣺${date?string("yyyy/MM/dd HH:mm:ss")}<br>
	nullֵ�Ĵ���${val!"isEmpty"}<br>
	include�÷�������ģ��<#include "hello.ftl">
</body>
</html>