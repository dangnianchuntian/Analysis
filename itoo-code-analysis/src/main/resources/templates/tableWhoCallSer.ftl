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
	Service被哪些类调用情况
	<table border="0" cellspacing="1" cellpadding="0">
		<tr>
		    <td>被调Service所在目录</td>
            <td>被调Service名称</td>
			<td>主调类名称</td>
			<td>主调类所在目录</td>
		<tr>
		<#list list as data >
		<tr>
            <td>${data.targetPackage}</td>
			<td>${data.target}</td>
			<td><#list data.source as sour >
			${sour}</br>
				</#list>
			</td>
            <td><#list data.sourcePackage as sourPackage >
			${sourPackage}</br>
				</#list>
			</td>
		</tr>
		</#list>
	</table>
</div>

</body>
</html>