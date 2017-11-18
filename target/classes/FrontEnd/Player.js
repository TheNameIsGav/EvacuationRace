window.onload = function() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "http://localhost:4567/checkCookie", true);
        xhr.onload = function (e) {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {

                } else {
                    console.error(xhr.status);
                }
            }
        };
        xhr.onerror = function (e) {
            console.error(xhr.statusText);
        };
        xhr.send(null);
}