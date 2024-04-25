<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="parts/header-authorized.jsp" %>

<div class="base-edit-quest">
    <div class="center-text-editor">
        <div class="text-editor-block">
            <h1 class="text-edit-h1">Текстовый редактор:</h1>
            <c:if test="${requestScope.hasAlerts}">
                <p class="alert">${requestScope.alert}</p>
            </c:if>
            <form class="form-text-editor" method="post">
                <fieldset class="form-text-editor">

                    <!-- Text input-->
                    <div class="block-for-quest-text-editor">
                        <textarea class="text-editor-textarea"
                                  id="text"
                                  name="text"
                                  required="">введите описание квеста или загрузите шаблон</textarea>
                    </div>

                    <div class="block-for-quest-text-editor">
                        <button class="button-text-editor" name="button-add-quest">Добавить</button>
                        <button class="button-text-editor" name="button-export">Экспорт</button>
                        <button class="button-text-editor" name="button-reset">Сброс</button>
                        <button class="button-text-editor" name="button-sample">Шаблон</button>
                    </div>

                    <div class="block-for-quest-text-editor">
                        <p class="quest-text-label">${requestScope.rules}</p>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>

</body>
</html>