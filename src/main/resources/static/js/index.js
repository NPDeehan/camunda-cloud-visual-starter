var stompClient = null;
var $loading = $('#loadingDiv').hide();
$(document)
  .ajaxStart(function () {
    $loading.show();
  })
  .ajaxStop(function () {
    $loading.hide();
  });

function connect() {
    var socket = new SockJS('/twitter-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/topic/message', function (greeting) {
            console.log(greeting.body)
            $('.response').append('<br>'+greeting.body+'</br>');
        });
    });
}
connect();


$( "#form" ).submit(function( event ) {

  event.preventDefault();
    var data = { processKey: $("#ProcessKey").val(), processVars: $("#Message").val()};
  $.ajax({
    method: "POST",
    contentType:"application/json; charset=utf-8",
    dataType:"json",
    url: "/localStartProcess",
    data: JSON.stringify(data)
  })
    .done(function( msg ) {
    		$('.response').append('<br><p style="border:2px solid DodgerBlue;">'+msg+'</p>');
      
    }).fail(function( jqXHR, textStatus ) {
    	  $('.response').append('<p>'+jqXHR.responseText+'</p>');
    });
});

function sendMessage() {
  document.getElementById("demo").style.color = "red";
}

$( "#messageform" ).submit(function( event ) {

  event.preventDefault();
    var data = { processKey: $("#messageName").val(), processVars: $("#correlationKey").val()};
  $.ajax({
    method: "POST",
    contentType:"application/json; charset=utf-8",
    dataType:"json",
    url: "/localSendMessage",
    data: JSON.stringify(data)
  })
    .done(function( msg ) {
    		$('.response').append('<br><p style="border:2px solid DodgerBlue;">'+msg+'</p>');

    }).fail(function( jqXHR, textStatus ) {
    	  $('.response').append('<p>'+jqXHR.responseText+'</p>');
    });
});

!function(t,e,i,n) {
    function s(e,i){this.element=e,this.$element=t(e),this.init()}
    var h="textareaAutoSize",o="plugin_"+h,r = function(t){return t.replace(/\s/g,"").length>0};
    s.prototype={init:function(){var i=parseInt(this.$element.css("paddingBottom"))+parseInt(this.$element.css("paddingTop"))+parseInt(this.$element.css("borderTopWidth"))+parseInt(this.$element.css("borderBottomWidth"))||0;r(this.element.value)&&this.$element.height(this.element.scrollHeight-i),this.$element.on("input keyup",function(n){var s=t(e),h=s.scrollTop();
        t(this).height(0).height(this.scrollHeight-i),s.scrollTop(h)})}},t.fn[h]=function(e){return this.each(function(){t.data(this,o)||t.data(this,o,new s(this,e))}),this}}(jQuery,window,document);

$('.textarea-autosize').textareaAutoSize();