<html>
<body>
Hello, I am a template file. My name is ${name!""}.
Redirected to this page: ${redirected!"false"}
<#list map as k, v>
    ${k}, ${v}
</#list>
</body>
</html>