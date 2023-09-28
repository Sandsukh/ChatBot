package com.example.mychatbot

class MessageModal (private var message: String, private var sender: String) {
    fun getMessage(): String {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getSender(): String {
        return sender
    }

    fun setSender(sender: String) {
        this.sender = sender
    }
}