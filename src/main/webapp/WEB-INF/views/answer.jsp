<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="parts/head-part.jsp" %>

<div class="base">
    <div class="base-border">
        <div class="base-left">
            <c:if test="${requestScope.answer.isFinal()}">
                <c:choose>
                    <c:when test="${requestScope.answer.isWin()}">
                        <h1>Вы выйграли!</h1>
                    </c:when>
                    <c:otherwise>
                        <h1>Вы проиграли!</h1>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class="base-item-left">
                <p class="block-p">${requestScope.answer.finalMessage}</p>
            </div>
            <div class="base-item-left">
                <form method="post">
                    <c:choose>
                        <c:when test="${requestScope.answer.isFinal()}">
                            <button class="base-button" name="button-again">Начать заново</button>
                            <button class="base-button" name="button-quests">К списку квестов</button>
                        </c:when>
                        <c:otherwise>
                            <button class="base-button" name="next">Дальше</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
    </div>
    <c:if test="${requestScope.answer.img || requestScope.answer.isFinal()}">
        <div class="base-right">
            <div class="base-item-right">
                <img class="base-image" src="${pageContext.request.contextPath}/img/${requestScope.answer.getImage()}.png" alt="user-image">
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
