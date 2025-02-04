package io.phone.build.sdk.voipsdkandroid.contacts

import android.net.Uri

data class SupplementaryContact(val number: String, val name: String, val imageUri: Uri? = null)

fun SupplementaryContact.toContact() = Contact(name = name, imageUri = imageUri)