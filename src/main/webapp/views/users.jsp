<%@ page import="java.util.List" %>
<%@ page import="ru.msnigirev.oris.authorisation.session.entity.User" %>
<html>
    <head>
        <title>Users</title>
    </head>
    <body>
        <table>
            <th>Username</th>
            <th>Email</th>
            <th>Phone Number</th>

            <%
                List<User> users = (List<User>) request.getAttribute("users");
                for (User u : users) {
            %>
            <tr>
                <td><%=u.getUsername()%></td>
                <td><%=u.getEmail()%></td>
                <td><%=u.getPhoneNumber()%></td>
            </tr>
            <%
                }
            %>

        </table>
        <form action="/logout" method="post">
            <div>
                <input type="submit" value="logout">
            </div>
        </form>
    </body>
</html>