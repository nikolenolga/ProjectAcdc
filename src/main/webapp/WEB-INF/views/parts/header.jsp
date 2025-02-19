<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Quest</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/styles.css">
</head>
<body>
<header>
    <div class="logo">
        <a href="/quests">
            <img src="images/logo" width="177px" height="40px" alt="logo"/>
        </a>
    </div>
    <nav class="menu">
        <ul>
            <li><a href="/quests">Квесты</a></li>
        </ul>
    </nav>
    <div class="authorize-block">
        <ul>
            <li><a href="/login">Вход</a></li>
            <li><a href="/registration">Регистрация</a></li>
        </ul>
    </div>
</header>