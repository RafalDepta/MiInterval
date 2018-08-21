package pl.depta.rafal.miinterval.utils

import java.nio.charset.StandardCharsets

class AppUtils {
    companion object {

        @JvmStatic
        fun fromUint8(value: Int): Byte {
            return (value and 0xff).toByte()

        }

        @JvmStatic
        fun fromUint8s(message: String): ByteArray {
            return message.toByteArray(StandardCharsets.UTF_8)

        }
    }

}