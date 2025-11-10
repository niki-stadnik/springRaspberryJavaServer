var stompClient = null;

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    if (reloading) {
        sessionStorage.removeItem("reloading");
    }
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/imageDoorCam', function(message) {
            showImageV2(JSON.parse(message.body));
        });
    });
}

function showImage(image) {
    try {
        const base64 = image.image; // the base64 JPEG string from your message

        // Convert Base64 → Blob
        const byteChars = atob(base64);
        const byteArrays = [];
        for (let offset = 0; offset < byteChars.length; offset += 512) {
            const slice = byteChars.slice(offset, offset + 512);
            const byteNumbers = new Array(slice.length);
            for (let i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }
            byteArrays.push(new Uint8Array(byteNumbers));
        }
        const blob = new Blob(byteArrays, { type: 'image/jpeg' });

        // Create an object URL and display it
        const url = URL.createObjectURL(blob);
        const img = document.getElementById('image');

        // Release the old image data once the new one has loaded
        img.onload = () => {
            URL.revokeObjectURL(url);
        };

        img.src = url;

    } catch (err) {
        console.error("Error displaying image:", err);
    }
}

function showImageV2(image) {
    var dd = image.image;
    const imageData = document.getElementById('image');
    imageData.src = 'data:image/jpeg;base64,' + dd;
}