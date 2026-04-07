package ug.ac.ndejje.welcome

import android.net.Uri

data class Student(
    val id: Int,
    val name: String,
    val regNumber: String,
    val programme: String,
    val profileImageId: Int? = null, // For pre-defined resources
    val profileImageUri: Uri? = null, // For uploaded images
    val isVerified: Boolean = false
)
