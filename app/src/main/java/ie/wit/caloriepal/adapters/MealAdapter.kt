package ie.wit.caloriepal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.caloriepal.R
import ie.wit.caloriepal.models.MealJSONStore
import ie.wit.caloriepal.models.MealModel
import kotlinx.android.synthetic.main.card_meal.view.*
import org.jetbrains.anko.AnkoLogger

class MealAdapter(
    private var meals: MutableList<MealModel>,
    private val listener: MealListener,
    private val onDeleteClickSerialize: () -> Unit
) : RecyclerView.Adapter<MealAdapter.MainHolder>(), AnkoLogger {

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
        holder.itemView.buttonDeleteMeal.setOnClickListener{
            meals.remove(meal)
            onDeleteClickSerialize()
            notifyItemRemoved(holder.adapterPosition)
        }
    }



    override fun getItemCount(): Int {
        return meals.size
    }

    class MainHolder constructor(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(meal:MealModel, listener: MealListener) {
            itemView.cardMealName.text = meal.title
            itemView.cardCaloricContent.text = meal.caloricContent.toString()
            itemView.setOnClickListener { listener.onMealClick(meal)}
        }
    }
}

interface MealListener {
    fun onMealClick(meal: MealModel)
}