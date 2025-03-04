package com.fsck.k9.backends

import app.k9mail.dev.developmentBackends
import app.k9mail.dev.developmentModuleAdditions
import com.fsck.k9.BuildConfig
import com.fsck.k9.backend.BackendManager
import com.fsck.k9.backend.imap.BackendIdleRefreshManager
import com.fsck.k9.backend.imap.SystemAlarmManager
import com.fsck.k9.mail.oauth.OAuth2TokenProviderFactory
import com.fsck.k9.mail.store.imap.IdleRefreshManager
import org.koin.core.qualifier.named
import org.koin.dsl.module

val backendsModule = module {
    single {
        BackendManager(
            mapOf(
                "imap" to get<ImapBackendFactory>(),
                "pop3" to get<Pop3BackendFactory>(),
            ) + developmentBackends(),
        )
    }
    single {
        ImapBackendFactory(
            accountManager = get(),
            powerManager = get(),
            idleRefreshManager = get(),
            backendStorageFactory = get(),
            trustedSocketFactory = get(),
            context = get(),
            clientIdAppName = get(named("ClientIdAppName")),
            clientIdAppVersion = get(named("ClientIdAppVersion")),
        )
    }
    single<SystemAlarmManager> { AndroidAlarmManager(context = get(), alarmManager = get()) }
    single<IdleRefreshManager> { BackendIdleRefreshManager(alarmManager = get()) }
    single { Pop3BackendFactory(get(), get()) }
    single(named("ClientIdAppName")) { BuildConfig.CLIENT_ID_APP_NAME }
    single(named("ClientIdAppVersion")) { BuildConfig.VERSION_NAME }
    single<OAuth2TokenProviderFactory> { RealOAuth2TokenProviderFactory(context = get()) }

    developmentModuleAdditions()
}
