<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.authorized}">
        <%@ include file="parts/header-authorized.jsp" %>
    </c:when>
    <c:otherwise>
        <%@ include file="parts/header.jsp" %>
    </c:otherwise>
</c:choose>

<div class="base">
    <div class="base-border">
        <form method="post">
            <fieldset>
                <div class="base-left">
                    <div class="base-item-left">
                        <h2 class="block-h2">${requestScope.question.questionMessage}</h2>
                        <div id="radios">
                            <c:forEach var="answer" items="${requestScope.answers}" varStatus="status">
                                <p class="block-p">
                                    <input type="radio" name="answerId"
                                           value="${answer.id}" ${status.first ? 'checked' : ''}>
                                    <label>${answer.answerMessage}</label>
                                </p>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="base-item-left">
                        <form method="post">
                            <button class="base-button" name="submit">Подтвердить</button>
                        </form>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="base-right">
        <div class="base-item-right">
            <img class="base-image" src="images/${requestScope.question.getImage()}" alt="user-image">
        </div>
    </div>
</div>
</body>
</html>
