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
        <a href="/quests">
            <img src="${pageContext.request.contextPath}/img/logo.png" width="177px" height="40px"/>
        </a>
    </div>
    <nav class="menu">
        <ul>
            <li><a href="/quests">Квесты</a></li>
            <li><a class="active" href="/user-quests">Редактор квестов</a></li>
        </ul>
    </nav>
    <div class="authorize-block">
        <ul><li><a href="/edit-user">Личный кабинет</a></li></ul>
    </div>
    <div class="logo">
        <a href="/edit-user">
            <img class="menu-image" src="${pageContext.request.contextPath}/img/${sessionScope.user.getImage()}.png" alt="user-image">
        </a>
    </div>
</header>