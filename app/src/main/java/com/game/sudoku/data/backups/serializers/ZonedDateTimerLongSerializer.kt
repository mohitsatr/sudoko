package com.game.sudoku.data.backups.serializers

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class ZonedDateTimerLongSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("dateTime", PrimitiveKind.LONG)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(
        encoder: Encoder,
        value: ZonedDateTime,
    ) {
        encoder.encodeLong(value.toEpochSecond())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(decoder.decodeLong()),
            ZoneOffset.systemDefault()
        )
    }
}
