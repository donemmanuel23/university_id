package ug.ac.ndejje.welcome

data class Student(
    val id: Int,
    val name: String,
    val regNumber: String,
    val programme: String,
    val profileImageId: Int, // Resource ID (e.g., R.drawable.female_student)
    val isVerified: Boolean = false
)
