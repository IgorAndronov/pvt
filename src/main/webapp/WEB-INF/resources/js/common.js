/**
 * Created by igor on 11.07.15.
 */
//keep footer bottom
$(document).ready(function(){
    if ($(document).height() <= $(window).height()){
        $("#footer").addClass("navbar-fixed-bottom row-fluid");
    }
});

