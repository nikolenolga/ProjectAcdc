<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="parts/head-part.jsp" %>

<div class="base">
    <div class="base-border">
        <div class="base-left">
            <div class="base-item-left">
                <h2 class="block-h2">${requestScope.quest.name}</h2>
                <p class="block-p">${requestScope.quest.description}</p>
            </div>
            <div class="base-item-left">
                <form method="post">
                    <button class="base-button" name="start">Начать</button>
                </form>
            </div>
        </div>
    </div>
    <c:if test="${requestScope.quest.img}">
        <div class="base-right">
            <div class="base-item-right">
                <img class="base-image" src="${pageContext.request.contextPath}/img/${requestScope.quest.getImage()}.png" alt="user-image">
            </div>
        </div>
    </c:if>
</div>

</body>
</html>
