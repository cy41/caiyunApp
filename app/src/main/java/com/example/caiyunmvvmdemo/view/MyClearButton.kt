package com.example.caiyunmvvmdemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.caiyunmvvmdemo.R
import kotlin.math.min

/**
 * 主要功能：绘制一个清除按钮；
 * 支持修改按钮背景颜色，交叉线的颜色，四个内边距。
 * @author chen yu
 * @since 2020/07/01
 */
class MyClearButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle){

    companion object {
        // 每条交叉线所占按钮height的比例
        const val CROSS_THICKNESS_RATE = 0.1f
        // 画线时，画布需要旋转的角度
        const val QUARTER_CIRCLE = 45f
    }

    private var buttonBackgroundPaint: Paint = Paint()
    private var buttonLinePaint: Paint = Paint()

    init {
        buttonBackgroundPaint.style = Paint.Style.FILL
        buttonBackgroundPaint.isAntiAlias = true

        buttonLinePaint.style = Paint.Style.STROKE
        buttonLinePaint.strokeCap = Paint.Cap.ROUND
        buttonLinePaint.isAntiAlias = true

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClearButton)
        setButtonBackgroundColor(ta.getColor(R.styleable.ClearButton_buttonColor,
            ContextCompat.getColor(context, R.color.standard_gray)))
        setCrossLineColor(ta.getColor(R.styleable.ClearButton_crossColor,
            ContextCompat.getColor(context, android.R.color.white)))
        ta.recycle()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun setButtonBackgroundColor(color: Int) {
        buttonBackgroundPaint.color = color
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun setCrossLineColor(color: Int) {
        buttonLinePaint.color = color
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { drawButton(canvas) }
    }

    /**
     * Draw circular background and a cross
     */
    private fun drawButton(canvas: Canvas) {
        if (buttonLinePaint.strokeWidth <= 0) {
            buttonLinePaint.strokeWidth = height * CROSS_THICKNESS_RATE
        }
        //按钮根据四个内边距确定下来，下面四个参数分别为考虑padding后这个矩形区域的坐标
        // 按钮左右位置
        val startPos = paddingLeft
        val endPos = width - paddingRight
        // 按钮上下位置
        val topPos = paddingTop
        val bottomPos = height - paddingBottom

        val crossPointX = (endPos - startPos) / 2f + startPos
        val crossPointY = (bottomPos - topPos) / 2f + topPos

        val radius = min(endPos - startPos, bottomPos - topPos) / 2f

        val lineToCircleBorderDis = radius / 2f

        val lineOneStartX = crossPointX - lineToCircleBorderDis
        val lineOneEndX = crossPointX + lineToCircleBorderDis

        val lineTwoStartY = crossPointY - lineToCircleBorderDis
        val lineTwoEndY = crossPointY + lineToCircleBorderDis

        canvas.drawCircle(crossPointX, crossPointY, radius, buttonBackgroundPaint)
        canvas.save()

        canvas.rotate(QUARTER_CIRCLE, crossPointX, crossPointY)
        canvas.drawLine(crossPointX, lineTwoStartY, crossPointX, lineTwoEndY,buttonLinePaint)
        canvas.drawLine(lineOneStartX, crossPointY, lineOneEndX, crossPointY, buttonLinePaint)
        canvas.restore()
    }
}