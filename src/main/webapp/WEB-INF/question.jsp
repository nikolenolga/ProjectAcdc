<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${sessionScope.quest.name}</title>
</head>
<body>
<form method="post">
    <fieldset>
        <!-- Question  -->
        <legend>${requestScope.question.questionMessage}</legend>

        <!-- Multiple Radios  -->
        <div id="radios">
                <c:forEach var="answer" items="${requestScope.answers}">
                    <p>
                        <input type="radio" name="answerId" value="${answer.id}" checked="checked">
                        ${answer.answerMessage}
                    </p>
                </c:forEach>
        </div>

        <!-- Button -->
        <div>
                <button>Подтвердить</button>
        </div>
    </fieldset>
</form>
</body>
</html>
