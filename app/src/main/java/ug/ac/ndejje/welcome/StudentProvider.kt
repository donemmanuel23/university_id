package ug.ac.ndejje.welcome

class StudentProvider {
    companion object {
        val studentList = listOf(
            Student(1, "Akello Stellamaris", "24/2/314/01", "BIT", R.drawable.female_1, null,  true),
            Student(2, "Ademun Emmanuel",       "24/2/314/02", "BIT", R.drawable.male_1,   null,  false),
            Student(3, "Mbabazi Joan",      "24/2/314/03", "BIT", R.drawable.female_2, null, true),
            Student(4, "Ebwaku James",     "24/2/314/04", "BSE", R.drawable.male_2,   null,  true),
            Student(5, "Nakato Mary", "24/2/314/05", "BIT", R.drawable.female_3, null, true),
            Student(6, "Otim Peter", "24/2/314/06", "BCS", R.drawable.male_3, null, false),
            Student(7, "Auma Sarah", "24/2/314/07", "BIT", R.drawable.female_4, null, true),
            Student(8, "Mugisha David", "24/2/314/08", "BSE", R.drawable.male_4, null, true),
            Student(9, "Nabirye Grace", "24/2/314/09", "BCS", R.drawable.female_student, null, false)
        )
    }
}
