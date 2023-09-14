package com.cnc.tictac.backend.database

import com.cnc.tictac.backend.system.HumanPlayer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlayerSerializer::class)
object PlayerSerializer : KSerializer<HumanPlayer> {
    override val descriptor : SerialDescriptor = PrimitiveSerialDescriptor("Player", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value : HumanPlayer){
        val name        : String = value.playerName
        val id          : String = value.playerID.toString()
        val avatar_s    : String = value.playerAvatar.second
        val avatar_r    : String = value.playerAvatar.first.toString()
        val icon_s      : String = value.playerIcon.second
        val icon_r      : String = value.playerIcon.first.toString()
        val serial_string = "$name#$id#$avatar_s#$avatar_r#$icon_s#$icon_r"
        encoder.encodeString(serial_string)
    }
    override fun deserialize(decoder : Decoder) : HumanPlayer {
        val string = decoder.decodeString()
        val arr = string.split("#")
        var player = HumanPlayer(
            playerName  = arr[0],
            playerID    = arr[1].toInt(),
            playerAvatar = Pair(arr[3], arr[2]),
            playerIcon = Pair(arr[5], arr[4])
        )
        return player
    }
}