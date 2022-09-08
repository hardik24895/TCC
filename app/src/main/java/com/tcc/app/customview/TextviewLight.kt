package com.tcc.app.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView


@SuppressLint("AppCompatCustomView")
class TextviewLight : TextView {
    constructor(context: Context) : super(context) {
        val face = Typeface.createFromAsset(context.assets, "font/opensans_light.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val face = Typeface.createFromAsset(context.assets, "font/opensans_light.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val face = Typeface.createFromAsset(context.assets, "font/opensans_light.ttf")
        this.typeface = face
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}

