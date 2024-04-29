<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="parts/header-authorized.jsp"%>

<div class="block">
    <c:forEach var="quest" items="${requestScope.quests}">
        <div class="block-item">
            <img class="img-in-img-block" src="images/${quest.getImage()}">
            <p class="p-in-img-block"><a class="list-quest-a" href="edit-quest?questId=${quest.id}">${quest.name}</a></p>
        </div>
    </c:forEach>
</div>

</body>
</html>
