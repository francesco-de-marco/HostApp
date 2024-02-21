package com.example.hostapp

class HelperClass {     //Classe di appoggio per registrare i dati dello Sportivo

    var user: String = ""
        private set

    var email: String = ""
        private set

    var password: String = ""
        private set

    var tipo: String = ""
        private set

    constructor()
    constructor(user: String, email: String, password: String, tipo: String) {
        this.email = email
        this.user = user
        this.password = password
        this.tipo = tipo
    }
}
