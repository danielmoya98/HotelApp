package com.example.hotelapp.utils

object UserSession {
    var userId: Int? = null
    var userName: String? = null
    var userEmail: String? = null
    var userTel: String? = null
    var userPass: String? = null
    var userRole: String? = null
    var userPhotoUrl: String? = null

    // Método para limpiar la sesión
    fun clearSession() {
        userId = null
        userName = null
        userEmail = null
        userTel = null
        userPass = null
        userRole = null
        userPhotoUrl = null
    }
}
