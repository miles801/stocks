/**
 * 发送桌面通知
 * @param title 标题
 * @param options 配置项
 * @param callback 回调
 */
function notify(title, options, callback) {
    // Let's check if the browser supports notifications
    if (!("Notification" in window)) {
        alert("This browser does not support desktop notification");
    } else if (Notification.permission === "granted") {
        // Let's check if the user is okay to get some notification
        // If it's okay let's create a notification
        return new Notification(title, options);
    } else if (Notification.permission !== 'denied') {
        // Otherwise, we need to ask the user for permission
        return Notification.requestPermission(function (permission) {
            // If the user is okay, let's create a notification
            if (permission === "granted") {
                var notification = new Notification(title, options);
            }
        });
    }
    // At last, if the user already denied any notification, and you
    // want to be respectful there is no need to bother them any more.
}