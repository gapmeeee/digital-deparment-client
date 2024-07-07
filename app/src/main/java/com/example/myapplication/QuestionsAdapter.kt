//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.recyclerview.widget.RecyclerView
//import com.example.myapplication.R
//
//class QuestionsAdapter(private val questions: List<String>, private val onClickListener: (String) -> Unit) :
//    RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.questions_item, parent, false)
//        return QuestionViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
//        holder.bind(questions[position], onClickListener)
//    }
//
//    override fun getItemCount(): Int = questions.size
//
//    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val questionButton: Button = itemView.findViewById(R.id.buttonQuestion)
//
//        fun bind(question: String, onClickListener: (String) -> Unit) {
//            questionButton.text = question
//            questionButton.setOnClickListener { onClickListener(question) }
//        }
//    }
//}