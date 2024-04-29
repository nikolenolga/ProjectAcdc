<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.authorized}">
        <%@ include file="parts/header-authorized.jsp"%>
    </c:when>
    <c:otherwise>
        <%@ include file="parts/header.jsp"%>
    </c:otherwise>
</c:choose>
<div class="block">
    <c:forEach var="quest" items="${requestScope.quests}">
        <div class="block-item">
            <img class="img-in-img-block" src="images/${quest.getImage()}">
            <p class="p-in-img-block"><a class="list-quest-a" href="play?questId=${quest.id}">${quest.name}</a></p>
        </div>
    </c:forEach>
</div>

</body>
</html>
