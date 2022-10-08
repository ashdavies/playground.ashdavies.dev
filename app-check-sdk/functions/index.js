const functions = require('firebase-functions');
const firebaseAdmin = require('firebase-admin');
firebaseAdmin.initializeApp();

exports.verifyAppCheckToken = functions.region('asia-northeast1').https.onRequest((req, res) => {
    const token = req.header('X-Firebase-AppCheck')
    firebaseAdmin.appCheck().verifyToken(token)
        .then(function (tokenRes) {
            console.log("verify", tokenRes)
            return res.sendStatus(200);
        })
        .catch(function (err) {
            console.error(err);
            return res.sendStatus(401);
        })
});

const appId = 'YOUR_FIREBASE_APP_ID';
exports.fetchAppCheckToken = functions.region('asia-northeast1').https.onRequest((req, res) => {
    firebaseAdmin.appCheck().createToken(appId)
        .then(function (token) {
            return res.send({
                token: token,
                expiresAt: Math.floor(Date.now() / 1000) + 60 * 60,
            })
        })
        .catch(function (err) {
            console.error(err);
            return res.sendStatus(500);
        });
});
