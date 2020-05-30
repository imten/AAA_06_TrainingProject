$(document).ready(function () {
    $("#loginname").blur(function(){
        var value = this.value;
        var node = this;
        $.post('/user/checkUsername',{login:value},function (data) {
            if(data=='false'){
                layer.msg('['+value+']已经被占用',{icon: 5});//!,ok,wrong,question,lock,cry,smile
                node.focus();
            }

        });
    })
});
