package com.example.hostapp

class OrarioClass {     // classe di appoggio per salvare le prenotazioni su database
    var nome: String = ""
        private set
    var giorno: String = ""
        private set
    var ora: String = ""
        private set
    constructor()
    constructor(nome: String,giorno: String, ora: String) {
        this.nome = nome
        this.giorno = giorno
        this.ora = ora
    }
}