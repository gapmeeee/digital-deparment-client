package retrofit.moduls

data class Lecture (

    val id: Long?=null,
    val numberOfLectures: Int,
    val comment: String,
    val course: Course,
    val name: String,
    val dateOfCreated:String

)