package com.rcmiku.payload.dumper.compose.utils

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import com.google.protobuf.ByteString
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File

object MiscUtil {

    fun String.isValidUrl(): Boolean {
        return this.toHttpUrlOrNull() != null
    }

    fun ByteString.toHexString(): String {
        return joinToString("") { "%02x".format(it.toInt() and 0xFF) }
    }

    fun ByteArray.toLong(): Long {
        var result = 0L
        for (i in this.indices) {
            result = result shl 8
            result = result or (this[i].toLong() and 0xFF)
        }
        return result
    }

    fun ByteArray.toInt(): Int {
        return this.toLong().toInt()
    }

    fun ByteArray.toBufferedInputStream(): BufferedInputStream {
        val byteArrayInputStream = ByteArrayInputStream(this)
        return BufferedInputStream(byteArrayInputStream)
    }

    fun Long.sizeIn(): String {
        return when {
            this < 1000 -> "%d B".format(this)
            this < 1000 * 1000 -> "%d KB".format(this / 1024)
            this < 1000 * 1000 * 1000 -> "%d MB".format(this / (1024 * 1024))
            else -> "%.2f GB".format(this / (1024.0 * 1024 * 1024))
        }
    }

    fun String.isReadable(): Boolean {
        val file = File(this)
        return file.exists() && file.canRead()
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":")
            if (split.size == 2) {
                val type = split[0]
                val path = split[1]
                if ("primary" == type) {
                    return "/storage/emulated/0/$path"
                }
            }
        }
        return null
    }
}