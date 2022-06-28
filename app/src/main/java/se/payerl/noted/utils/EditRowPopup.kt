package se.payerl.noted.utils

import android.content.Context
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import se.payerl.noted.R
import se.payerl.noted.model.*

class EditRowPopup {
    companion object {
        fun open(
            context: Context,
            base: NoteBase,
            onSave: (base: NoteBase) -> Unit = {}) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setTitle("Edit")
            val popupView = View.inflate(context, R.layout.edit_popup_layout, null).apply {
                findViewById<LinearLayout>(R.id.item_uuid_fields).apply {
                    (children.elementAt(0) as TextView).apply {
                        text = "UUID"
                        isEnabled = false
                    }
                    (children.elementAt(1) as EditText).apply {
                        setText(base.uuid)
                        isEnabled = false
                    }
                }
                findViewById<LinearLayout>(R.id.item_type_fields).apply {
                    (children.elementAt(0) as TextView).apply {
                        text = "TYPE"
                        isEnabled = false
                    }
                    (children.elementAt(1) as EditText).apply {
                        setText(base.type.toString())
                        isEnabled = false
                    }
                }
            }

            when(base.type) {
                NoteType.LIST -> popupView.findViewById<LinearLayout>(R.id.first).apply {
                    (children.elementAt(0) as TextView).apply {
                        visibility = View.GONE
                    }
                    (children.elementAt(1) as EditText).apply {
                        setText((base as Note).name)
                        addTextChangedListener {
                            (base as Note).name = it.toString()
                        }
                        isEnabled = true
                    }
                }
                NoteType.ROW_AMOUNT -> {
                    popupView.findViewById<LinearLayout>(R.id.first).apply {
                        (children.elementAt(0) as TextView).apply {
                            visibility = View.GONE
                        }
                        (children.elementAt(1) as EditText).apply {
                            setText((base as NoteRowAmount).content)
                            addTextChangedListener {
                                (base as NoteRowAmount).content = it.toString()
                            }
                            isEnabled = true
                        }
                    }
                    popupView.findViewById<NumberPicker>(R.id.numberPicker).apply {
                        val sp = context.getSharedPreferences("", Context.MODE_PRIVATE)
                        visibility = View.VISIBLE
                        value = (base as NoteRowAmount).amountWhenFinished.value!!
                        setOnValueChangedListener { picker, oldVal, newVal ->
                            (base as NoteRowAmount).amountWhenFinished.postValue(newVal)
                        }
                        minValue = sp.getInt("number_picker_min_value", 1)
                        maxValue = sp.getInt("number_picker_max_value", 10)
                    }
                }
                NoteType.ROW_TEXT -> {
                    popupView.findViewById<LinearLayout>(R.id.first).apply {
                        (children.elementAt(0) as TextView).apply {
                            visibility = View.GONE
                        }
                        (children.elementAt(1) as EditText).apply {
                            setText((base as NoteRowText).content)
                            isEnabled = true
                            addTextChangedListener {
                                (base as NoteRowText).content = it.toString()
                            }
                        }
                    }
                }
            }

            alertDialogBuilder.setView(popupView)
//            alertDialogBuilder.setView(R.layout.edit_popup_layout)

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->dialog.cancel() }
            alertDialogBuilder.setPositiveButton("Select") { dialog, which ->
                onSave(base)
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}