package com.example.navigationlab.data.source

import com.example.navigationlab.data.model.Student

class StudentLocalDataSource {
    private val students = listOf(
        Student(2026, "Alya Putri", "Teknik Informatika", "Universitas Brawijaya"),
        Student(2027, "Bima Pratama", "Teknik Informatika", "Universitas Brawijaya"),
        Student(2028, "Citra Lestari", "Teknik Informatika", "Universitas Brawijaya"),
        Student(2029, "Dinda Hapsari","Sistem Informasi", "Universitas Brawijaya"),
        Student(2030, "Edwin Hartanto", "Sistem Informasi", "Universitas Brawijaya")
    )

    fun getStudentById(id: Int): Student? =
        students.find { it.id == id }

    fun getStudents(): List<Student> = students
}