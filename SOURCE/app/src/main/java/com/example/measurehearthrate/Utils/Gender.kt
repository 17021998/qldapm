package com.example.measurehearthrate.Utils

enum class Gender constructor(val gender: String) {
    MALE("F"),
    FEMALE("M"),
    OTHER("O");

    override fun toString(): String {
        return gender
    }

    companion object {
        fun getObjectGender(value: String) : Gender? {
            when(value) {
                "F" -> return MALE
                "M" -> return FEMALE
                "O" -> return OTHER
                else -> return null
            }
        }
    }


}