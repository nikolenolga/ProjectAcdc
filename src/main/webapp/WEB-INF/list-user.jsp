<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp" %>
<div class="container">


    <c:forEach var="user" items="${requestScope.users}">
        <img src="images/${user.image}" alt="images/${user.image}" width="100px">
        Edit user <a href="edit-user?id=${user.id}">${user.login}</a> <br> <br>
    </c:forEach>

    <p>
        <a href="signup" class="top-nav">Create new user</a>
    </p>

    <h3>TODO: Тут еще нужно будет подключить безопасность</h3>

</div>
<%@include file="parts/footer.jsp" %>

