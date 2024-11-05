package com.temple.aldwairi_projects_emotionecho

class User (
    val userID: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null
    ) {

        @Override
        override fun toString(): String = userID + "\n" + firstName + "\n" + lastName + "\n" + email

        companion object{
            public val USER_TABLE = "Users"
            public val USERID = "userID"
            public val FIRST_NAME = "firstName"
            public val LAST_NAME = "lastName"
            public val EMAIL_ADDRESS = "email"
        }
    }