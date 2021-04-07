package com.cwod.trasset.home.provider.model

/*{
  "data": {
    "phone": 7891167898,
    "address": "White House, RG Baruah Rd, Chandmari, Guwahati, Assam 781003",
    "about": "I am a person who is positive about every aspect of life. There are many things I like to do, to see, and to experience. I like to read, I like to write; I like to think, I like to dream; I like to talk, I like to listen.",
    "role": "Site Admin",
    "isMale": true,
    "_id": "6062df594c319337dcc4fa1b",
    "name": "Perul Jain ",
    "email": "perul365@gmail.com",
    "password": "$2b$10$fOBzq.1qQgjZufhfrdGZj.H113Bxp0EOUMzukHHrWAvw5DSQIzVum",
    "__v": 0
  },
  "error": {}
}*/

data class ProfileModel(
    val name: String,
    val email: String,
    val address: String,
    val phone: Long,
    val about: String,
    val role: String,
    val _id: String,
    val isMale: Boolean
)

