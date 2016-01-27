<%--
  Created by IntelliJ IDEA.
  User: jacke.wang
  Date: 15-11-24
  Time: 下午6:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传文件</title>
</head>
<body>
<form action="/excel/upload" method="post" enctype="multipart/form-data">
    <table>
        <tr>
            <td>host</td>
            <td><input type="text" name="host"></td>
            <td>参考: 127.0.0.1 或 l-qboss1.qss.dev.cn6.qunar.com</td>
        </tr>
        <tr>
            <td>port</td>
            <td><input type="text" name="port"></td>
            <td>参考: 3306</td>
        </tr>
        <tr>
            <td>db</td>
            <td><input type="text" name="db"></td>
            <td>参考: qss</td>
        </tr>
        <tr>
            <td>table</td>
            <td><input type="text" name="table"></td>
            <td>参考: user</td>
        </tr>
        <tr>
            <td>user</td>
            <td><input type="text" name="user"></td>
            <td>参考: dev</td>
        </tr>
        <tr>
            <td>password</td>
            <td><input type="text" name="password"></td>
            <td>参考: qunar.com</td>
        </tr>
        <tr>
            <td>file</td>
            <td><input type="file" name="file"></td>
        </tr>
        <tr><td><input type="submit" value="submit"></td></tr>
    </table>
</form>
<br>
<br>
<br>
<div>
    <h1>说明：</h1>
    <table border="1">
        <tr><td>数据类型</td><td>MySql类型参考</td><td>Excel单元格格式</td><td>特殊说明</td></tr>
        <tr><td>整数类</td><td>tinyint/smallint/mediumint/int/bigint</td><td>无特殊要求</td><td>Java boolean在MySql数据库中以整型处理</td></tr>
        <tr><td>浮点数类</td><td>float/double/decimal</td><td>无特殊要求</td><td></td></tr>
        <tr><td>字符类</td><td>char/varchar/tinyblob/tinytext/blob/text/mediumblob/mediumtext/longblob/longtext</td><td>要求为文本类型</td><td></td></tr>
        <tr><td>时间类</td><td>date/time/year/datetime/timestamp</td><td>要求为文本类型</td><td>所有时间类型必须为格式：yyyy-MM-DD HH:mm:ss；如果MySql类型为date，则HH:mm:ss也必须以00:00:00填充</td></tr>
    </table>
</div>
</body>
</html>
