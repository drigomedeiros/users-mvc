/**
 * Single Page - Partial Loading
 * ------------------
 */
function addNavigationHooks(jQuery) {
    $(".spaNavigation").each(function () {
        var navigationElement = this;
        navigationElement.addEventListener("click", function(event) {
            event.preventDefault();
            jQuery.ajax({
                url: event.target.href,
                headers: {"Authorization": sessionStorage.getItem("Authorization")},
                type: "GET",
                success: function (data) {
                    $("#appRoot").html(data);
                },
                error: function (data) {
                    $("#appRoot").html(data);
                }
            });
        });
    });
};