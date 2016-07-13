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

});
