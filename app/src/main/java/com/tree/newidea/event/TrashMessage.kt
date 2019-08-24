package com.tree.newidea.event

import com.tree.newidea.bean.NotepadBean
import java.text.FieldPosition

data class TrashMessage (val bean: NotepadBean.DatesBean.TextsBean?,val position: Int)