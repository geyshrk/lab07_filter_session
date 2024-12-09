<head>
    <title>Login</title>
    <meta charset="utf-8"/>
</head>
<body>
<h1>Enter password</h1>

<form action="/usercheck" method="post">
    <div>
        <label>Username</label>
        <input type="text" name="username">
    </div>
    <div>
        <label>Password</label>
        <input type="password" name="password">
    </div>
    <div>
        <input type="submit" value="OK">
    </div>
    <div>
        <input type="checkbox" name="rememberMe">
        <label> Remember me </label>
    </div>
</form>
<form action="/registerpage" method="get">
    <div>
        <input type="submit" value="register">
    </div>
</form>

</body>
</html>