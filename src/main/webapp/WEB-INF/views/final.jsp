<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Финал</title>
</head>
<body>
<div>

        <c:choose>
            <c:when test="${requestScope.answer.isWin()}">
                <h1>Вы выйграли!</h1>
            </c:when>
            <c:otherwise>
                <h1>Вы проиграли!</h1>
            </c:otherwise>
        </c:choose>

    <p>${requestScope.answer.finalMessage}</p>

    <div>
        <form method="post">
            <button name="button-again">Начать заново</button>
            <button name="button-quests">К списку квестов</button>
        </form>
    </div>
</div>
</body>
</html>
