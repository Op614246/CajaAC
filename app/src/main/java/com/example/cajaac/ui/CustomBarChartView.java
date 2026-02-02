package com.example.cajaac.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.cajaac.R;

import java.util.ArrayList;
import java.util.List;

public class CustomBarChartView extends View {

    private List<BarItem> barItems = new ArrayList<>();
    private Paint trackPaint;
    private Paint progressPaint;
    private Paint textPaint;

    // Dimensiones
    private float barWidth;
    private float cornerRadius;
    private float textSpacing;
    private float bottomPadding; // Espacio para el texto
    private float topPadding; // Espacio superior para reducir altura

    public CustomBarChartView(Context context) {
        super(context);
        init(context);
    }

    public CustomBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inicializar pinturas
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setColor(getResources().getColor(R.color.text_5, null));
        trackPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(spToPx(14, context)); // 11sp

        try {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
            textPaint.setTypeface(typeface);
        } catch (Exception e) {
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        // Dimensiones fijas o iniciales
        barWidth = dpToPx(10, context); // 12dp ancho de barra
        cornerRadius = dpToPx(100, context); // Totalmente redondeado
        textSpacing = dpToPx(8, context);
        bottomPadding = dpToPx(20, context); // Espacio reservado para texto abajo
        topPadding = dpToPx(24, context); // Reducir altura visual de la barra
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (barItems == null || barItems.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int count = barItems.size();

        // Espacio disponible para las barras (horizontal)
        // Dividimos el ancho en 'count' secciones
        float sectionWidth = (float) width / count;

        // Altura disponible para la barra (restar espacio del texto y padding superior)
        float availableBarHeight = height - bottomPadding - textSpacing - topPadding;

        for (int i = 0; i < count; i++) {
            BarItem item = barItems.get(i);

            // Centro de la sección actual
            float centerX = (i * sectionWidth) + (sectionWidth / 2);

            // Coordenadas de la barra (Track)
            float left = centerX - (barWidth / 2);
            float right = centerX + (barWidth / 2);
            float top = topPadding;
            float bottom = top + availableBarHeight;

            RectF trackRect = new RectF(left, top, right, bottom);

            // Dibujar track (fondo gris)
            canvas.drawRoundRect(trackRect, cornerRadius, cornerRadius, trackPaint);

            // Calcular altura de progreso
            // Valor es porcentaje (0-100)
            float progressHeight = (item.value / 100f) * availableBarHeight;
            float progressTop = bottom - progressHeight; // Crecimiento hacia arriba

            RectF progressRect = new RectF(left, progressTop, right, bottom);

            // Dibujar progreso
            progressPaint.setColor(item.color);
            canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, progressPaint);

            // Dibujar Texto
            textPaint.setColor(item.color); // Color del texto igual a la barra
            float textX = centerX;
            float textY = height - dpToPx(5, getContext()); // Un poco arriba del borde inferior

            canvas.drawText(item.label, textX, textY, textPaint);
        }
    }

    public void setBars(List<BarItem> items) {
        this.barItems = items;
        invalidate(); // Redibujar
    }

    private float dpToPx(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private float spToPx(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static class BarItem {
        public String label;
        public float value; // 0-100
        public int color;

        public BarItem(String label, float value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }
}
