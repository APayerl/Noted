package se.payerl.noted.adapters.view_holders

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.*
import se.payerl.noted.model.db.Mapper

class NoteAmountVH(gla: GeneralListAdapter, itemView: View, parent: ViewGroup, val shortListener: (note: NoteRowAmount) -> Boolean, longListener: (note: NoteRowAmount) -> Unit
): GeneralVH<NoteRowAmount>(gla, itemView, parent, longListener) {
    private val seekbarListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = progress == seekBar.max
            checkbox.setOnCheckedChangeListener(checkboxChangeListener)
            data.amount = seekBar.progress
            gla.db.queryExecutor.execute {
                gla.db.rowAmountDao().update(Mapper().noteRowAmountToNoteRowAmountEntity(data))
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
    override val checkboxChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        data.amount = 0
        seekbar.setOnSeekBarChangeListener(null)
        seekbar.progress = seekbar.min
        seekbar.setOnSeekBarChangeListener(seekbarListener)
        gla.db.queryExecutor.execute {
            gla.db.rowAmountDao().update(Mapper().noteRowAmountToNoteRowAmountEntity(data))
        }
    }
    override lateinit var data: NoteRowAmount
    private val seekbar: SeekBar
    private val content: TextView
    val sp: SharedPreferences = parent.context.getSharedPreferences("", Context.MODE_PRIVATE)

    init {
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_row_amount, parent, false).let { sub ->
            seekbar = sub.findViewById<SeekBar>(R.id.list_item_row_amount_seekbar)
            content = sub.findViewById<TextView>(R.id.list_item_row_amount_content)
            subLayoutFrame.addView(sub)
        }
    }

    override fun initialize(_d: NoteRowAmount) {
        data = _d

        data.selected.observeForever { isSelected ->
            selectionOverlay.visibility = if(isSelected) View.VISIBLE else View.GONE
        }

        seekbar.apply {
            gla.isDeletable.observeForever {
                trash.visibility = if(it) ImageView.VISIBLE else ImageView.GONE
            }
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                progress = if(isChecked) max else min
            }
            data.amountWhenFinished.observeForever {
                max = it
            }
            min = sp.getInt("number_picker_min_value", 0)

            progress = data.amount

            setOnSeekBarChangeListener(seekbarListener)
        }
        content.apply {
            text = data.content
        }
    }

    override fun onFastClick(v: View): Boolean {
        return shortListener(data)
    }
}