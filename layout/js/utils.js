// WACKY functions

$(document).ready(function(){

  // BEWARE the event delegation

  // Switch map view
  $("body").delegate(".js-map-toggle-view", "click", function() {
    $(".map").toggleClass("list-view");
  })

});
