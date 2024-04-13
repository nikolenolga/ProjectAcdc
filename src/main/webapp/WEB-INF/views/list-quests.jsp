<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Квесты</title>
</head>
<body>
<h1>Доступные квесты:</h1>
<c:forEach var="quest" items="${requestScope.quests}">
    <p><a href="game-start?questId=${quest.id}">${quest.name}</a></p>
</c:forEach>
</body>
</html>
