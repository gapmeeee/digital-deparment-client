package retrofit.moduls

data class Course (

    val id: Long?=null,
    val title: String?=null,
    val description: String,
    val users: List<User>,
    val lectures: List<Lecture>

)