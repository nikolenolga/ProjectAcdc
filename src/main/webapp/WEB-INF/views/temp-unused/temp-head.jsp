<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Quest</title>
    <style>
        <%@ include file="/WEB-INF/static/styles.css" %>
    </style>
</head>
<body>
<header>
    <div class="logo">
        <a href="/">
            <img src="https://bureau.ru/soviet/130568/files/logo.svg" width="60" height="60px"/>
        </a>
    </div>
    <nav class="menu">
        <ul>
            <li><a href="/quests">Квесты</a></li>
            <li><a href="/services">Услуги</a></li>
            <li><span class="active">Блог</span></li>
            <li><a href="/contacts">Контакты</a></li>
        </ul>
    </nav>
    <div class="authorize-block">
        <ul>
            <c:choose>
                <c:when test="${sessionScope.authorized}">
                    <li><a href="/quests">Личный кабинет</a></li>
                    <li><img class="menu-image" src="${pageContext.request.contextPath}/img/${sessionScope.user.getImage()}.png" alt="user-image"></li>
                </c:when>
                <c:otherwise>
                    <li><a href="/login">Вход</a></li>
                    <li><a href="/register">Регистрация</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
    <div class="logo">
        <a href="/">
            <img class="menu-image" src="${pageContext.request.contextPath}/img/${sessionScope.user.getImage()}.png" alt="user-image">
        </a>
    </div>
</header>

<%--<div class="menu">--%>
<%--    <div>--%>
<%--        <div class="menu-item-left">--%>
<%--            <p>QuestRoom</p>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="menu-right">--%>
<%--        <c:choose>--%>
<%--            <c:when test="${sessionScope.authorized}">--%>
<%--                <div class="menu-item-right">--%>
<%--                    <p>Личный кабинет</p>--%>
<%--                </div>--%>
<%--                <img class="menu-image" src="${pageContext.request.contextPath}/img/${sessionScope.user.getImage()}.png" alt="user-image">--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <form id="menu-form" class="menu-form" method="post">--%>
<%--                    <div class="menu-item-right">--%>
<%--                        <button name="button-login">Вход</button>--%>
<%--&lt;%&ndash;                        <a class="menu-a" href="login">Вход</a>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                    <div class="menu-item-right">--%>
<%--                        <button name="button-register">Регистрация</button>--%>
<%--&lt;%&ndash;                        <a class="menu-a" href="login">Регистрация</a>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                </form>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
<%--    </div>--%>
<%--</div>--%>