package com.example.cajaac.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.MetricAffectingSpan;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.cajaac.R;
import com.example.cajaac.models.TransactionItem;
import com.example.cajaac.models.TransactionSection;
import com.example.cajaac.models.TransactionTotal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionSectionView extends LinearLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private LinearLayout itemsContainer;
    private LinearLayout secondaryTotalsContainer;
    private TextView tvTotalLabel;
    private TextView tvTotalQuantity;
    private TextView tvTotalAmount;
    private LinearLayout totalContainer;

    public TransactionSectionView(Context context) {
        super(context);
        init(context);
    }

    public TransactionSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionSectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.border_rounded);

        LayoutInflater.from(context).inflate(R.layout.view_transaction_section, this, true);

        ivIcon = findViewById(R.id.ivIcon);
        tvTitle = findViewById(R.id.tvTitle);
        itemsContainer = findViewById(R.id.itemsContainer);
        secondaryTotalsContainer = findViewById(R.id.secondaryTotalsContainer);
        tvTotalLabel = findViewById(R.id.tvTotalLabel);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        totalContainer = findViewById(R.id.totalContainer);
    }

    public void setData(TransactionSection section) {
        // Configurar encabezado
        ivIcon.setImageResource(section.getIconRes());
        ivIcon.setColorFilter(ContextCompat.getColor(getContext(), section.getTitleColor()));
        tvTitle.setText(section.getTitle());
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), section.getTitleColor()));

        // Limpiar items anteriores
        itemsContainer.removeAllViews();

        // Agregar header si es tipo TABLE, TWO_COLUMNS, FOUR_COLUMNS o FIVE_COLUMNS
        if (section.getColumnType() == TransactionSection.ColumnType.TABLE) {
            View headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.header_transaction_table, itemsContainer, false);
            itemsContainer.addView(headerView);
        } else if (section.getColumnType() == TransactionSection.ColumnType.TWO_COLUMNS) {
            View headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.header_transaction_two_columns, itemsContainer, false);
            itemsContainer.addView(headerView);
        } else if (section.getColumnType() == TransactionSection.ColumnType.FOUR_COLUMNS) {
            View headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.header_transaction_four_columns, itemsContainer, false);
            itemsContainer.addView(headerView);
        } else if (section.getColumnType() == TransactionSection.ColumnType.FIVE_COLUMNS) {
            View headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.header_transaction_table_five_columns, itemsContainer, false);
            itemsContainer.addView(headerView);
        }

        // Agregar items dinámicamente
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (TransactionItem item : section.getItems()) {
            View itemView;

            // Seleccionar el layout según el tipo de columnas
            if (section.getColumnType() == TransactionSection.ColumnType.FIVE_COLUMNS) {
                itemView = inflater.inflate(R.layout.item_transaction_table_five_columns, itemsContainer, false);

                TextView tvDate = itemView.findViewById(R.id.tvDate);
                TextView tvUser = itemView.findViewById(R.id.tvUser);
                TextView tvReceivedFrom = itemView.findViewById(R.id.tvReceivedFrom);
                TextView tvConcept = itemView.findViewById(R.id.tvConcept);
                TextView tvAmount = itemView.findViewById(R.id.tvAmount);

                tvDate.setText(item.getDate());
                tvUser.setText(item.getUser());
                tvReceivedFrom.setText(item.getReceivedFrom());
                tvConcept.setText(formatConceptText(item.getConcept(), item.getDeliveryCode(), item.getPaymentMethod()));
                tvAmount.setText(item.getAmount());
            } else if (section.getColumnType() == TransactionSection.ColumnType.TABLE) {
                itemView = inflater.inflate(R.layout.item_transaction_table, itemsContainer, false);

                TextView tvDate = itemView.findViewById(R.id.tvDate);
                TextView tvUser = itemView.findViewById(R.id.tvUser);
                TextView tvConcept = itemView.findViewById(R.id.tvConcept);
                TextView tvAmount = itemView.findViewById(R.id.tvAmount);

                tvDate.setText(item.getDate());
                tvUser.setText(item.getUser());
                tvConcept.setText(formatConceptText(item.getConcept(), item.getDeliveryCode(), item.getPaymentMethod()));
                tvAmount.setText(item.getAmount());
            } else if (section.getColumnType() == TransactionSection.ColumnType.FOUR_COLUMNS) {
                itemView = inflater.inflate(R.layout.item_transaction_four_columns, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvOperations = itemView.findViewById(R.id.tvOperations);
                TextView tvTotal = itemView.findViewById(R.id.tvTotal);
                TextView tvAverage = itemView.findViewById(R.id.tvAverage);

                tvLabel.setText(item.getLabel());
                tvOperations.setText(item.getOperations());
                tvTotal.setText(item.getAmount());
                tvAverage.setText(item.getAverage());
            } else if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                itemView = inflater.inflate(R.layout.item_transaction_three_columns, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvQuantity = itemView.findViewById(R.id.tvQuantity);
                TextView tvAmount = itemView.findViewById(R.id.tvAmount);

                tvLabel.setText(item.getLabel());
                tvQuantity.setText(item.getQuantity());
                tvAmount.setText(item.getAmount());
            } else if (section.getColumnType() == TransactionSection.ColumnType.TWO_COLUMNS) {
                itemView = inflater.inflate(R.layout.item_transaction, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvValue = itemView.findViewById(R.id.tvValue);

                tvLabel.setText(item.getLabel());
                tvValue.setText(item.getAmount());
            } else {
                // DEFAULT usamos el mismo layout genérico
                itemView = inflater.inflate(R.layout.item_transaction, itemsContainer, false);

                TextView tvLabel = itemView.findViewById(R.id.tvLabel);
                TextView tvValue = itemView.findViewById(R.id.tvValue);

                tvLabel.setText(item.getLabel());
                // Usar serie si está disponible, sino usar amount
                String value = !item.getSerie().isEmpty() ? item.getSerie() : item.getAmount();
                tvValue.setText(value);

                // Aplicar tamaño de texto personalizado si está definido
                if (item.getCustomTextSize() != null) {
                    tvValue.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, item.getCustomTextSize());
                }
            }

            itemsContainer.addView(itemView);
        }

        // Mostrar mensaje vacío si no hay items
        if (section.getItems().isEmpty()) {
            View emptyView = inflater.inflate(R.layout.item_empty_message, itemsContainer, false);
            TextView tvEmptyMessage = emptyView.findViewById(R.id.tvEmptyMessage);

            String message = section.getEmptyMessage();
            if (message == null || message.isEmpty()) {
                message = "No hay registros";
            }
            tvEmptyMessage.setText(message);

            itemsContainer.addView(emptyView);
        }

        // Configurar totales secundarios si existen Y hay items
        if (section.getSecondaryTotals() != null && !section.getSecondaryTotals().isEmpty()
            && section.getItems() != null && !section.getItems().isEmpty()) {
            totalContainer.setVisibility(View.GONE); // Ocultar el total principal
            secondaryTotalsContainer.setVisibility(View.VISIBLE);
            secondaryTotalsContainer.removeAllViews();

            // Agregar cada total secundario al contenedor dedicado
            for (TransactionTotal total : section.getSecondaryTotals()) {
                View totalView;

                if (section.getColumnType() == TransactionSection.ColumnType.TABLE 
                    || section.getColumnType() == TransactionSection.ColumnType.FIVE_COLUMNS
                    || section.getColumnType() == TransactionSection.ColumnType.TWO_COLUMNS) {
                    // Para tabla (2, 4 o 5 columnas), usar layout de total simplificado (solo label y monto)
                    totalView = inflater.inflate(R.layout.item_transaction_table_total, secondaryTotalsContainer, false);

                    TextView tvTotalLabel = totalView.findViewById(R.id.tvTotalLabel);
                    TextView tvTotalAmount = totalView.findViewById(R.id.tvTotalAmount);

                    tvTotalLabel.setText(formatTotalLabelText(total.getLabel()));
                    tvTotalAmount.setText(total.getAmount());

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                } else if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                    totalView = inflater.inflate(R.layout.item_transaction_three_columns, secondaryTotalsContainer, false);

                    TextView tvLabel = totalView.findViewById(R.id.tvLabel);
                    TextView tvQuantity = totalView.findViewById(R.id.tvQuantity);
                    TextView tvAmount = totalView.findViewById(R.id.tvAmount);

                    tvLabel.setText(total.getLabel());
                    tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvQuantity.setText(total.getQuantity());
                    tvQuantity.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvAmount.setText(total.getAmount());
                    tvAmount.setTypeface(null, android.graphics.Typeface.BOLD);

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                    totalView.setPadding(10, 8, 10, 8);
                } else {
                    totalView = inflater.inflate(R.layout.item_transaction, secondaryTotalsContainer, false);

                    TextView tvLabel = totalView.findViewById(R.id.tvLabel);
                    TextView tvValue = totalView.findViewById(R.id.tvValue);

                    tvLabel.setText(total.getLabel());
                    tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);
                    tvValue.setText(total.getAmount());
                    tvValue.setTypeface(null, android.graphics.Typeface.BOLD);

                    totalView.setBackgroundColor(ContextCompat.getColor(getContext(), total.getBackgroundColor()));
                    totalView.setPadding(10, 8, 10, 8);
                }

                secondaryTotalsContainer.addView(totalView);
            }
        } else {
            secondaryTotalsContainer.setVisibility(View.GONE);
        }

        // Configurar o ocultar la sección de total principal (también valida si hay items)
        if (section.hasTotal()
            && (section.getSecondaryTotals() == null || section.getSecondaryTotals().isEmpty())
            && section.getItems() != null && !section.getItems().isEmpty()) {
            totalContainer.setVisibility(View.VISIBLE);
            tvTotalLabel.setText(section.getTotalLabel());
            totalContainer.setBackgroundColor(
                    ContextCompat.getColor(getContext(), section.getTotalBackgroundColor())
            );

            // Mostrar columna de cantidad si es THREE_COLUMNS
            if (section.getColumnType() == TransactionSection.ColumnType.THREE_COLUMNS) {
                tvTotalQuantity.setVisibility(View.VISIBLE);
                tvTotalQuantity.setText(section.getTotalQuantity());
                tvTotalAmount.setText(section.getTotalAmount());
            } else {
                tvTotalQuantity.setVisibility(View.GONE);
                tvTotalAmount.setText(section.getTotalAmount());
            }
        } else if (!section.hasTotal()) {
            totalContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Aplica negrita a "Delivery #XXXXX" y "forma de pago" en el texto del concepto.
     * Si deliveryCode y paymentMethod vienen del backend, los usa para construir el texto formateado.
     * Si no, intenta encontrar patrones en el texto existente (fallback).
     */
    private SpannableString formatConceptText(String baseText, String deliveryCode, String paymentMethod) {
        String finalText = baseText;

        // Si tenemos deliveryCode y paymentMethod del backend, construir el texto completo
        if (deliveryCode != null && !deliveryCode.isEmpty() && paymentMethod != null && !paymentMethod.isEmpty()) {
            // Construir el texto: "Ingreso por confirmación de Delivery #31765 con forma de pago en línea"
            finalText = "Ingreso por confirmación de Delivery " + deliveryCode + " con forma de pago " + paymentMethod;
        }

        SpannableString spannable = new SpannableString(finalText);

        // Aplicar negrita a "Delivery #XXXXX"
        Pattern deliveryPattern = Pattern.compile("Delivery #\\d+");
        Matcher deliveryMatcher = deliveryPattern.matcher(finalText);

        if (deliveryMatcher.find()) {
            spannable.setSpan(
                new StyleSpan(android.graphics.Typeface.BOLD),
                deliveryMatcher.start(),
                deliveryMatcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // Aplicar negrita a la forma de pago si está presente
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            int paymentStart = finalText.indexOf(paymentMethod);
            if (paymentStart != -1) {
                spannable.setSpan(
                    new StyleSpan(android.graphics.Typeface.BOLD),
                    paymentStart,
                    paymentStart + paymentMethod.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return spannable;
    }

    /**
     * Aplica negrita al texto excepto al contenido entre paréntesis (incluyendo los paréntesis)
     * El contenido entre paréntesis usará Roboto Regular
     */
    private SpannableString formatTotalLabelText(String text) {
        SpannableString spannable = new SpannableString(text);

        // Patrón para encontrar texto entre paréntesis
        Pattern parenthesisPattern = Pattern.compile("\\([^)]+\\)");
        Matcher matcher = parenthesisPattern.matcher(text);

        // Cargar la fuente Roboto Regular
        Typeface robotoRegular = ResourcesCompat.getFont(getContext(), R.font.roboto);

        // Si hay paréntesis, aplicar negrita solo antes de ellos
        if (matcher.find()) {
            int parenthesisStart = matcher.start();
            int parenthesisEnd = matcher.end();

            // Aplicar negrita solo al texto antes del paréntesis
            if (parenthesisStart > 0) {
                spannable.setSpan(
                    new StyleSpan(android.graphics.Typeface.BOLD),
                    0,
                    parenthesisStart,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            // Aplicar Roboto Regular al texto entre paréntesis
            spannable.setSpan(
                new CustomTypefaceSpan(robotoRegular),
                parenthesisStart,
                parenthesisEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        } else {
            // Si no hay paréntesis, aplicar negrita a todo
            spannable.setSpan(
                new StyleSpan(android.graphics.Typeface.BOLD),
                0,
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return spannable;
    }

    /**
     * Span personalizado para aplicar un Typeface específico
     */
    private static class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        public CustomTypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint tp) {
            applyTypeface(tp);
        }

        @Override
        public void updateMeasureState(@NonNull TextPaint tp) {
            applyTypeface(tp);
        }

        private void applyTypeface(TextPaint paint) {
            paint.setTypeface(typeface);
        }
    }
}
