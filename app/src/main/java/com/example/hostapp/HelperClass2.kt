package com.example.hostapp

class HelperClass2 {

    var attivita: String = ""
        private set

    var telefono: String = ""
        private set

    constructor()
    constructor(attivita: String, telefono: String) {
        this.attivita = attivita
        this.telefono = telefono
    }
}
