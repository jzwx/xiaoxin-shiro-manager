var picCode;

$(function () {
    picCode = drawPic(); //登录页面验证码初始化
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        //监控提交
        form.on("submit(login)", function () {
            login();
            return false;
        });
    })
})

function login() {
    // var flag = checkParams();
    // if (flag != false) {
        $.post("/user/login", $("#useLogin").serialize(), function (data) {
            console.log("data:" + data);
            if(data.code == "1000"){
                layer.alert("登录成功",function () {
                    window.location.href="/home";
                });
            }else{
                picCode = drawPic();
                $("#code").val("");
                layer.alert(data.message,function () {
                   layer.closeAll();//关闭所有弹框
                });
            }
        });
    // }
}

// function checkParams(){
//     //  校验
//     var username=$("#username").val();
//     var password=$("#password").val();
//     // var mobile=$("#mobile").val();
//     var code=$("#code").val();
//     if("ok"!=ValidateUtils.checkUserName(username) || "ok"!=ValidateUtils.checkSimplePassword(password)){
//         layer.alert("请您输入正确的用户名和密码");
//         return false;
//     }
//     // if("ok"!=ValidateUtils.checkMobile(mobile)){
//     //     //tips层-右
//     //     layer.tips(ValidateUtils.checkMobile(mobile), '#mobile', {
//     //         tips: [2, '#78BA32'], //还可配置颜色
//     //         tipsMore: true
//     //     });
//     //     return false;
//     // }
//     if("ok"!=ValidateUtils.checkPicCode(code)){
//         //tips层-右
//         layer.tips(ValidateUtils.checkPicCode(code), '#canvas', {
//             tips: [2, '#78BA32'], //还可配置颜色
//             tipsMore: true
//         });
//         return false;
//     }
//     if(picCode.toLowerCase()!=code.toLowerCase()){
//         //tips层-右
//         layer.tips("请您输入正确的验证码", '#canvas', {
//             tips: [2, '#78BA32'], //还可配置颜色
//             tipsMore: true
//         });
//         return false;
//     }
// }