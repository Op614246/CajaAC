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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.adapters.TransactionAdapter;
import com.example.cajaac.models.TransactionSection;
import com.example.cajaac.models.TransactionTotal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionSectionView extends LinearLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private RecyclerView rvItems;
    private TransactionAdapter adapter;
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
        rvItems = findViewById(R.id.rvItems);
        secondaryTotalsContainer = findViewById(R.id.secondaryTotalsContainer);
        tvTotalLabel = findViewById(R.id.tvTotalLabel);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        totalContainer = findViewById(R.id.totalContainer);

        // Configurar RecyclerView
        rvItems.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TransactionAdapter();
        rvItems.setAdapter(adapter);
    }

    public void setData(TransactionSection section) {
        // Configurar encabezado
        ivIcon.setImageResource(section.getIconRes());
        ivIcon.setColorFilter(ContextCompat.getColor(getContext(), section.getTitleColor()));
        tvTitle.setText(section.getTitle());
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), section.getTitleColor()));

        // Configurar adapter según el tipo de columna
        adapter.setColumnType(section.getColumnType());

        // Mostrar header para ciertos tipos de columna
        boolean showHeader = section.getColumnType() == TransactionSection.ColumnType.TABLE
                || section.getColumnType() == TransactionSection.ColumnType.TWO_COLUMNS
                || section.getColumnType() == TransactionSection.ColumnType.FOUR_COLUMNS
                || section.getColumnType() == TransactionSection.ColumnType.FIVE_COLUMNS;
        adapter.setShowHeader(showHeader);

        // Configurar mensaje vacío
        String emptyMessage = section.getEmptyMessage();
        if (emptyMessage == null || emptyMessage.isEmpty()) {
            emptyMessage = "No hay registros";
        }
        adapter.setEmptyMessage(emptyMessage);

        // Enviar items al adapter
        adapter.submitList(section.getItems());

        // Configurar totales secundarios si existen Y hay items
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (section.getSecondaryTotals() != null && !section.getSecondaryTotals().isEmpty()
            && section.getItems() != null && !section.getItems().isEmpty()) {
            totalContainer.setVisibility(View.GONE);
            secondaryTotalsContainer.setVisibility(View.VISIBLE);
            secondaryTotalsContainer.removeAllViews();

            for (TransactionTotal total : section.getSecondaryTotals()) {
                View totalView;

                if (section.getColumnType() == TransactionSection.ColumnType.TABLE 
                    || section.getColumnType() == TransactionSection.ColumnType.FIVE_COLUMNS
                    || section.getColumnType() == TransactionSection.ColumnType.TWO_COLUMNS) {
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

        // Configurar o ocultar la sección de total principal
        if (section.hasTotal()
            && (section.getSecondaryTotals() == null || section.getSecondaryTotals().isEmpty())
            && section.getItems() != null && !section.getItems().isEmpty()) {
            totalContainer.setVisibility(View.VISIBLE);
            tvTotalLabel.setText(section.getTotalLabel());
            totalContainer.setBackgroundColor(
                    ContextCompat.getColor(getContext(), section.getTotalBackgroundColor())
            );

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
