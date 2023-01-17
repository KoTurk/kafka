package nl.adesso.streaming

import example.avro.Message

data class JoinedMessage(val transaction: Message, val balance: Message) {
    
}
