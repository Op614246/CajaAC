package com.example.cajaac.ui;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.button.MaterialButton;
import com.example.cajaac.R;

public class BotonReutilizable extends MaterialButton {

    private int tipoBoton;
    private int tamanoBoton; // 0 = grande, 1 = pequeno
    private int colorBase;

    public BotonReutilizable(@NonNull Context context) {
        super(context);
        init(null);
    }

    public BotonReutilizable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BotonReutilizable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        Resources res = getContext().getResources();
        setCornerRadius(res.getDimensionPixelSize(R.dimen.boton_corner_radius));
        setIconGravity(ICON_GRAVITY_TEXT_START);
        setIconPadding(res.getDimensionPixelSize(R.dimen.boton_icon_padding));
        setAllCaps(false);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.boton_text_size));
        setLetterSpacing(0);

        // Quitar la sombra/elevación
        setElevation(0f);
        setStateListAnimator(null);

        if (attrs != null) {
            // Asegúrate de que en attrs.xml el declare-styleable se llame "BotonReutilizable"
            try (TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BotonReutilizable)) {
                tipoBoton = a.getInt(R.styleable.BotonReutilizable_tipoBoton, 0);
                tamanoBoton = a.getInt(R.styleable.BotonReutilizable_tamanoBoton, 1); // Default pequeño (1)

                int colorDefault = getContext().getColor(R.color.info);

                colorBase = a.getColor(R.styleable.BotonReutilizable_colorBase, colorDefault);
            }
        } else {
             tamanoBoton = 1; // Default
        }

        aplicarPadding(res);
        aplicarEstilo();
    }

    private void aplicarPadding(Resources res) {
        int paddingH;
        int paddingV;

        if (tamanoBoton == 0) { // Grande
            paddingH = res.getDimensionPixelSize(R.dimen.boton_padding_horizontal_grande);
            paddingV = res.getDimensionPixelSize(R.dimen.boton_padding_vertical_grande);
        } else { // Pequeño
            paddingH = res.getDimensionPixelSize(R.dimen.boton_padding_horizontal_pequeno);
            paddingV = res.getDimensionPixelSize(R.dimen.boton_padding_vertical_pequeno);
        }

        setPadding(paddingH, paddingV, paddingH, paddingV);
    }

    private void aplicarEstilo() {
        if (tipoBoton == 0) {
            // --- ESTILO BORDEADO ---
            int colorFondoTenue = ColorUtils.setAlphaComponent(colorBase, 25);
            setBackgroundTintList(ColorStateList.valueOf(colorFondoTenue));

            setStrokeColor(ColorStateList.valueOf(colorBase));
            setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.boton_stroke_width));

            setTextColor(colorBase);
            setIconTint(ColorStateList.valueOf(colorBase));

        } else {
            // --- ESTILO RELLENO ---
            setBackgroundTintList(ColorStateList.valueOf(colorBase));
            setStrokeWidth(0);

            setTextColor(Color.WHITE);
            setIconTint(ColorStateList.valueOf(Color.WHITE));
        }
    }
}