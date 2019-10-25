package com.example.measurehearthrate.Utils

enum class Gender constructor(val gender: String) {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    override fun toString(): String {
        return gender
    }

    companion object {
        fun getObjectGender(value: String) : Gender? {
            when(value) {
                "M" -> return MALE
                "F" -> return FEMALE
                "O" -> return OTHER
                else -> return null
            }
        }
    }


}