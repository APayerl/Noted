package se.payerl.noted.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

class RowAmountVL(itemView: View, src: GeneralListAdapter.GeneralVH, gla: GeneralListAdapter): ViewLogic(itemView, src, gla) {
    private val seekbar: SeekBar
    private val content: TextView

    init {
        val dataObj = src.data as NoteRowAmount
        LayoutInflater.from(src.parent.context).inflate(R.layout.list_item_row_amount, src.parent, false).let { sub ->
            seekbar = sub.findViewById<SeekBar>(R.id.list_item_row_amount_seekbar).apply {
                max = dataObj.amount
                min = 0
                progress = dataObj.amountDone
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        Log.w("SeekBar", "${dataObj.uuid}: $progress")
                        src.checkbox.isChecked = progress == max
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                })
            }
            content = sub.findViewById<TextView>(R.id.list_item_row_amount_content).apply {
                text = dataObj.content
            }
            subLayoutFrame.addView(sub)
        }
    }
}