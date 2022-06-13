package se.payerl.noted.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import se.payerl.noted.R

class RowAddDialog {
    companion object {
        fun get(
            context: Context,
            title: String,
            itemList: Array<String>,
            default: Int,
            onNeutral: (() -> Unit)? = null,
            onCancel: () -> Unit = {},
            onChosen: (chosen: Int) -> Unit) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setSingleChoiceItems(itemList, default) { _,_ -> false }

            onNeutral?.let { neu ->
                alertDialogBuilder.setNeutralButton("") { _, _ -> neu() }
                alertDialogBuilder.setNeutralButtonIcon(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_help_outline_black_24, null))
            }
            alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> onCancel() }
            alertDialogBuilder.setPositiveButton("Select") { dialog, which ->
                (dialog as AlertDialog).listView.checkedItemPosition.let { pos ->
                    onChosen(pos)
                }
                true
            }

            alertDialogBuilder.create().show()
        }
    }
}