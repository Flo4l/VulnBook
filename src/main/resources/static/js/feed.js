
function loadMore() {
    var url = "/posts/retrieve";
    var idFirst = parseInt($(".posts").attr("data-last")) - 1;
    var numPosts = 20;

    $.post(url,
        {
            idFirst: idFirst,
            numPosts: numPosts
        },
        function(data, status) {
            if(status === "success") {
                var posts = JSON.parse(data);
                posts.forEach(function(post) {
                    insertPost(post.postid, post.username, post.text, post.time, post.likes);
                });

                var lastId = parseInt(posts[posts.length - 1].postid);
                $(".posts").attr("data-last", lastId);

                if(lastId === 1) {
                    $(".btn-more").remove();
                }
            }
        });
}

function insertPost(id, username, text, time, likes) {

    var post =  "<div class='post' id='" + id + "'><div class='container'>" +
                    "<div class='card'>" +
                        "<div class='card-body'>" +
                            "<div class='row'>" +
                                "<div class='post-name col-10'>" + username + "</div>" +
                                "<div class='post-date col-2'>" + time + "</div>" +
                            "</div>" +
                            "<div class='card-text'>" + text + "</div>" +
                            "<div class='row'>" +
                                "<button class='btn btn-primary border rounded col-1 offset-10' type='button' onclick='likePost(5)'>" +
                                    "<span class='far fa-thumbs-up'></span>" +
                                "</button>" +
                            "<div class='likecounter col-1'>+" + likes + "</div>" +
                            "</div>" +
                        "</div>" +
                    "</div>" +
                "</div>";

    $(".posts").append(post);
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