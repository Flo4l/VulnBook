
function loadMore() {

}

function loadPosts(latestPostId, numPosts) {

}

function likePost(postId) {
    var url = "/post/like/" + postId;
    $.post(url,
        function(data, status) {
            if(status === "success") {
                var counter = $("#" + postId).find(".likecounter");
                var likes = parseInt(counter.html().toString().substr(1)) + 1;
                counter.html("+" + likes);
            }
        });
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1)
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}