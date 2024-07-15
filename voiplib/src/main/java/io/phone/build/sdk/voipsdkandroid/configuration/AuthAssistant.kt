package io.phone.build.sdk.voipsdkandroid.configuration

data class AuthAssistant (
    val accessToken: String,
    val licencesKey: String,
) {
    val isValid: Boolean
    get() = accessToken.isNotBlank() && licencesKey.isNotBlank()
}