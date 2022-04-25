package com.parsanatech.whatsapp.Models

class messageModel {

    var id:String?=null
    var messageId:String?=null
    var message:String?=null
    var time:Long?=null

    constructor(){

    }

    constructor(id: String?, message: String?, time: Long?) {
        this.id = id
        this.message = message
        this.time = time
    }

    constructor(id: String?, message: String?) {
        this.id = id
        this.message = message
    }


}