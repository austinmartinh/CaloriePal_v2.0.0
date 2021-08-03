package ie.wit.caloriepal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.caloriepal.R
import ie.wit.caloriepal.models.MealModel
import kotlinx.android.synthetic.main.card_meal.view.*

class MealAdapter constructor(
    private var meals: List<MealModel>,
    private val listener: MealListener
) : RecyclerView.Adapter<MealAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_meal,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealAdapter.MainHolder, position: Int) {
        val meal = meals[holder.adapterPosition]
        holder.bind(meal, listener)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    class MainHolder constructor(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(meal:MealModel, listener: MealListener) {
            itemView.cardMealName.text = meal.title
            itemView.cardCaloricContent.text = meal.caloricContent.toString()
        }
    }

}

interface MealListener {
    fun onMealClick(meal: MealModel)
}