<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Quest</title>
    <style><%@ include file="/WEB-INF/static/styles.css"%></style>
</head>
<body>
<div class="menu">
    <div>
        <div class="menu-item-left">
            <p>QuestRoom</p>
        </div>
    </div>
    <div class="menu-right">
        <c:choose>
            <c:when test="${sessionScope.authorized}">
                <div class="menu-item-right">
                    <p>Личный кабинет</p>
                </div>
                <img class="menu-image" src="${pageContext.request.contextPath}/img/${sessionScope.user.getImage()}.png" alt="user-image">
            </c:when>
            <c:otherwise>
                <form class="menu-form" method="post">
                    <div class="menu-item-right">
                        <button name="login">Вход</button>
                    </div>
                    <div class="menu-item-right">
                        <button name="register">Регистрация</button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>