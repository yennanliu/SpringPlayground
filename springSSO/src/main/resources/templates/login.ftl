<!DOCTYPE html>
<html lang="en">
<!-- https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/resources/templates/authorize.ftl -->
<head>
    <meta charset="UTF-8">
    <title>Oauth2.0 统一授权中心</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/css/login.css">
</head>
<body>
<div class="authcenter" id="J-authcenter">
    <!--头部代码定义-->
    <div class="authcenter-head">
        <div class="container fn-clear">
            <ul class="container-left">
                <li class="container-left-item container-left-first">
                    <a href="#" class="on" target="_blank">SSO统一授权登录中心</a>
                </li>
            </ul>
            <ul class="container-right">
                <li class="container-right-item"><a href="https://www.baidu.com/" target="_blank" title="统一授权中心首页">统一授权中心首页</a></li>
            </ul>
        </div>
    </div>
    <!--登录代码Form表单定义-->
    <div class="authcenter-body fn-clear">
        <div class="authcenter-body-login">
            <ul class="ui-nav" id="J-loginMethod-tabs">
                <li class="active" data-status="show_login">账号登录</li>
            </ul>
            <div class="login login-modern " id="J-login">
                <form name="loginForm" id="login" action="/login" method="post" class="ui-form"
                      novalidate="novalidate"
                      data-widget-cid="widget-3" data-qrcode="false"><input type="hidden" name="ua" id="UA_InputId" value="">
                    <fieldset>
                        <div class="ui-form-item" id="J-username">
                            <label id="J-label-user" class="ui-label">
                                <span class="ui-icon ui-icon-userDEF"><i class="iconauth-men"></i></span>
                            </label>
                            <input type="text" id="J-input-user" class="ui-input ui-input-normal" name="username"
                                   tabindex="1" value="" autocomplete="off" maxlength="100" placeholder="帐号">
                            <div class="ui-form-explain"></div>
                        </div>
                        <div class="ui-form-item ui-form-item-20pd" id="J-password">
                            <label id="J-label-editer" class="ui-label" data-desc="登录密码">
                                <span class="ui-icon ui-icon-securityON" id="safeSignCheck"><i class="iconauth-lock"></i></span>
                            </label>
                            <span class="alieditContainer" id="password_container">
                                <input type="password" tabindex="2" id="password_rsainput" name="password"
                                       class="ui-input i-text" value="" placeholder="密码"></span>
                            <p class="ui-form-other ui-form-other-fg">
                                <a class="textlink forgot" href="javascript:void(0)" tabindex="5"></a>
                            </p>
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <div class="ui-form-explain"></div>
                        </div>
                        <!--登录按钮-->
                        <div class="ui-form-item ui-form-item-30pd" id="J-submit">
                            <input type="submit" value="登 录" class="ui-button" id="J-login-btn">
                            <p class="ui-form-other">
                                <a class="register" href="javascript:void(0)" target="_blank">免费注册</a>
                            </p>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
<!--页面尾部定义-->
<div class="authcenter-foot" id="J-authcenter-foot">
    <div class="authcenter-foot-container">
        <p class="authcenter-foot-link">
            <a href="https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU3NDY4NzQwNQ==&scene=124#wechat_redirect">关于无敌码农</a>
        </p>
        <div class="copyright">
            <a href="" target="_blank">Copyright©&nbsp;&nbsp;微信公众号(无敌码农)</a>
        </div>
    </div>
</div>
</body>
</html>