package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textWidth = 0f

    private val animationDuration = 4000L
    private var loading = 0f

    companion object {
        private const val ANIMATION_BUTTON_TEXT = "We are loading"
        private const val BUTTON_TEXT = "Download"
    }

    private var buttonBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var buttonBackgroundRect = RectF()
    private var buttonTextColor = ContextCompat.getColor(context, R.color.white)
    private var defaultButtonText = BUTTON_TEXT
    private var buttonText = defaultButtonText
    private var animatedButtonText = ANIMATION_BUTTON_TEXT
    private var buttonAnimationRect = RectF()
    private var buttonAnimationBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = animationDuration
        interpolator = LinearInterpolator()
        addUpdateListener {
            loading = it.animatedValue as Float
            invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                buttonState = ButtonState.Loading
                this@LoadingButton.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                buttonState = ButtonState.Completed
                this@LoadingButton.isEnabled = true
            }
        })
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                valueAnimator.start()
                invalidate()
            }
            ButtonState.Loading -> {
                buttonText = animatedButtonText
                invalidate()
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                buttonText = defaultButtonText
                invalidate()
            }
        }
    }


    init {

    }

    private val paintButtonBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Paint styles used for rendering are initialized here. This
        // is a performance optimization, since onDraw() is called
        // for every screen refresh.
        style = Paint.Style.FILL
        color = buttonBackgroundColor
    }

    private val paintAnimationButtonBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = buttonAnimationBackgroundColor
    }

    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = buttonTextColor
        textSize = 50f
        typeface = Typeface.DEFAULT
        textAlign = Paint.Align.LEFT
    }
    private fun drawButtonBackground(canvas: Canvas) {
        if (buttonState == ButtonState.Loading) {
            buttonBackgroundRect.set(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
            canvas.drawRect(buttonBackgroundRect, paintButtonBackground)
            buttonAnimationRect.set(0f, 0f, widthSize * loading, heightSize.toFloat())
            canvas.drawRect(buttonAnimationRect, paintAnimationButtonBackground)
        } else {
            buttonBackgroundRect.set(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
            canvas.drawRect(buttonBackgroundRect, paintButtonBackground)
        }
    }
    private fun drawButtonText(canvas: Canvas){
        textWidth = paintButtonText.measureText(buttonText)
        canvas.drawText(
            buttonText,
            (widthSize - textWidth) / 2f,
            (heightSize - (paintButtonText.ascent() + paintButtonText.descent())) / 2f, paintButtonText)
    }

    /** onDraw() method to draw the custom view, using a Canvas object styled by a Paint object.
     * The onDraw() method is called every time the screen refreshes,
     * which can be many times a second
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawButtonBackground(canvas)
        drawButtonText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}