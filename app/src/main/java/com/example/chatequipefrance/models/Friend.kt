package com.example.chatequipefrance.models


data class Friend(
    var uuid: String,
    val name: String,
    val lastMsg: String,
    val image: String,
    val timestamp: Long,
) {
    constructor() : this("", "", "", "", 0)
}