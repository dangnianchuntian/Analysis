<!DOCTYPE html>

<html>
<head>
<style type="text/css">
.table-d table{ background:#000}
.table-d table td{ background:#FFF}
</style>
</head>
<body>

<div class="table-d">
	Controller调用情况
	<table border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td>Controller名称</td>
			<td>Service名称</td>
		<tr>
		<#list list as data >
		<tr>
			<td>${data.source}</td>
			<td><#list data.target as tar >
			${tar}</br>
				</#list>
			</td>
		</tr>
		</#list>
	</table>
</div>

</body>
</html>