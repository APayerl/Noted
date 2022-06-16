package se.payerl.noted.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

class RowAmountVL(itemView: View, src: GeneralListAdapter.GeneralVH, val shortListener: (note: NoteBase) -> Unit, val longListener: (note: NoteBase) -> Unit): ViewLogic(itemView) {
    private val seekbar: SeekBar
    private val content: TextView
    private val dataObj: NoteRowAmount
    val sp: SharedPreferences = src.parent.context.getSharedPreferences("", Context.MODE_PRIVATE)

    init {
        dataObj = src.data as NoteRowAmount
        LayoutInflater.from(src.parent.context).inflate(R.layout.list_item_row_amount, src.parent, false).let { sub ->
            seekbar = sub.findViewById<SeekBar>(R.id.list_item_row_amount_seekbar).apply {
                src.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    progress = if(isChecked) max else min
                }
                dataObj.amountWhenFinished.observeForever {
                    max = it
                }
                min = sp.getInt("number_picker_min_value", 0)

                progress = dataObj.amount
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

    override fun onFastClick(v: View): Boolean {
        shortListener(dataObj)
        return true
    }

    override fun onLongClick(v: View): Boolean {
        longListener(dataObj)
        return true
    }
}