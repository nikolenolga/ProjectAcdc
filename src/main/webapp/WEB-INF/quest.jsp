<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:import url="parts/header.jsp"/>
<div class="container">
    <h5>${requestScope.quest.name}</h5>
    <c:forEach var="question" items="${requestScope.quest.questions}">
        <a id="bookmark${question.id}"></a>

        <div class="row">
            <div class="image-container" style="float: left; width: 33.33%">
                <img id="previewId${question.id}" src="images/${question.image}" class="img-fluid"
                     alt="${question.image}"
                     onclick="loadImageFile('image${question.id}','previewId${question.id}');"
                />
            </div>
            <div class="form-container" style="float: right; width: 66.67%">
                <form class="row row-cols-lg-auto g-3 align-items-center"
                      action="quest"
                      method="post" enctype="multipart/form-data"
                      id="form${question.id}">
                    <div class="form-group">
                        <textarea name="text" class="form-control"
                                  id="exampleFormControlTextarea1"
                                  rows="5">${question.text}</textarea>
                        <button type="submit" class="btn btn-primary">Обновить вопрос</button>
                        <input id="image${question.id}" name="image"
                               style="visibility:hidden;"
                               class="input-file" type="file">
                        <input name="questionId" type="hidden" value="${question.id}">
                        <input name="id" type="hidden" value="${requestScope.quest.id}">
                    </div>
                </form>
            </div>
        </div>
    </c:forEach>
</div>

<c:import url="parts/footer.jsp"/>

