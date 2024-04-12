<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Ответ</title>
</head>
<body>
<div>
    <c:if test="${requestScope.isFinal}">
        <c:choose>
            <c:when test="${requestScope.isWin}">
                <h1>Вы выйграли!</h1>
            </c:when>
            <c:otherwise>
                <h1>Вы проиграли!</h1>
            </c:otherwise>
        </c:choose>
    </c:if>

    <p>${requestScope.answer.finalMessage}</p>

    <div>
        <form method="post">
            <button>${requestScope.button}</button>
        </form>
    </div>
</div>
</body>
</html>
