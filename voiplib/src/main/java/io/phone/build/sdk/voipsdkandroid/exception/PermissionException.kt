package io.phone.build.sdk.voipsdkandroid.exception

class PermissionException internal constructor(missingPermission: String) : Exception(
    "Missing required permission: $missingPermission"
)
