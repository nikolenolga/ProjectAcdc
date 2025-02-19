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
                                  name="text"
                                  required=""
                        ><c:choose><c:when test="${sessionScope.text != null}">${sessionScope.text}</c:when><c:otherwise><%@ include file="../static/sample.txt" %></c:otherwise></c:choose></textarea>
                    </div>

                    <div class="block-for-quest-text-editor">
                        <button class="button-text-editor" name="button-add-quest">Добавить</button>
                        <button class="button-text-editor" name="button-reset">Сброс</button>
                    </div>

                    <div class="block-for-quest-text-editor">
                        <p class="quest-text-label">
                            <%@ include file="../static/rules.txt" %>
                        </p>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>

</body>
</html>