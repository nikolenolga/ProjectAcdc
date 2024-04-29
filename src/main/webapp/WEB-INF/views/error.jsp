<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isErrorPage="true" %>

<c:choose>
    <c:when test="${sessionScope.authorized}">
        <%@ include file="parts/header-authorized.jsp"%>
    </c:when>
    <c:otherwise>
        <%@ include file="parts/header.jsp"%>
    </c:otherwise>
</c:choose>

<div class="base">
    <div class="base-border">
        <div class="base-left">
            <h1 class="block-h1">Exception Report</h1>
            <h2 class="block-h2">${requestScope.errorCode}</h2>
            <h2 class="block-h2">${requestScope.description}</h2>
            <h2 class="block-h2">${requestScope.errorMessage}</h2>
        </div>
    </div>
    <div class="base-right">
        <div class="base-item-right">
            <img class="base-image" src="images/error" alt="user-image">
        </div>
    </div>
</div>
</body>
</html>