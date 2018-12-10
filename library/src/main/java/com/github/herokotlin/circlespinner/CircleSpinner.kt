package com.github.herokotlin.circlespinner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class CircleSpinner : View {

    companion object {

        var DEFAULT_VALUE = 0f

        var DEFAULT_COLOR = Color.parseColor("#BBFFFFFF")

        var DEFAULT_RADIUS = 34

        var DEFAULT_STROKE_GAP = 2

        var DEFAULT_STROKE_WIDTH = 2

    }

    /**
     * 当前值
     *
     * 可选值为 0 - 1
     */
    var value = DEFAULT_VALUE

        set(value) {
            field = when {
                value < 0 -> 0f
                value > 1 -> 1f
                else -> value
            }
        }

    /**
     * 圆形的颜色
     */
    var color = DEFAULT_COLOR

    /**
     * 圆形的半径
     */
    var radius = DEFAULT_RADIUS

    /**
     * 圆环和扇形之间的距离
     */
    var strokeGap = DEFAULT_STROKE_GAP

    /**
     * 圆环的粗细
     */
    var strokeWidth = DEFAULT_STROKE_WIDTH

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.CircleSpinner, defStyle, 0)

        value = typedArray.getFloat(R.styleable.CircleSpinner_circle_spinner_value, DEFAULT_VALUE)

        color = typedArray.getColor(R.styleable.CircleSpinner_circle_spinner_color, DEFAULT_COLOR)

        radius = typedArray.getDimensionPixelSize(
            R.styleable.CircleSpinner_circle_spinner_radius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS.toFloat(), resources.displayMetrics).toInt()
        )

        strokeGap = typedArray.getDimensionPixelSize(
            R.styleable.CircleSpinner_circle_spinner_stroke_gap,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STROKE_GAP.toFloat(), resources.displayMetrics).toInt()
        )

        strokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.CircleSpinner_circle_spinner_stroke_width,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STROKE_WIDTH.toFloat(), resources.displayMetrics).toInt()
        )

        // 获取完 TypedArray 的值后，
        // 一般要调用 recycle 方法来避免重新创建的时候出错
        typedArray.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = widthSize
        var height = heightSize

        if (widthMode != MeasureSpec.EXACTLY) {
            width = radius * 2
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = radius * 2
        }

        setMeasuredDimension(width, height)

    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = strokeWidth.toFloat()

        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2
        val centerRadius = width / 2 - strokeWidth

        canvas.drawCircle(centerX, centerY, centerRadius.toFloat(), paint)

        paint.style = Paint.Style.FILL

        rect.left = centerX - centerRadius + strokeGap
        rect.top = centerY - centerRadius + strokeGap
        rect.right = centerX + centerRadius - strokeGap
        rect.bottom = centerY + centerRadius - strokeGap

        canvas.drawArc(rect, -90f, value * 360, true, paint)

    }
}
