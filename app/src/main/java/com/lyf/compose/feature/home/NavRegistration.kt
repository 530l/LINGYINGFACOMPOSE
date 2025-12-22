package com.lyf.compose.feature.home

// Nav registration for home previously lived here. Routing registration is now centralized
// in `com.lyf.compose.router.RouterRegistrations`. Keep this file as a noop placeholder
// to avoid accidental usage or compilation issues during transition.

@Deprecated("Use com.lyf.compose.router.RouterRegistrations.registerAll() instead")
object HomeNavRegistration {
    fun registerNav() {
        // no-op: registrations centralized in router/RouterRegistrations
    }
}
