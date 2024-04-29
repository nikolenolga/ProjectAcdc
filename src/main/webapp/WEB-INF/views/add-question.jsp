<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="parts/header-authorized.jsp" %>

<div class="base-edit-quest">
    <div class="edit-center-quest">
        <div class="edit-block-question">

            <form class="form-edit-quest" method="post">
                <fieldset class="form-edit-quest">
                    <div class="edit-image-inline">
                        <div class="border-for-image">
                            <img class="image-in-quest-edit"
                                 src="images/question-default">
                        </div>
                    </div>
                    <div class="edit-question-inline">
                        <h2 class="question-edit-h2">Создать вопрос:</h2>
                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label">Текст Вопроса</label>
                            </div>
                            <textarea class="edit-quest-text-textarea"
                                      name="questionMessage"
                                      required="">введите вопрос</textarea>
                        </div>

                        <button class="buttons-for-edit-quest" name="button-back">Назад</button>
                        <button class="button-add-components" name="button-add-question">Добавить</button>
                    </div>
                </fieldset>
            </form>

        </div>
    </div>
</div>

</body>
</html>