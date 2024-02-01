package com.example.hostapp

class HelperClass {

    var user: String = ""
        private set

    var email: String = ""
        private set

    var password: String = ""
        private set

    var tipologia: String = ""
        private set

    constructor()
    constructor(user: String, email: String, password: String, tipologia: String) {
        this.email = email
        this.user = user
        this.password = password
        this.tipologia = tipologia
    }
}
