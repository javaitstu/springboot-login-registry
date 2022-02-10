<!DOCTYPE html>
<html>
<head>
    <#include "head.ftlh" />
</head>
<body>
<div class="ui middle aligned center aligned grid">
    <div class="column">
        <h2 class="ui teal image header">
            <img src="assets/images/logo.png" class="image">
            <div class="content">
                Log-in to your account
            </div>
        </h2>
        <form class="ui large form">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="email" placeholder="E-mail address">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="Password">
                    </div>
                </div>
                <div class="ui fluid large teal submit button" id="login">Login</div>
            </div>

            <div class="ui error message"></div>

        </form>

        <div class="ui message">
            New to us? <a href="/registry">Registry</a>
        </div>
    </div>
</div>
<script type="text/javascript" charset="UTF-8">
    $("#login").on("click", function () {
        $.ajax({
            url: "user/login",
            type: "POST",
            data: {
                email: $("#email").val(),
                password: $("#password").val()
            },
            resultType: "JSON",
            success: function (result) {
                alert(result.message);
                if (result.code == 200) {
                    window.location.href = "/";
                }
            },
            error: function (result) {
                result.message;
            }
        });
    });
</script>
</body>

</html>
