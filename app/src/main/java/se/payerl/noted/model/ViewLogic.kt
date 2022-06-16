package se.payerl.noted.model

import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

abstract class ViewLogic(private val itemView: View) {
    var subLayoutFrame: LinearLayout = itemView.findViewById<LinearLayout>(R.id.general_list_sublayout)
    var itemRow: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.general_list_layout)

    init {
        itemRow.setOnClickListener {
            onFastClick(it)
        }
        itemRow.setOnLongClickListener {
            onLongClick(it)
            true
        }
    }

    abstract fun onFastClick(v: View): Boolean
    abstract fun onLongClick(v: View): Boolean
}