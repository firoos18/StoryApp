package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class PasswordEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var passwordVisibilityButtonImage : Drawable

    private fun init() {
        passwordVisibilityButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                showPasswordVisibilityButton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    error = "Minimal 8 karakter"
                }
            }

            override fun afterTextChanged(s: Editable?) {
//                showPasswordVisibilityButton()
            }

        })
    }

    private fun showPasswordVisibilityButton() {
        setButtonDrawables(endOfTheText = passwordVisibilityButtonImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val visibilityButtonStart: Float
            val visibilityButtonEnd: Float
            var isPasswordVisibilityButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibilityButtonEnd = (passwordVisibilityButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < visibilityButtonEnd -> isPasswordVisibilityButtonClicked = true
                }
            } else {
                visibilityButtonStart = (width - paddingEnd - passwordVisibilityButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > visibilityButtonStart -> isPasswordVisibilityButtonClicked = true
                }
            }
            if (isPasswordVisibilityButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordVisibilityButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_off_24) as Drawable
                        showPasswordVisibilityButton()

                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        passwordVisibilityButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_visibility_24) as Drawable
                        showPasswordVisibilityButton()

                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

    constructor(context: Context) : super(context) {
        init()
    }
    
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Enter password!"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}