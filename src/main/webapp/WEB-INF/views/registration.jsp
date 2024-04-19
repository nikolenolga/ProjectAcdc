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

<div class="base-login">
    <div class="base-center">
        <div class="base-center-buttons">
            <div class="login-change-reg">
                <h2 class="login-h2"><a class="reg-block" href="/login">Вход</a></h2>
            </div>
            <div class="login-change-reg-picked" name="reg-submit">
                <h2 class="login-h2"><a class="reg-block" href="/registration">Регистрация</a></h2>
            </div>
        </div>
        <form method="post">
            <fieldset>
                <div class="base-center-info">

                    <!-- Text input-->
                    <div class="form-group">
                        <p class="input-label"><label for="name">Имя</label></p>
                        <div class="col-md-5">
                            <input id="name"
                                   name="name"
                                   type="text"
                                   value="User-exist-alert"
                                   placeholder="your name"
                                   class="login-input"
                                   required="">
                            <span class="help-block">min 3 symbols</span>
                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <p class="input-label"><label for="login">Логин</label></p>
                        <div class="col-md-5">
                            <input id="login"
                                   name="login"
                                   type="text"
                                   value="admin"
                                   placeholder="your login"
                                   class="login-input"
                                   required="">
                            <span class="help-block">min 3 symbols</span>
                        </div>
                    </div>

                    <!-- Password input-->
                    <div class="form-group">
                        <p class="input-label"><label for="password">Пароль</label></p>
                        <div class="col-md-5">
                            <input id="password"
                                   name="password"
                                   type="password"
                                   value="example-password"
                                   placeholder="your password"
                                   class="login-input"
                                   required="">
                            <span class="help-block">min 8 symbols</span>
                        </div>
                    </div>
                </div>

                <!-- Button (Double) -->
                <button class="login-button" name="button-submit">Подтвердить</button>

            </fieldset>
        </form>
        <c:if test="${requestScope.hasAlerts}">
            <p class="alert">${requestScope.alert}</p>
        </c:if>
    </div>
</div>

</body>
</html>
