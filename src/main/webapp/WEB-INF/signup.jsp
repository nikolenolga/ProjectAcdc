<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp" %>
<div class="container">
    <form class="form-horizontal" action="signup" method="post"
          enctype="multipart/form-data">
        <fieldset>

            <!-- Form Name -->
            <legend>User Form</legend>

            <!-- File Button -->
            <div class="form-group">

                <label class="col-md-4 control-label" for="image">
                    <div class="form-group">
                        <img id="previewId" src="images/${user.image}" width="150" alt="${user.image}">
                    </div>
                    Нажмите чтобы изменить
                </label>
                <div class="col-md-4">
                    <input onchange="PreviewImage('image','previewId');" id="image" name="image"
                           style="visibility:hidden;"
                           class="input-file" type="file">
                </div>
            </div>

            <script type="text/javascript">
                function PreviewImage(inputFileId, imageId) {
                    var oFReader = new FileReader();
                    oFReader.readAsDataURL(document.getElementById(inputFileId).files[0]);
                    oFReader.onload = function (oFREvent) {
                        document.getElementById(imageId).src = oFREvent.target.result;
                    };
                }
            </script>

            <!-- Text input-->
            <div class="form-group">
                <label class="col-md-4 control-label" for="userLogin">Login</label>
                <div class="col-md-4">
                    <input id="userLogin" name="login" type="text" placeholder=""
                           class="form-control input-md" required="">
                </div>
            </div>

            <!-- Password input-->
            <div class="form-group">
                <label class="col-md-4 control-label" for="password">Password</label>
                <div class="col-md-4">
                    <input id="password" name="password" type="password" placeholder=""
                           class="form-control input-md"
                           required="">

                </div>
            </div>

            <!-- Select Basic -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="role">Role</label>
                <div class="col-md-4">
                    <select id="role" name="role" class="form-control">
                        <c:forEach items="${applicationScope.roles}" var="role">
                            <option value="${role}" ${"GUEST"==role?"selected":""}>${role}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <!-- Button (Double) -->
            <div class="form-group">
                <label class="col-md-4 control-label" for="create"></label>
                <div class="col-md-8">
                    <button id="create" name="create"
                            class="btn btn-success">Sign-up
                    </button>
                </div>
            </div>

        </fieldset>
    </form>
</div>
<%@include file="parts/footer.jsp" %>
