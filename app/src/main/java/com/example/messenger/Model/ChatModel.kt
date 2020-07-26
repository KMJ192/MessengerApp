package com.example.messenger.Model

class ChatModel(val myUid: String?, val othersUid: String?, val message: String, val time: Long, val who : String) {
    constructor() : this("", "", "", 0, "")
}