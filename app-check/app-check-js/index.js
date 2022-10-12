const functions = require("firebase-functions");
const firebaseAdmin = require("firebase-admin");

firebaseAdmin.initializeApp();

exports.createToken = functions.region("europe-west1").https.onRequest((req, res) => {
    firebaseAdmin.appCheck().createToken(req.query.appId)
        .then((token) => { return res.send({ expiresAt: Math.floor(Date.now() / 1000) + 60 * 60, token: token }) })
        .catch((err) => { res.status(401).send(err.message) })
});

exports.verifyToken = functions.region("europe-west1").https.onRequest((req, res) => {
    const token = req.header("X-Firebase-AppCheck")

    firebaseAdmin.appCheck().verifyToken(token)
        .then((tokenRes) => { return res.status(200).send(tokenRes.token) })
        .catch((err) => { res.status(401).send(err.message) })
});
