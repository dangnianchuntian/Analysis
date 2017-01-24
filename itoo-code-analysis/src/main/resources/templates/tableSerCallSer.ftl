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
	Service调用Service情况
	<table border="0" cellspacing="1" cellpadding="0">
		<tr>
		    <td>主调Service所在模块</td>
			<td>主调Service名称</td>
			<td>被调Service名称</td>
			<td>被调Service所在模块</td>
		<tr>
		<#list list as data >
		<tr>
            <td>${data.sourcePackage}</td>
			<td>${data.source}</td>
			<td><#list data.target as tar >
			${tar}</br>
				</#list>
			</td>
			<td><#list data.targetPackage as tarPackage >
			${tarPackage}</br>
				</#list>
			</td>
		</tr>
		</#list>
	</table>
</div>

</body>
</html>