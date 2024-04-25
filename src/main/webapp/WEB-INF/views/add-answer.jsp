<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="parts/header-authorized.jsp" %>

<div class="base-edit-quest">
    <div class="edit-center-quest">
        <div class="edit-block-answer">

            <form class="form-edit-quest" method="post">
                <fieldset class="form-edit-quest">

                    <div class="edit-image-inline">
                        <div class="border-for-image">
                            <img class="image-in-quest-edit"
                                 src="${pageContext.request.contextPath}/img/answer-default.png">
                        </div>
                    </div>
                    <%-- Блок для редактирования основных свойств квеста: имя, описание --%>
                    <div class="edit-question-inline">
                        <h2 class="answer-edit-h2">Редактировать ответ:</h2>

                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label">Текст ответа</label>
                            </div>
                            <textarea class="edit-quest-text-textarea"
                                      name="answerMessage"
                                      required="">введите ответ</textarea>
                        </div>

                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label">Финальный текст ответа</label>
                            </div>
                            <textarea class="edit-quest-text-textarea"
                                      name="finalMessage"
                                      ></textarea>
                        </div>


                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label">Статус игры для ответа</label>
                            </div>
                            <select name="gameState"
                                    class="edit-quest-text-textarea">
                                <c:forEach var="gameState" items="${applicationScope.gameStates}">
                                    <option value="${gameState}">${gameState}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <c:if test="${!answer.isFinal()}">
                            <div class="horizontal-edit-quest">
                                <div class="edit-quest-label">
                                    <label class="edit-quest-label">Номер следующего вопроса</label>
                                </div>
                                <input class="edit-quest-text-input"
                                       name="nextQuestionId"
                                       type="text"
                                       value="0"
                                       required="">
                            </div>
                        </c:if>

                        <button class="buttons-for-edit-quest" name="button-back">Назад</button>
                        <button class="button-add-components" name="button-add-answer">Добавить</button>

                    </div>

                </fieldset>
            </form>

        </div>
    </div>
</div>

</body>
</html>