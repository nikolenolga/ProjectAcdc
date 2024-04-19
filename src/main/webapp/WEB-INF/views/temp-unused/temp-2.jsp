<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../parts/header-authorized.jsp"%>



<form class="form-edit-quest" method="post">
    <fieldset class="form-edit-quest">
        <!-- Text input-->
        <div class="horizontal-edit-quest">
            <p class="edit-quest-label"><label for="name">Имя квеста</label></p>
            <input class="edit-quest-text-input" id="name"
                   name="name"
                   type="text"
                   value="${sessionScope.quest.name}"
                   required="">
        </div>

        <!-- Text input-->
        <div class="horizontal-edit-quest">
            <p class="edit-quest-label"><label for="description">Имя квеста</label></p>
            <input class="edit-quest-text-input" id="description"
                   name="description"
                   type="text"
                   value="${sessionScope.quest.description}"
                   required="">
        </div>

        <!-- Button (Double) -->
        <button class="buttons-for-edit-quest" name="button-edit-quest">Сохранить изменения</button>

    </fieldset>
</form>








</body>
</html>
