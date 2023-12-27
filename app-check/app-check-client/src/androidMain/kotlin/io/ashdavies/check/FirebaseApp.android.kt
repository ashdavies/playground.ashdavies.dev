package io.ashdavies.check

import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck

public val FirebaseApp.appCheck: FirebaseAppCheck
    get() = FirebaseAppCheck.getInstance(this)
