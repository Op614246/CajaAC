package com.example.cajaac.adapters;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.models.TransactionItem;
import com.example.cajaac.models.TransactionSection.ColumnType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_TWO_COLUMNS = 2;
    private static final int VIEW_TYPE_THREE_COLUMNS = 3;
    private static final int VIEW_TYPE_FOUR_COLUMNS = 4;
    private static final int VIEW_TYPE_TABLE = 5;
    private static final int VIEW_TYPE_FIVE_COLUMNS = 6;
    private static final int VIEW_TYPE_EMPTY = 7;

    private List<TransactionItem> items = new ArrayList<>();
    private ColumnType columnType = ColumnType.DEFAULT;
    private boolean showHeader = false;
    private boolean isEmpty = false;
    private String emptyMessage = "No hay registros";

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public void setEmptyMessage(String message) {
        this.emptyMessage = message;
    }

    public void submitList(List<TransactionItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        this.isEmpty = this.items.isEmpty();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (showHeader && position == 0) {
            return VIEW_TYPE_HEADER;
        }
        if (isEmpty) {
            return VIEW_TYPE_EMPTY;
        }
        switch (columnType) {
            case TWO_COLUMNS:
                return VIEW_TYPE_TWO_COLUMNS;
            case THREE_COLUMNS:
                return VIEW_TYPE_THREE_COLUMNS;
            case FOUR_COLUMNS:
                return VIEW_TYPE_FOUR_COLUMNS;
            case TABLE:
                return VIEW_TYPE_TABLE;
            case FIVE_COLUMNS:
                return VIEW_TYPE_FIVE_COLUMNS;
            default:
                return VIEW_TYPE_DEFAULT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                int headerLayout = getHeaderLayout();
                view = inflater.inflate(headerLayout, parent, false);
                return new HeaderViewHolder(view);
            case VIEW_TYPE_EMPTY:
                view = inflater.inflate(R.layout.item_empty_message, parent, false);
                return new EmptyViewHolder(view);
            case VIEW_TYPE_FIVE_COLUMNS:
                view = inflater.inflate(R.layout.item_transaction_table_five_columns, parent, false);
                return new FiveColumnsViewHolder(view);
            case VIEW_TYPE_TABLE:
                view = inflater.inflate(R.layout.item_transaction_table, parent, false);
                return new TableViewHolder(view);
            case VIEW_TYPE_FOUR_COLUMNS:
                view = inflater.inflate(R.layout.item_transaction_four_columns, parent, false);
                return new FourColumnsViewHolder(view);
            case VIEW_TYPE_THREE_COLUMNS:
                view = inflater.inflate(R.layout.item_transaction_three_columns, parent, false);
                return new ThreeColumnsViewHolder(view);
            case VIEW_TYPE_TWO_COLUMNS:
            case VIEW_TYPE_DEFAULT:
            default:
                view = inflater.inflate(R.layout.item_transaction, parent, false);
                return new DefaultViewHolder(view);
        }
    }

    private int getHeaderLayout() {
        switch (columnType) {
            case TABLE:
                return R.layout.header_transaction_table;
            case TWO_COLUMNS:
                return R.layout.header_transaction_two_columns;
            case FOUR_COLUMNS:
                return R.layout.header_transaction_four_columns;
            case FIVE_COLUMNS:
                return R.layout.header_transaction_table_five_columns;
            default:
                return R.layout.header_transaction_table;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            return; // Headers no necesitan binding
        }
        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind(emptyMessage);
            return;
        }

        int itemPosition = showHeader ? position - 1 : position;
        TransactionItem item = items.get(itemPosition);

        if (holder instanceof FiveColumnsViewHolder) {
            ((FiveColumnsViewHolder) holder).bind(item);
        } else if (holder instanceof TableViewHolder) {
            ((TableViewHolder) holder).bind(item);
        } else if (holder instanceof FourColumnsViewHolder) {
            ((FourColumnsViewHolder) holder).bind(item);
        } else if (holder instanceof ThreeColumnsViewHolder) {
            ((ThreeColumnsViewHolder) holder).bind(item);
        } else if (holder instanceof DefaultViewHolder) {
            ((DefaultViewHolder) holder).bind(item, columnType);
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty) {
            // Header (si aplica) + mensaje vacío
            return (showHeader ? 1 : 0) + 1;
        }
        return items.size() + (showHeader ? 1 : 0);
    }

    // ViewHolder para headers
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // ViewHolder para mensaje vacío
    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvEmptyMessage;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmptyMessage = itemView.findViewById(R.id.tvEmptyMessage);
        }

        public void bind(String message) {
            tvEmptyMessage.setText(message);
        }
    }

    // ViewHolder para DEFAULT y TWO_COLUMNS
    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLabel;
        private final TextView tvValue;

        public DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvValue = itemView.findViewById(R.id.tvValue);
        }

        public void bind(TransactionItem item, ColumnType columnType) {
            tvLabel.setText(item.getLabel());
            if (columnType == ColumnType.TWO_COLUMNS) {
                tvValue.setText(item.getAmount());
            } else {
                // DEFAULT - usar serie si está disponible, sino amount
                String value = !item.getSerie().isEmpty() ? item.getSerie() : item.getAmount();
                tvValue.setText(value);

                if (item.getCustomTextSize() != null) {
                    tvValue.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, item.getCustomTextSize());
                }
            }
        }
    }

    // ViewHolder para THREE_COLUMNS
    static class ThreeColumnsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLabel;
        private final TextView tvQuantity;
        private final TextView tvAmount;

        public ThreeColumnsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(TransactionItem item) {
            tvLabel.setText(item.getLabel());
            tvQuantity.setText(item.getQuantity());
            tvAmount.setText(item.getAmount());
        }
    }

    // ViewHolder para FOUR_COLUMNS
    static class FourColumnsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLabel;
        private final TextView tvOperations;
        private final TextView tvTotal;
        private final TextView tvAverage;

        public FourColumnsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvOperations = itemView.findViewById(R.id.tvOperations);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvAverage = itemView.findViewById(R.id.tvAverage);
        }

        public void bind(TransactionItem item) {
            tvLabel.setText(item.getLabel());
            tvOperations.setText(item.getOperations());
            tvTotal.setText(item.getAmount());
            tvAverage.setText(item.getAverage());
        }
    }

    // ViewHolder para TABLE (4 columnas: date, user, concept, amount)
    static class TableViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvUser;
        private final TextView tvConcept;
        private final TextView tvAmount;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvConcept = itemView.findViewById(R.id.tvConcept);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(TransactionItem item) {
            tvDate.setText(item.getDate());
            tvUser.setText(item.getUser());
            tvConcept.setText(formatConceptText(item.getConcept(), item.getDeliveryCode(), item.getPaymentMethod()));
            tvAmount.setText(item.getAmount());
        }
    }

    // ViewHolder para FIVE_COLUMNS
    static class FiveColumnsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvUser;
        private final TextView tvReceivedFrom;
        private final TextView tvConcept;
        private final TextView tvAmount;

        public FiveColumnsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvReceivedFrom = itemView.findViewById(R.id.tvReceivedFrom);
            tvConcept = itemView.findViewById(R.id.tvConcept);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(TransactionItem item) {
            tvDate.setText(item.getDate());
            tvUser.setText(item.getUser());
            tvReceivedFrom.setText(item.getReceivedFrom());
            tvConcept.setText(formatConceptText(item.getConcept(), item.getDeliveryCode(), item.getPaymentMethod()));
            tvAmount.setText(item.getAmount());
        }
    }

    /**
     * Aplica negrita a "Delivery #XXXXX" y "forma de pago" en el texto del concepto.
     */
    private static SpannableString formatConceptText(String baseText, String deliveryCode, String paymentMethod) {
        String finalText = baseText;

        if (deliveryCode != null && !deliveryCode.isEmpty() && paymentMethod != null && !paymentMethod.isEmpty()) {
            finalText = "Ingreso por confirmación de Delivery " + deliveryCode + " con forma de pago " + paymentMethod;
        }

        SpannableString spannable = new SpannableString(finalText);

        Pattern deliveryPattern = Pattern.compile("Delivery #\\d+");
        Matcher deliveryMatcher = deliveryPattern.matcher(finalText);

        if (deliveryMatcher.find()) {
            spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                deliveryMatcher.start(),
                deliveryMatcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            int paymentStart = finalText.indexOf(paymentMethod);
            if (paymentStart != -1) {
                spannable.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    paymentStart,
                    paymentStart + paymentMethod.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return spannable;
    }
}
