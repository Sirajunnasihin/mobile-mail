package app.k9mail.feature.account.setup.domain.entity

import kotlinx.collections.immutable.toImmutableList

enum class OutgoingProtocolType(
    val defaultName: String,
    val defaultConnectionSecurity: ConnectionSecurity,
) {
    SMTP("smtp", ConnectionSecurity.TLS),
    ;

    companion object {
        val DEFAULT = SMTP

        fun all() = values().toList().toImmutableList()
    }
}

fun OutgoingProtocolType.toDefaultPort(connectionSecurity: ConnectionSecurity): Long {
    return when (this) {
        OutgoingProtocolType.SMTP -> connectionSecurity.toSmtpDefaultPort()
    }
}
