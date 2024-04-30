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
        <div class="base-left">
            <div class="base-item-left">
                <h2 class="block-h2">${requestScope.quest.name}</h2>
                <p class="block-p-description">${requestScope.quest.description}</p>
            </div>
            <div class="base-item-left">
                <form method="post">
                    <button class="base-button" name="button-start">Начать</button>
                </form>
            </div>
        </div>
    </div>
    <div class="base-right">
        <div class="base-item-right">
            <img class="base-image" src="images/${requestScope.quest.getImage()}" alt="user-image">
        </div>
    </div>
</div>

</body>
</html>
