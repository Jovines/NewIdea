package com.tree.newidea.data

import android.provider.BaseColumns

object NotepadEntry : BaseColumns {
    const val ID = BaseColumns._ID
    const val TABLE_NAME = "note"
    const val TILTE = "tilte"
    const val TEXT = "text"
    const val IS_MARKDOWN = "ismarkdown"
    const val IS_UNDO = "isundo"
    const val DATE = "date"

}

