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
  $('body').delegate('.js-circle-root','mouseenter mouseleave', function(e) {

    // time

    // position
    if (e.type == 'mouseenter') {

      $(this).find('> .js-context-info').addClass('info-shown');

      $(this).find('> .js-context-info')
      .position({
        my: "center bottom+5%",
        at: "center top+5%",
        of: $(this),
        collision: "flipfit flipfit",
        within: ".map-circles-view"
      });

    } else {
      $(this).find('> .js-context-info').removeClass('info-shown');
    }
  });


});
