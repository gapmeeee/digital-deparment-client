package retrofit.Api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit.moduls.Course
import retrofit.moduls.FAQ
import retrofit.moduls.Image
import retrofit2.Response
import retrofit.moduls.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {
    @GET("/api/login")
    suspend fun authorization(@Header("Authorization") token: String): User

    @POST("/api/registration")
    suspend fun registration(@Body user: User):Response<User>

    @GET("/api/user/{user}")
    suspend fun getProfile(@Header("Authorization") token: String, @Body user: User): User

    @GET("/api/courses/")
    suspend fun getCourse(@Header("Authorization") token: String): Course

    @GET("/api/FAQ/get-faq")
    suspend fun getFAQs(@Header("Authorization") token: String): List<FAQ>

    @Multipart
    @POST("/api/avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Response<Void>


    @GET("/api/get-avatar")
    suspend fun getAvatar(
        @Header("Authorization") token: String):ResponseBody
}