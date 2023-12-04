<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

    <link rel="stylesheet" type="text/css"
          href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
    <c:url value="/css/main.css" var="jstlCss" />
    <link href="${jstlCss}" rel="stylesheet" />

</head>
<body>
<div class="container">
    <header>
        <h1>Spring MVC + JSP + JPA + Spring Boot 2</h1>
    </header>
    <div class="starter-template">
        <h1>Company List</h1>
        <table
                class="table table-striped table-hover table-condenstemplatesed table-bordered">
            <tr>
                <th>Id</th>
                <th>Name</th>
            </tr>
            <c:forEach var="company" items="${company}">
                <tr>
                    <td>${company.id}</td>
                    <td>${company.name}</td>
                </tr>
            </c:forEach>
        </table>
    </div>

</div>

<script type="text/javascript"
        src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>

</html>