<!DOCTYPE html>

<html>
<head>
<style type="text/css">
  #container {
    max-width: 1400px;
    height: 3000px;
    margin: auto;
  }
</style>
</head>
<body>
<div id="container"></div>
<div>
	Controller调用情况
	<table>
		<th>
			<td>Controller名称</td>
			<td>Service名称</td>
		<th>
	</table>
</div>

<script src="/js/sigma.min.js"></script>
<script src="/js/sigma.parsers.json.min.js"></script>
<script>
  sigma.parsers.json('/PTPSerCallSerData', {
    container: 'container',
    settings: {
      defaultNodeColor: '#ec5148'
    }
  });
</script>
</body>
</html>