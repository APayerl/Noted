package se.payerl.noted.adapters.view_holders

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.NoteBase

abstract class GeneralVH<T>(val gla: GeneralListAdapter, private val itemView: View, val parent: ViewGroup, val longListener: (note: T) -> Unit) : RecyclerView.ViewHolder(itemView) {
    abstract var data: T
    val checkbox: CheckBox = itemView.findViewById<CheckBox>(R.id.general_list_checkbox)
    val trash: ImageView = itemView.findViewById<ImageView>(R.id.item_delete_btn)
    var subLayoutFrame: LinearLayout = itemView.findViewById<LinearLayout>(R.id.general_list_sublayout)
    val selectionOverlay: View = itemView.findViewById<View>(R.id.selection_overlay)
    var itemRow: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.general_list_layout).apply {
        setOnClickListener {
            onFastClick(it)
        }
        setOnLongClickListener {
            longListener(data)
            true
        }
    }
    abstract fun onFastClick(v: View): Boolean

    abstract fun initialize(_d: T)

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> {
        return object : ItemDetailsLookup.ItemDetails<String>() {
            override fun getPosition(): Int {
                return gla.dataList.indexOf(data as NoteBase)
            }

            override fun getSelectionKey(): String {
                return (data as NoteBase).uuid
            }
        }
    }
}