<%--
  Created by IntelliJ IDEA.
  User: jackewang
  Date: 14-8-21
  Time: 上午10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <script type="text/javascript" src="/resources/jquery-1.11.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            // 页面加载时,发送请求
            turnPage(1);
        });

        // 此函数用于切换登入窗口
        function changeBlock(i) {
            if (i == 0) {
                $("#loginDiv").css("display", "none");
            } else {
                $("#loginDiv").css("display", "block");
            }
        }
        // 此函数用于用户登入
        function login() {
            var username = $("#username").val();
            var pwd = $("#pwd").val();
            $.ajax({
                type: "post",
                url: "/login.do",
                data: {"userName": username, "password": pwd},
                dataType: "json",
                success: function (result) {
                    if (result.data == "success") {
                        alert("登入成功");
                        changeBlock(0);
                        $("#listDiv").empty();
                        turnPage(1);
                    } else {
                        alert("登入失败");
                        window.location.href = "${pageContext.request.contextPath}/index.jsp";
                    }
                },
                error: function () {
                    alert("登入失败");
                    window.location.href = "error.jsp";
                }
            })
        }
        // 此函数用于显示diffFile列表中的一页，即实现了分页的功能
        function turnPage(i) {
            $.ajax({
                type: "get",
                url: "/turnPage.do",
                data: {"pageNow": i},
                dataType: "json",
                success: function (result) {
                    var list = result.data.list;
                    $("#more").remove();
                    var bindStr = "<table id='resultList' border='1'>";
                    if (result.data.isUser == "user") {
                        for (var i = 0; i < list.length; i++) {
                            bindStr += "<tr><td width='50px'>" + list[i].date + "</td>";
                            bindStr += "<td width='200px'>" + list[i].source + "</td>";
                            bindStr += "<td width='200px'>" + list[i].target + "</td>";
                            bindStr += "<td width='200px'>" + list[i].difference + "</td>";
                            bindStr += "<td width='50px'><input type='button' onclick='deleteResult(" + list[i].id + ")' value='Delete'/></td></tr>"
                        }
                    } else {
                        for (var i = 0; i < list.length; i++) {
                            bindStr += "<tr><td width='50px'>" + list[i].date + "</td>";
                            bindStr += "<td width='200px'>" + list[i].source + "</td>";
                            bindStr += "<td width='200px'>" + list[i].target + "</td>";
                            bindStr += "<td width='200px'>" + list[i].difference + "</td></tr>";
                        }
                    }
                    bindStr += "</table>";
                    bindStr += "<button onclick='turnPage(" + result.data.nextPage + ")' id='more'>查看更多</button>";
                    $("#listDiv").append(bindStr);
                },
                error: function () {
                    alert("翻页失败");
                    window.location.href = "error.jsp";
                }
            })
        }

        // 此函数用于删除一条记录
        function deleteResult(id) {
            $.ajax({
                type: "post",
                url: "/delete.do",
                data: {"id": id},
                dataType: "json",
                success: function (result) {
                    if (result.data != "success") {
                        alert("游客没有删除的权限");
                    }
                    $("#listDiv").empty();
                    turnPage(1);
                },
                error: function () {
                    alert("翻页失败");
                    window.location.href = "error.jsp";
                }
            })
        }
    </script>
</head>
<body>
<div id="mainDiv">

    <button onclick="changeBlock(1)">登入窗口</button>

    <div id="loginDiv" style="display: none">
        <table id="table1" style="display:block">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="username"/></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="password" id="pwd"/></td>
            </tr>
            <tr>
                <td align="center"><input type="button" onclick="changeBlock(0)" value="返回"/></td>
                <td align="center"><input type="button" id="button" value="登入" onclick="login()"/></td>
            </tr>
        </table>
    </div>

    <form action="/diffFiles.do" method="post" enctype="multipart/form-data">
        <input type="file" name="source">
        <input type="file" name="target">
        <input type="submit">
    </form>

    <div id="listDiv">

    </div>

</div>
</body>
</html>
