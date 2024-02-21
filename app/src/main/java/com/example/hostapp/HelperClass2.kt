package com.example.hostapp

class HelperClass2 {    // classe di appoggio per registrare i dati del Gestore
    var user: String = ""
        private set

    var email: String = ""
        private set

    var password: String = ""
        private set

    var attivita: String = ""
        private set

    var telefono: String = ""
        private set

    var tipo: String = ""
        private set

    constructor()
    constructor(user: String, email: String, password: String, attivita: String,telefono: String, tipo: String) {
        this.email = email
        this.user = user
        this.password = password
        this.attivita = attivita
        this.telefono = telefono
        this.tipo = tipo
    }
}
