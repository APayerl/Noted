package se.payerl.noted.model

import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

abstract class ViewLogic(itemView: View, src: GeneralListAdapter.GeneralVH, gla: GeneralListAdapter) {
    var subLayoutFrame: LinearLayout
    var itemRow: ConstraintLayout

    init {
        subLayoutFrame = itemView.findViewById<LinearLayout>(R.id.general_list_sublayout)
        itemRow = itemView.findViewById<ConstraintLayout>(R.id.general_list_layout)
        itemRow.setOnClickListener {
            gla.listener(src.data)
        }
    }
}