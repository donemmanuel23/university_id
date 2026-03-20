package ug.ac.ndejje.welcome

class StudentProvider {
    companion object {
        val studentList = listOf(
            Student(1, "Akello Stellamaris", "24/2/314/01", "BIT", R.drawable.female_1,   true),
            Student(2, "Kirya James",       "24/2/314/02", "BCS", R.drawable.male_1,     false),
            Student(3, "Mbabazi Joan",      "24/2/314/03", "BIT", R.drawable.female_2, true),
            Student(4, "Kato Johnmary",     "24/2/314/04", "BSE", R.drawable.male_2,     true)
        )
    }
}