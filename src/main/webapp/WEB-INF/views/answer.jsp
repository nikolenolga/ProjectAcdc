<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Ответ</title>
</head>
<body>
<div>
    <p>${requestScope.answer.finalMessage}</p>

    <div>
        <form method="post">
            <button>Дальше</button>
        </form>
    </div>
</div>
</body>
</html>
