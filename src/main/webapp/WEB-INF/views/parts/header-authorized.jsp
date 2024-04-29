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
            <li>
                <div class="dropdown">
                    <a class="drop" href="/user-quests">Редактор квестов</a>
                    <div class="dropdown-content">
                        <a href="/user-quests">Редактировать квест</a>
                        <a href="/create-quest">Создать квест</a>
                        <a href="/quest-text-editor">Текстовый редактор</a>
                    </div>
                </div>
            </li>
        </ul>
    </nav>
    <div class="authorize-block">
        <ul><li><a href="/edit-user">Личный кабинет</a></li></ul>
    </div>
    <div class="logo">
        <a href="/edit-user">
            <img class="menu-image" src="images/${sessionScope.user.getImage()}"  alt="user-image">
        </a>
    </div>
</header>