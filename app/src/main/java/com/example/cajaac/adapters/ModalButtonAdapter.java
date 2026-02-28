package com.example.cajaac.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.models.ModalButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para los botones del modal base.
 * Cada item muestra un ícono, texto y un separador vertical (excepto el último).
 */
public class ModalButtonAdapter extends RecyclerView.Adapter<ModalButtonAdapter.ViewHolder> {

    /**
     * Posición visual del botón para aplicar el fondo correcto con bordes redondeados.
     */
    private enum ButtonPosition {
        SINGLE,
        LEFT,
        MIDDLE,
        RIGHT
    }

    private List<ModalButton> buttons = new ArrayList<>();
    private int recyclerViewWidth = 0;

    public void submitList(List<ModalButton> newButtons) {
        this.buttons = newButtons != null ? newButtons : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setRecyclerViewWidth(int width) {
        this.recyclerViewWidth = width;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_modal_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModalButton buttonConfig = buttons.get(position);
        int total = buttons.size();

        // ── Distribuir ancho equitativamente; el último absorbe los píxeles restantes ──
        if (total > 0 && recyclerViewWidth > 0) {
            int baseWidth = recyclerViewWidth / total;
            int lastWidth = recyclerViewWidth - baseWidth * (total - 1);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            lp.width = (position == total - 1) ? lastWidth : baseWidth;
            holder.itemView.setLayoutParams(lp);
        }

        ButtonPosition btnPosition;
        if (total == 1) {
            btnPosition = ButtonPosition.SINGLE;
        } else if (position == 0) {
            btnPosition = ButtonPosition.LEFT;
        } else if (position == total - 1) {
            btnPosition = ButtonPosition.RIGHT;
        } else {
            btnPosition = ButtonPosition.MIDDLE;
        }

        holder.bind(buttonConfig, btnPosition, position == total - 1);
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ViewHolder
    // ─────────────────────────────────────────────────────────────────────────

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout container;
        private final ImageView icon;
        private final TextView text;
        private final View separator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.btn_modal_container);
            icon = itemView.findViewById(R.id.btn_modal_icon);
            text = itemView.findViewById(R.id.btn_modal_text);
            separator = itemView.findViewById(R.id.btn_modal_separator);
        }

        public void bind(ModalButton config, ButtonPosition position, boolean isLast) {
            Context ctx = itemView.getContext();

            // Texto
            text.setText(config.getText());

            // Ícono
            if (config.getIconResId() != 0) {
                icon.setImageResource(config.getIconResId());
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }

            // Fondo + ripple
            applyBackgroundWithRipple(ctx, config, position);

            // Tint del ícono
            applyIconTint(ctx, config);

            // Color del texto
            applyTextColor(ctx, config);

            // Separador: oculto en el último elemento
            separator.setVisibility(isLast ? View.GONE : View.VISIBLE);

            // Click listener
            if (config.getOnClickListener() != null) {
                container.setOnClickListener(v -> config.getOnClickListener().onClick());
            } else {
                container.setOnClickListener(null);
                container.setClickable(false);
            }
        }

        // ── Fondo con Ripple ─────────────────────────────────────────────────

        private void applyBackgroundWithRipple(Context ctx, ModalButton config, ButtonPosition position) {
            Drawable bgDrawable = null;

            if (config.getBackgroundResId() != -1 && config.getBackgroundResId() != 0) {
                bgDrawable = ContextCompat.getDrawable(ctx, config.getBackgroundResId());
            } else {
                int bgRes = getBackgroundForTypeAndPosition(ctx, config.getButtonType(), position);
                if (bgRes != 0) {
                    bgDrawable = ContextCompat.getDrawable(ctx, bgRes);
                }
            }

            // Mask rectangular para definir el área del ripple
            ShapeDrawable mask = new ShapeDrawable(new RectShape());
            mask.getPaint().setColor(Color.WHITE);

            int rippleColor = getRippleColor(ctx, config);
            ColorStateList rippleColorStateList = ColorStateList.valueOf(rippleColor);

            if (bgDrawable != null) {
                // Botón con fondo de color: ripple blanco semi-transparente sobre el fondo
                RippleDrawable rippleDrawable = new RippleDrawable(rippleColorStateList, bgDrawable, mask);
                container.setBackground(rippleDrawable);
            } else {
                // Botón NORMAL sin fondo: ripple gris sobre transparente, con mask para definir área
                RippleDrawable rippleDrawable = new RippleDrawable(rippleColorStateList, null, mask);
                container.setBackground(rippleDrawable);
            }

            container.setClickable(true);
            container.setFocusable(true);
        }

