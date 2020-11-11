package com.library.base.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable

open class QMUIRoundConstraintLayout : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attribute: AttributeSet) : super(context, attribute) {
        init(context, attribute, 0)
    }

    constructor(context: Context, attribute: AttributeSet, defStyleAttr: Int) : super(context, attribute, defStyleAttr) {
        init(context, attribute, defStyleAttr)
    }

    fun init(context: Context, attribute: AttributeSet?, defStyleAttr: Int) {
        val fromAttributeSet = QMUIRoundButtonDrawable.fromAttributeSet(context, attribute, defStyleAttr)
        QMUIViewHelper.setBackgroundKeepingPadding(this, fromAttributeSet)
    }

}