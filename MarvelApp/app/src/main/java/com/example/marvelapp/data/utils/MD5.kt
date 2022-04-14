package com.example.marvelapp.data.utils

import java.math.BigInteger
import java.security.MessageDigest

class MD5 {
    operator fun invoke(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
