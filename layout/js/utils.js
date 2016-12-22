// WACKY functions

$(document).ready(function(){

  // BEWARE the event delegation

  // Switch map view
  $("body").delegate(".js-map-toggle-view", "click", function() {
    $(".map").toggleClass("list-view");
  });

  // Collapse / expand ideas & comments
  $("body").delegate(".js-collapse-ideas", "click", function() {
    $(".event-wrapper").addClass("ideas-collapsed");
  });
  $("body").delegate(".js-collapse-comments", "click", function() {
    $(".event-wrapper").addClass("comments-collapsed");
  });
  $("body").delegate(".js-expand-ideas", "click", function() {
    $(".event-wrapper").removeClass("ideas-collapsed");
  });
  $("body").delegate(".js-expand-comments", "click", function() {
    $(".event-wrapper").removeClass("comments-collapsed");
  });

  // Show circles info
  $('body').delegate('.js-circle-w-context','mouseenter mouseleave', function(e) {
// mouseover mouseout
    // time

    // position
    if (e.type == 'mouseenter') {
      // console.log('enter');
      $('.js-context-info').removeClass('info-shown');
      $(this).find('> .js-context-info').addClass('info-shown')
      .position({
        my: "center bottom+5%",
        at: "center top+5%",
        of: $(this),
        collision: "flipfit flipfit",
        within: ".map-circles-view"
      });

    } else {
      // console.log('out');
      $(this).find('> .js-context-info').removeClass('info-shown');
      //.css('top','').css('bottom','').css('left','').css('right','');
    }
  });

  // Display event info description
  $("body").delegate(".js-event-info-toggle", "click", function() {
    $(".js-event-info").toggleClass("expanded");
    $(this).toggleClass("expanded");
    /*$(".js-event-info").slideToggle('fast',"swing", function () {
      // Animation complete.
    });*/
  });

// AUTO RESIZING TEXT AREA
// https://stephanwagner.me/auto-resizing-textarea
  $("body").delegate("textarea", "focus", function() {
  //jQuery.each(jQuery('textarea'), function() {

      if (jQuery(this).data('autoresizeAttached')) return;

      var offset = this.offsetHeight - this.clientHeight;

      var resizeTextarea = function(el) {
          jQuery(el).css('height', 'auto').css('height', el.scrollHeight + offset);
      };
      jQuery(this).on('keyup input', function() { resizeTextarea(this); }).data('autoresizeAttached', true);
  });

});

// TEXTAREA SUBMIT
$("body").delegate(".js-ideas-textarea", "keypress", function(e) {
  if(e.which == 13) {
    $('#form-ideas .button').click();
    e.preventDefault();
  }
});
$("body").delegate(".js-comments-textarea", "keypress", function(e) {
  if(e.which == 13) {
    $('#form-comments .button').click();
    e.preventDefault();
  }
});
