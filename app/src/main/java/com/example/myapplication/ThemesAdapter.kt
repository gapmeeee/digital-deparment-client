import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ThemesAdapter(private val themes: List<String>, private val onClickListener: (String) -> Unit) :
    RecyclerView.Adapter<ThemesAdapter.ThemeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.theme_item, parent, false)
        return ThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]
        holder.bind(theme)
        holder.itemView.setOnClickListener { onClickListener(theme) }
    }

    override fun getItemCount(): Int = themes.size

    class ThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val themeText: TextView = itemView.findViewById(R.id.TextEditTheme)

        fun bind(theme: String) {
            themeText.text = theme
        }
    }
}