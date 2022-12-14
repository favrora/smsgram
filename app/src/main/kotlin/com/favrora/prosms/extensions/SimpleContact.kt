package com.favrora.prosms.extensions

import android.text.TextUtils
import com.favrora.commons.models.SimpleContact

fun ArrayList<SimpleContact>.getThreadTitle(): String = TextUtils.join(", ", map { it.name }.toTypedArray()).orEmpty()

fun ArrayList<SimpleContact>.getAddresses() = flatMap { it.phoneNumbers }.map { it.normalizedNumber }
