<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="parts/head-part.jsp"%>

<div class="block">
    <h2>Доступные квесты:</h2>
</div>
<div class="block">
    <c:forEach var="quest" items="${requestScope.quests}">
        <div class="block-item">
            <c:choose>
                <c:when test="${!quest.img && sessionScope.user.id == quest.userAuthorId}">
                    <img class="img-in-img-block" src="${pageContext.request.contextPath}/img/quest-created.png">
                </c:when>
                <c:otherwise>
                    <img class="img-in-img-block" src="${pageContext.request.contextPath}/img/${quest.getImage()}.png">
                </c:otherwise>
            </c:choose>
            <p class="p-in-img-block"><a class="list-quest-a" href="game-start?questId=${quest.id}">${quest.name}</a></p>
        </div>
    </c:forEach>
</div>

</body>
</html>
