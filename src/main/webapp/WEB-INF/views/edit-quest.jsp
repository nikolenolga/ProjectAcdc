<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="parts/header-authorized.jsp" %>

<div class="base-edit-quest">
    <div class="edit-center-quest">
        <div class="edit-block-question">
            <%-- Блок для редактирования изображение--%>
            <div class="edit-image-inline">
                <div class="border-for-image">
                    <img class="image-in-quest-edit"
                         src="${pageContext.request.contextPath}/img/${requestScope.quest.getImage()}.png">
                </div>
                <button class="button-edit-quest-load-image" name="button-load-quest-image">Загрузить</button>
            </div>
            <%-- Блок для редактирования основных свойств квеста: имя, описание --%>
            <div class="edit-question-inline">
                <h1 class="quest-edit-h1">Редактировать квест:</h1>
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
                                   value="${requestScope.quest.name}"
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

                                      required="">${requestScope.quest.description}</textarea>
                        </div>

                        <!-- Text input-->
                        <div class="horizontal-edit-quest">
                            <div class="edit-quest-label">
                                <label class="edit-quest-label">Номер первого вопроса</label>
                            </div>
                            <input class="edit-quest-text-input"
                                   name="firstQuestionId"
                                   type="text"
                                   value="${quest.firstQuestionId}"
                                   >
                        </div>

                        <button class="three-buttons" name="button-edit-quest">Сохранить</button>
                        <button class="three-buttons" name="button-delete-quest">Удалить</button>
                        <button class="three-buttons" name="button-add-question">Добавить вопрос</button>

                    </fieldset>
                </form>
            </div>
        </div>
        <c:forEach var="question" items="${requestScope.questions}">
            <%-- Блок для редактирования вопроса--%>
            <div class="edit-block-question">
                <form class="form-edit-quest" method="post">
                    <fieldset class="form-edit-quest">
                            <%-- Блок для редактирования изображения к вопросу--%>
                        <div class="edit-image-inline">
                            <div class="border-for-image">
                                <img class="image-in-quest-edit"
                                     src="${pageContext.request.contextPath}/img/${question.getImage()}.png">
                            </div>
                            <button class="button-edit-quest-load-image" name="button-load-question-image">Загрузить
                            </button>
                        </div>
                            <%-- Блок для редактирования основных свойств квеста: имя, описание --%>
                        <div class="edit-question-inline">
                            <h2 class="question-edit-h2">Редактировать вопрос:</h2>
                            <!-- Text input-->
                            <div class="horizontal-edit-quest">
                                <div class="edit-quest-label">
                                    <label class="edit-quest-label">Номер вопроса</label>
                                </div>
                                <input class="edit-quest-text-input"
                                       name="questionId"
                                       type="text"
                                       value="${question.id}"
                                       required=""
                                       readonly>
                            </div>

                            <!-- Text input-->
                            <div class="horizontal-edit-quest">
                                <div class="edit-quest-label">
                                    <label class="edit-quest-label">Текст Вопроса</label>
                                </div>
                                <textarea class="edit-quest-text-textarea"
                                          name="questionMessage"
                                          required="">${question.questionMessage}</textarea>
                            </div>

                            <button class="three-buttons" name="button-edit-question">Сохранить</button>
                            <button class="three-buttons" name="button-delete-question">Удалить</button>
                            <button class="three-buttons" name="button-add-answer">Добавить ответ</button>
                        </div>
                    </fieldset>
                </form>
            </div>
            <%-- Ответы для текущего вопроса--%>
            <c:forEach var="answer" items="${question.possibleAnswers}">
                <%-- Блок для редактирования ответа--%>
                <div class="edit-block-answer">
                    <form class="form-edit-quest" method="post">
                        <fieldset class="form-edit-quest">
                                <%-- Блок для редактирования изображения к ответу--%>
                            <div class="edit-image-inline">
                                <div class="border-for-image">
                                    <img class="image-in-quest-edit"
                                         src="${pageContext.request.contextPath}/img/${answer.getImage()}.png">
                                </div>
                                <button class="button-edit-quest-load-image" name="button-load-answer-image">Загрузить
                                </button>
                            </div>
                                <%-- Блок для редактирования основных свойств квеста: имя, описание --%>
                            <div class="edit-question-inline">
                                <h2 class="answer-edit-h2">Редактировать ответ:</h2>
                                <!-- Text input-->
                                <div class="horizontal-edit-quest">
                                    <div class="edit-quest-label">
                                        <label class="edit-quest-label">Номер вопроса</label>
                                    </div>
                                    <input class="edit-quest-text-input"
                                           name="questionId"
                                           type="text"
                                           value="${question.id}"
                                           required=""
                                           readonly>
                                </div>

                                <!-- Text input-->
                                <div class="horizontal-edit-quest">
                                    <div class="edit-quest-label">
                                        <label class="edit-quest-label">Номер ответа</label>
                                    </div>
                                    <input class="edit-quest-text-input"
                                           name="answerId"
                                           type="text"
                                           value="${answer.id}"
                                           required=""
                                           readonly>
                                </div>

                                <!-- Text input-->
                                <div class="horizontal-edit-quest">
                                    <div class="edit-quest-label">
                                        <label class="edit-quest-label">Текст ответа</label>
                                    </div>
                                    <textarea class="edit-quest-text-textarea"
                                              name="answerMessage"
                                              required="">${answer.answerMessage}</textarea>
                                </div>

                                <!-- Text input-->
                                <div class="horizontal-edit-quest">
                                    <div class="edit-quest-label">
                                        <label class="edit-quest-label">Финальный текст ответа</label>
                                    </div>
                                    <textarea class="edit-quest-text-textarea"
                                              name="finalMessage"
                                              >${answer.finalMessage}</textarea>
                                </div>


                                <div class="horizontal-edit-quest">
                                    <div class="edit-quest-label">
                                        <label class="edit-quest-label">Статус игры для ответа</label>
                                    </div>
                                    <select name="gameState"
                                            class="edit-quest-text-textarea">
                                        <c:forEach var="gameState" items="${applicationScope.gameStates}">
                                            <option value="${gameState}" ${gameState == answer.gameState ? "selected" : ""}>${gameState}</option>
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
                                               value="${answer.nextQuestionId}"
                                               required="">
                                    </div>
                                </c:if>

                                <button class="buttons-for-edit-quest" name="button-edit-answer">Сохранить</button>
                                <button class="button-add-components" name="button-delete-answer">Удалить ответ</button>
                            </div>
                        </fieldset>
                    </form>
                </div>

            </c:forEach>
        </c:forEach>

    </div>
</div>

</body>
</html>