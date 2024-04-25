<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="parts/header-authorized.jsp" %>

<div class="base-edit-quest">
    <div class="edit-center-quest">
        <div class="edit-block-question">
            <div class="edit-image-inline">
                <div class="border-for-image">
                    <img class="image-in-quest-edit"
                         src="${pageContext.request.contextPath}/img/quest-default.png">
                </div>
            </div>
            <%-- Блок для добавления основных свойств квеста: имя, описание --%>
            <div class="edit-question-inline">
                <h1 class="quest-edit-h1">Создать квест:</h1>
                <form class="form-edit-quest" method="post">
                    <fieldset class="form-edit-quest">
                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label" for="name">Имя квеста</label>
                            </div>
                            <input class="edit-quest-text-input" id="name"
                                   name="name"
                                   type="text"
                                   value="введите название квеста"
                                   required="">
                        </div>

                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label" for="description">Описание квеста</label>
                            </div>
                            <textarea class="edit-quest-text-textarea"
                                      id="description"
                                      name="description"
                                      required="">введите описание квеста</textarea>
                        </div>

                        <button class="button-add-components" name="button-add-quest">Добавить</button>

                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>