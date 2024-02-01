package com.example.hostapp

class HelperClass {

    var user: String = ""
        private set

    var email: String = ""
        private set

    var password: String = ""
        private set

    var gestore: Boolean = false
        private set

    constructor()
    constructor(user: String, email: String, password: String, gestore: Boolean) {
        this.email = email
        this.user = user
        this.password = password
        this.gestore = gestore
    }
}
