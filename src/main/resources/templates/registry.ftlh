<#include "head1.ftlh">
<body>

<div class="ui middle aligned center aligned grid">
    <div class="column">
        <h2 class="ui teal image header">
            <img src="http://www.lezijie.com/public/new/new/logo.png" class="image">
            <div class="content">
                Registry to your account
            </div>
        </h2>
        <form class="ui large form" onsubmit="return false;">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" id="email" name="email" placeholder="E-mail address">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" id="password" name="password" placeholder="Password">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="repassword" placeholder="Password">
                    </div>
                </div>
                <div class="ui fluid large teal submit button" id="registry">Registry</div>
            </div>

            <div class="ui error message"></div>

        </form>

        <div class="ui message">
            Already signed up <a href="/login">Login</a>
        </div>
    </div>
</div>
<script type="text/javascript" charset="UTF-8">
    $("#registry").on("click", function () {
        $.ajax({
            url: "user/create",
            type: "POST",
            data: {
                email: $("#email").val(),
                password: $("#password").val()
            },
            resultType: "JSON",
            success: function (result) {
                alert(result.message);
            },
            error: function (result) {
            }


        });
    });
</script>

</body>
</html>
