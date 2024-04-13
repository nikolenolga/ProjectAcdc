<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${requestScope.quest.name}</title>
</head>
<body>
<h1>${requestScope.quest.name}</h1>
<p>${requestScope.quest.description}</p>
<div>
    <form method="post">
        <button>Start</button>
    </form>
</div>
</body>
</html>
