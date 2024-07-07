package retrofit.moduls

data class FAQ (
    val id: Long?=null,
    val themes: String,
    val faqQuestions: List<FAQquestion>,
)
{
    data class FAQquestion (
        val id: Long?=null,
        val questions: String,
        val answers: String
    )
}