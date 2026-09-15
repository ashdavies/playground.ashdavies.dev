window
    .getAppCheckToken(false)
    .then(function(res) {
        return fetch('/', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token: res.token })
        });
    })
    .then(function(res) {
        return fetch('/shutdown', { method: 'GET' });
    })
    .then(function() { window.close() })
    .catch(console.error);
