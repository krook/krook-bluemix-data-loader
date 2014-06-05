<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Read results</title>
</head>
<body>
Read from PostgreSQL: <br />
<table>
<c:forEach items="${pList}" var="city">
  <tr>
    <td><c:out value="${city.id}" /><td>
    <td><c:out value="${city.name}" /><td>
    <td><c:out value="${city.countrycode}" /><td>
    <td><c:out value="${city.district}" /><td>
    <td><c:out value="${city.population}" /><td>         
  </tr>
</c:forEach>
</table>

Read from MongoDB: <br />
<table>
<c:forEach items="${mList}" var="city">
  <tr>
    <td><c:out value="${city.id}" /><td>
    <td><c:out value="${city.name}" /><td>
    <td><c:out value="${city.countrycode}" /><td>
    <td><c:out value="${city.district}" /><td>
    <td><c:out value="${city.population}" /><td>         
  </tr>
</c:forEach>
</table>
</body>
</html>