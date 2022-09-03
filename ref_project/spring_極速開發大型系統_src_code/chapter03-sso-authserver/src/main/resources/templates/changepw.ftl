<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>修改密码</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.css"/>
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script src="../js/bootstrap.js"></script>
    <style>
        html, body {
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
        <!-- background: url('./images/bg-3.jpg') no-repeat fixed;
        --> background-size: cover;

        }

        .container {
            padding-top: 150px;
            max-width: 450px;
        }

        .bg {
            border: 1px solid transparent;
            background: rgba(247, 247, 247, .5);
            border-radius: 5px;
        }

    </style>
</head>
<body>
<div class="container">
    <input type="hidden" id="srcPage" value="${srcPage}">
    <form class="form-horizontal bg" role="form">
        <div class="form-group text-center">
            <h4>修改密码</h4>
        </div>
        <div class="form-group">
            <label for="username" class="col-sm-3 control-label">账户</label>
            <div class="col-sm-9">
                <input type="text" class="form-control" style="width:250px;" id="username" placeholder="账号"><span
                        id="usernameTip" style="display:none;color:red;"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="oldpass" class="col-sm-3 control-label">旧密码</label>
            <div class="col-sm-9">
                <input type="password" class="form-control" style="width:250px;" id="oldpass" placeholder="旧密码"><span
                        id="oldpassTip" style="display:none;color:red;"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="newpass" class="col-sm-3 control-label">新密码</label>
            <div class="col-sm-9">
                <input type="password" class="form-control" style="width:250px;" id="newpass" placeholder="新密码"><span
                        id="newpassTip" style="display:none;color:red;"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="newpassAgain" class="col-sm-3 control-label">确认新密码</label>
            <div class="col-sm-9">
                <input type="password" class="form-control" style="width:250px;" id="newpassAgain"
                       placeholder="再次输入新密码"><span id="newpassAgainTip" style="display:none;color:red;"></span>
            </div>
        </div>
        <div class="form-group text-center">
            <button type="button" class="btn btn-primary" id="submit">确认</button>
            <button type="button" class="btn btn-primary" id="cancel">取消</button>
        </div>
    </form>
</div>
<div id="modifySuccess" class="alert alert-success alert-dismissable" style="width:50%;margin-left:40%;display:none;">
    <strong>Success!</strong> 你已成功修改密码!请重新访问原地址！
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var error = false;

        $("#username").blur(function () {
            var username = $("#username").val();
            if (username == '') {
                showError('username', '账户不能为空');
                error = true;
            } else {
                $("#username").css({"border-color": "green"});
                $("#usernameTip").css({"display": "none"});
                error = false;
            }
        });

        $("#oldpass").blur(function () {
            $("#username").blur();
            if (error) {
                return;
            }

            var oldpass = $("#oldpass").val();
            if (oldpass == '') {
                showError('oldpass', '密码不能为空');
                error = true;
            } else {
                $("#oldpass").css({"border-color": "green"});
                $("#oldpassTip").css({"display": "none"});
                error = false;
            }
        });

        $("#newpass").blur(function () {
            var newpass = $("#newpass").val();
            if (newpass == '' || newpass.length < 6) {
                showError('newpass', '新密码不能为空，且长度不能低于6位');
                error = true;
            } else if ($("#newpassAgain").val() != '') {
                // 如果已经输入了确认密码，则在判断下确认密码
                if ($("#newpassAgain").val() != newpass) {
                    showError('newpass', '与输入的确认密码不一致');
                    error = true;
                } else {
                    $("#newpass").css({"border-color": "green"});
                    $("#newpassTip").css({"display": "none"});
                    $("#newpassAgain").css({"border-color": "green"});
                    $("#newpassAgainTip").css({"display": "none"});
                    error = false;
                }
            } else {
                $("#newpass").css({"border-color": "green"});
                $("#newpassTip").css({"display": "none"});
                error = false;
            }
        });

        $("#newpassAgain").blur(function () {
            $("#newpass").blur();
            if (error) {
                return;
            }

            var newpassAgain = $("#newpassAgain").val();
            var newpass = $("#newpass").val();
            if (newpassAgain != newpass) {
                showError('newpassAgain', '与输入的新密码不一致');
                error = true;
            } else {
                $("#newpass").css({"border-color": "green"});
                $("#newpassTip").css({"display": "none"});
                $("#newpassAgain").css({"border-color": "green"});
                $("#newpassAgainTip").css({"display": "none"});
                error = false;
            }
        });

        $("#submit").click(function (event) {
            event.preventDefault();
            $("#oldpass").blur();
            if (error) {
                return false;
            }
            $("#newpassAgain").blur();
            if (error) {
                return false;
            }

            var username = $("#username").val();
            var newpass = $("#newpass").val();
            var oldpass = $('#oldpass').val();
            if (oldpass == newpass) {
                showError('newpass', '新密码和旧密码不能一致。');
                return false;
            }
            $.post(
                '/authserver/changepw',
                {oldpass: oldpass, username: username, newpass: newpass},
                function (data) {
                    if (data.code) {
                        $(".form-horizontal").hide();
                        $("#modifySuccess").css({'display': 'inline'}).html(
                            "<strong>Success!</strong> 你已成功修改密码!3秒后将重新跳转到登陆页面，请稍等！");
                        $.ajax("/logout");
                        setTimeout(function () {
                            var srcPage = $('#srcPage').val();
                            location.href = srcPage;
                        }, 3000);
                    } else {
                        $(".form-horizontal").hide();
                        $("#modifySuccess").css({'display': 'inline'}).html("<strong>ERROR!</strong> " + data.errMsg);
                    }
                }, "json");
            return false;
        });

        $("#cancel").click(function () {
            $(".form-horizontal").reset();
        })
    });

    function showError(formSpan, errorText) {
        $("#" + formSpan).css({"border-color": "red"});
        $("#" + formSpan + "Tip").empty();
        $("#" + formSpan + "Tip").append(errorText);
        ;
        $("#" + formSpan + "Tip").css({"display": "inline"});
    }

</script>
</body>
</html>
