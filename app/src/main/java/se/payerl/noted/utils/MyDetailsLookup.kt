package se.payerl.noted.utils

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.adapters.view_holders.GeneralVH
import se.payerl.noted.model.NoteBase


internal class MyDetailsLookup(private val mRecyclerView: RecyclerView) : ItemDetailsLookup<String>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        mRecyclerView.findChildViewUnder(e.x, e.y)?.let { view ->
            val holder = mRecyclerView.getChildViewHolder(view)
            return (holder as GeneralVH<out NoteBase>).getItemDetails()
        }
        return null
    }
}