        /**
         * Devuelve un color semi-transparente para el efecto ripple según el tipo de botón.
         * Para botones con fondo de color usa blanco semi-transparente (se ve un "brillo"),
         * para botones normales usa negro semi-transparente.
         */
        private int getRippleColor(Context ctx, ModalButton config) {
            switch (config.getButtonType()) {
                case PRIMARY:
                case INFO:
                case WARNING:
                case DANGER:
                case SUCCESS:
                case SECONDARY:
                    return Color.argb(60, 255, 255, 255); // blanco 24% opacidad
                case NORMAL:
                default:
                    return Color.argb(40, 0, 0, 0); // negro 16% opacidad
            }
        }

        private int getBackgroundForTypeAndPosition(Context ctx, ModalButton.ButtonType type, ButtonPosition position) {
            String suffix;
            switch (position) {
                case LEFT:    suffix = "_left";   break;
                case MIDDLE:  suffix = "_middle"; break;
                case RIGHT:   suffix = "_right";  break;
                case SINGLE:  suffix = "_single"; break;
                default:      suffix = "_right";  break;
            }

            String prefix;
            switch (type) {
                case PRIMARY:   prefix = "background_button_primary";   break;
                case INFO:      prefix = "background_button_info";      break;
                case WARNING:   prefix = "background_button_warning";   break;
                case DANGER:    prefix = "background_button_danger";    break;
                case SUCCESS:   prefix = "background_button_success";   break;
                case SECONDARY: prefix = "background_button_secondary"; break;
                case NORMAL:
                default:
                    return 0;
            }

            return ctx.getResources().getIdentifier(prefix + suffix, "drawable", ctx.getPackageName());
        }

        // ── Tint del ícono ───────────────────────────────────────────────────

        private void applyIconTint(Context ctx, ModalButton config) {
            if (config.getIconTintResId() != -1 && config.getIconTintResId() != 0) {
                icon.setColorFilter(ContextCompat.getColor(ctx, config.getIconTintResId()));
            } else {
                switch (config.getButtonType()) {
                    case PRIMARY:
                    case INFO:
                    case WARNING:
                    case DANGER:
                    case SUCCESS:
                    case SECONDARY:
                        icon.setColorFilter(ContextCompat.getColor(ctx, R.color.white));
                        break;
                    case NORMAL:
                    default:
                        if (config.getIconTintResId() == -1) {
                            icon.setColorFilter(ContextCompat.getColor(ctx, R.color.text_85));
                        }
                        break;
                }
            }
        }

        // ── Color del texto ──────────────────────────────────────────────────

        private void applyTextColor(Context ctx, ModalButton config) {
            if (config.getTextColorResId() != -1 && config.getTextColorResId() != 0) {
                text.setTextColor(ContextCompat.getColor(ctx, config.getTextColorResId()));
            } else {
                switch (config.getButtonType()) {
                    case PRIMARY:
                    case INFO:
                    case WARNING:
                    case DANGER:
                    case SUCCESS:
                    case SECONDARY:
                        text.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                        break;
                    case NORMAL:
                    default:
                        text.setTextColor(ContextCompat.getColor(ctx, R.color.text_85));
                        break;
                }
            }
        }
    }
}








