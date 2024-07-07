package retrofit.moduls

import com.google.android.material.textfield.TextInputLayout.LengthCounter

data class User (
    val id: Long?=null,
    val email: String,
    val phoneNumber: String,
    val first_name:String,
    val last_name:String,
    val father_name:String,
    val active: Boolean?=null,
    val admin: Boolean?=null,
    val password:String,
    val studentId:String,
    val group:String,
    val birthday :String,
    val course :Course?=null
)