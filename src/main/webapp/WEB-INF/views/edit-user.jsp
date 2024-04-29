<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="parts/header-authorized.jsp"%>


<div class="base-edit">
    <div class="edit-center">
        <div class="edit-for-block">
            <div class="edit-inline-block">
                <div class="base-user-img">
                    <img class="edit-user-image" id="image" src="images/${sessionScope.user.getImage()}" onclick="selectImage()">
                </div>

                <form class="form-edit-img"
                      method="post"
                      enctype="multipart/form-data">
                    <input type="file" id="imageFile" name="file" style="display: none">
                    <button class="buttons-edit" name="button-user-img-load">Загрузить изображение</button>
                </form>
            </div>
            <div class="edit-inline-block-right">
                <form class="form-edit" method="post">
                    <fieldset class="form-edit">
                        <div class="base-center-edit-info">
                            <div>
                                <h2 class="greeting">Привет, ${sessionScope.user.name}!</h2>
                            </div>

                            <!-- Text input-->
                            <div class="form-group">
                                <p class="input-label"><label for="name">Имя</label></p>
                                <div class="col-md-5">
                                    <input id="name"
                                           name="name"
                                           type="text"
                                           value="${sessionScope.user.name}"
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
                                           value="${sessionScope.user.login}"
                                           placeholder="your login"
                                           class="login-input"
                                           required=""
                                           readonly>
                                    <span class="help-block"></span>
                                </div>
                            </div>

                            <!-- Password input-->
                            <div class="form-group">
                                <p class="input-label"><label for="password">Пароль</label></p>
                                <div class="col-md-5">
                                    <input id="password"
                                           name="password"
                                           type="password"
                                           value="${sessionScope.user.password}"
                                           placeholder="your password"
                                           class="login-input"
                                           required="">
                                    <span class="help-block">min 8 symbols</span>
                                </div>
                            </div>
                        </div>

                        <!-- Button (Double) -->
                        <button class="buttons-user-edit" name="button-submit">Сохранить</button>
                        <button class="buttons-user-edit" name="button-exit">Выход</button>
                    </fieldset>
                </form>
                <c:if test="${requestScope.hasAlerts}">
                    <p class="alert">${requestScope.alert}</p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script>
    function selectImage() {
        document.getElementById('imageFile').click();
    }

    document.getElementById('imageFile').addEventListener('change', function() {
        var file = this.files[0];
        var reader = new FileReader();

        reader.onload = function(e) {
            document.getElementById('image').src = e.target.result;
        }

        reader.readAsDataURL(file);
    });
</script>

</body>
</html>
