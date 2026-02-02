package com.example.cajaac;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cajaac.models.TransactionItem;
import com.example.cajaac.models.TransactionSection;
import com.example.cajaac.models.TransactionTotal;
import com.example.cajaac.ui.ExpandableCardView;
import com.example.cajaac.ui.TransactionSectionView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TransactionSectionView transactionSectionIngresos = findViewById(R.id.transactionSectionIngresos);
        TransactionSectionView transactionSectionEgresos = findViewById(R.id.transactionSectionEgresos);
        TransactionSectionView transactionSectionCorrelativos = findViewById(R.id.transactionSectionCorrelativos);
        TransactionSectionView transactionSectionHechas = findViewById(R.id.transactionSectionHechas);
        TransactionSectionView transactionSectionAnuladas = findViewById(R.id.transactionSectionAnuladas);
        TransactionSectionView transactionSectionTable = findViewById(R.id.transactionSectionTable);
        TransactionSectionView transactionSectionEgresosTable = findViewById(R.id.transactionSectionEgresosTable);

        List<TransactionItem> ingresosItems = Arrays.asList(
                new TransactionItem("Apertura", "S/ 1,062.70"),
                new TransactionItem("Ventas en S/", "S/ 0.00"),
                new TransactionItem("Ingresos extras", "S/ 0.00"),
                new TransactionItem("Ventas con tarjeta", "S/ 6,875.09"),
                new TransactionItem("Ventas por transferencia", "S/ 0.00"),
                new TransactionItem("Ventas por vales", "S/ 0.00"),
                new TransactionItem("Créditos cobrados en soles", "S/ 0.00"),
                new TransactionItem("Créditos cobrados en tarjeta", "S/ 0.00"),
                new TransactionItem("Créditos cobrados con otros medios de pago", "S/ 0.00"),
                new TransactionItem("Propinas en efectivo", "S/ 0.00"),
                new TransactionItem("Propinas con tarjeta", "S/ 0.00"),
                new TransactionItem("Propinas con otros medios", "S/ 0.00"),
                new TransactionItem("Total por costo de delivery (incluido en total de ventas)", "S/ 0.00")
        );

        List<TransactionItem> egresosItems = Arrays.asList(
                new TransactionItem("Egresos extra", "S/ 0.00"),
                new TransactionItem("Compras", "S/ 0.00"),
                new TransactionItem("Pag. de compras al crédito", "S/ 0.00"),
                new TransactionItem("Descuentos en soles", "S/ 0.00"),
                new TransactionItem("Descuentos en dolares", "S/ 0.00"),
                new TransactionItem("Descuentos en euros", "S/ 0.00")
        );

        // Card 1: Correlativos usados (2 columnas, sin total)
        List<TransactionItem> correlativosItems = Arrays.asList(
                new TransactionItem("Facturas", "Serie F002 del 00002266 al 00002267"),
                new TransactionItem("Boletas", "Serie B002 del 00130834 al 00131035"),
                new TransactionItem("Nota de Ventas", "-"),
                new TransactionItem("Nota de credito Boletas", "-"),
                new TransactionItem("Nota de debito Boletas", "-"),
                new TransactionItem("Nota de credito Facturas", "-"),
                new TransactionItem("Nota de debito Facturas", "-")
        );

        // Card 2: Transacciones hechas (3 columnas, con total)
        List<TransactionItem> hechosItems = Arrays.asList(
                new TransactionItem("N° Facturas", "0", "S/1,062.70"),
                new TransactionItem("N° Boletas", "0", "S/0.00"),
                new TransactionItem("N° Nota de Ventas", "0", "S/0.00"),
                new TransactionItem("N° Nota de credito Boletas", "0", "S/6,875.09"),
                new TransactionItem("N° Nota de debito Boletas", "0", "S/0.00"),
                new TransactionItem("N° Nota de credito Facturas", "0", "S/0.00"),
                new TransactionItem("N° Nota de debito Facturas", "0", "S/0.00")
        );

        // Card 3: Transacciones anuladas (3 columnas, sin total)
        List<TransactionItem> anuladasItems = Arrays.asList(
                new TransactionItem("N° Facturas", "0"),
                new TransactionItem("N° Boletas", "0"),
                new TransactionItem("N° Nota de Ventas", "0"),
                new TransactionItem("N° Nota de credito Boletas", "0"),
                new TransactionItem("N° Nota de debito Boletas","0"),
                new TransactionItem("N° Nota de credito Facturas", "0"),
                new TransactionItem("N° Nota de debito Facturas", "0")
        );

        TransactionSection ingresosSection = new TransactionSection(
                "Ingresos",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                ingresosItems,
                "TOTAL INGRESOS",
                "S/ 7,937.79",
                R.color.primary_5
        );

        TransactionSection egresosSection = new TransactionSection(
                "Egresos",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_down,
                egresosItems,
                "TOTAL EGRESOS",
                "S/ 0.00",
                R.color.danger_5
        );

        TransactionSection correlativosSection = new TransactionSection(
                "Correlativos usados",
                R.color.info,
                R.drawable.icon_svg_list,
                correlativosItems,
                false,
                TransactionSection.ColumnType.DEFAULT
        );

        TransactionSection hechosSection = new TransactionSection(
                "Transacciones hechas",
                R.color.info,
                R.drawable.icon_svg_arrow_left_arrow_right,
                hechosItems,
                "TOTAL TRANSACCIONES HECHAS",
                "0",
                "S/8,945.41",
                R.color.info_5,
                true,
                TransactionSection.ColumnType.THREE_COLUMNS
        );

        TransactionSection anuladasSection = new TransactionSection(
                "Transacciones anuladas",
                R.color.info,
                R.drawable.icon_svg_circle_xmark,
                anuladasItems,
                "TOTAL TRANSACCIONES ANULADAS",
                "0",
                R.color.info_5
        );

        // Tabla con múltiples totales (Movimientos de ingresos extra)
        // NOTA: Si tableItems está vacío (Arrays.asList()), los totales NO se mostrarán automáticamente
        List<TransactionItem> tableItems = Arrays.asList(
                new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                        "Ingreso por confirmación de Delivery #31765 con forma de pago En línea", "S/20.00"),
                new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                        "Ingreso por confirmación de Delivery #31765 con forma de pago En línea", "S/20.00")
        );

        // Para probar sin items, cambia la línea anterior por:
        // List<TransactionItem> tableItems = Arrays.asList();

        List<TransactionTotal> tableTotals = Arrays.asList(
                new TransactionTotal("TOTAL INGRESOS (Efectivo y Tarjeta)", "S/ 0.00", R.color.info_5),
                new TransactionTotal("TOTAL INGRESOS POR DELIVERY (En línea, transferencia, Yape, Plin)", "S/ 40.00", R.color.info_5)
        );

        TransactionSection tableSection = new TransactionSection(
                "Movimientos de ingresos extra",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                tableItems,
                tableTotals,
                TransactionSection.ColumnType.TABLE,
                "No hay ingresos registrados"
        );

        // Tabla de egresos (5 columnas: Fecha, Usuario, Recibido de, Concepto, Monto)
        // Lista vacía para mostrar el mensaje
        List<TransactionItem> egresosTableItems = Arrays.asList(
//                new TransactionItem("15/01/2025\n10:30 A.M.", "María López", "Proveedor ABC", "Compra de insumos", "S/150.00"),
//                new TransactionItem("15/01/2025\n02:15 P.M.", "Carlos Ruiz", "Distribuidora XYZ", "Pago de factura pendiente", "S/320.50"),
//                new TransactionItem("16/01/2025\n09:00 A.M.", "Ana García", "Servicios Generales", "Mantenimiento de equipos", "S/85.00")
        );

        List<TransactionTotal> egresosTotals = Arrays.asList(
                new TransactionTotal("TOTAL EGRESOS (Efectivo y Tarjeta)", "S/ 0.00", R.color.danger_5),
                new TransactionTotal("TOTAL EGRESOS POR DELIVERY (En línea, transferencia, Yape, Plin)", "S/ 0.00", R.color.danger_5)
        );

        TransactionSection egresosTableSection = new TransactionSection(
                "Movimientos de egresos extra",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_down,
                egresosTableItems,
                egresosTotals,
                TransactionSection.ColumnType.FIVE_COLUMNS,
                "No hay egresos registrados"
        );

        transactionSectionIngresos.setData(ingresosSection);
        transactionSectionEgresos.setData(egresosSection);
        transactionSectionCorrelativos.setData(correlativosSection);
        transactionSectionHechas.setData(hechosSection);
        transactionSectionAnuladas.setData(anuladasSection);
        transactionSectionTable.setData(tableSection);
        transactionSectionEgresosTable.setData(egresosTableSection);

        // Configurar cards expandibles
        ExpandableCardView expandableCardVentas = findViewById(R.id.expandableCardVentas);
        TransactionSectionView transactionSectionDelivery = findViewById(R.id.transactionSectionDelivery);

        // Datos para "Ingresos en efectivo/tarjetas por ventas"
        List<ExpandableCardView.ExpandableItem> ventasItems = Arrays.asList(
                new ExpandableCardView.ExpandableItem("Soles", "0", "S/546.00"),
                new ExpandableCardView.ExpandableItem("POS", "0", "S/4,027.60"),
                new ExpandableCardView.ExpandableItem("Pago en linea", "0", "S/9.80"),
                new ExpandableCardView.ExpandableItem("Pago por transferencia", "0", "S/1,331.30"),
                new ExpandableCardView.ExpandableItem("Pago por Yape", "0", "S/2,405.38"),
                new ExpandableCardView.ExpandableItem("Pago por Plin", "-", "S/195.91"),
                new ExpandableCardView.ExpandableItem("Pago por Niubiz", "-", "S/228.22"),
                new ExpandableCardView.ExpandableItem("Pago por Vale", "0", "S/88.46"),
                new ExpandableCardView.ExpandableItem("Pago por Mercado pago", "-", "S/112.72"),
                new ExpandableCardView.ExpandableItem("Pago por Fpay", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago por POS Niubiz", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago por Izipay", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago por Qr Izipay", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago por Databank", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago por Qr Databank", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Cupón Yape", "-", "S/0.00"),
                new ExpandableCardView.ExpandableItem("Pago Otros", "-", "S/0.00")
        );

        ExpandableCardView.ExpandableCardData ventasData = new ExpandableCardView.ExpandableCardData(
                R.drawable.icon_svg_arrow_trend_up_blue, R.color.info, "Ingresos en efectivo/tarjetas por ventas"
        )
                .setHeaders("Moneda o tarjeta", "Retención", "Monto")
                .setTotal("TOTAL INGRESOS EFECTIVO/TARJETAS POR VENTAS", "S/9,027.60")
                .setItems(ventasItems)
                .setCollapsedItemCount(8);

        expandableCardVentas.setData(ventasData);

        // Datos para "Ingresos por canales de delivery" (card normal con 4 columnas)
        List<TransactionItem> deliveryItems = Arrays.asList(
                TransactionItem.createFourColumns("Ecommerce Cfi", "0", "S/546.00", "S/546.00"),
                TransactionItem.createFourColumns("Ecommerce Android", "0", "S/4,027.60", "S/4,027.60"),
                TransactionItem.createFourColumns("Rappi", "0", "S/1,331.30", "S/1,331.30"),
                TransactionItem.createFourColumns("Pedidos Ya", "0", "S/2,405.38", "S/2,405.38"),
                TransactionItem.createFourColumns("Justo", "0", "S/228.22", "S/228.22"),
                TransactionItem.createFourColumns("Didi Food", "0", "S/88.46", "S/88.46"),
                TransactionItem.createFourColumns("Ecommerce", "0", "S/1,331.30", "S/1,331.30"),
                TransactionItem.createFourColumns("Ecommerce IOS", "0", "S/195.91", "S/195.91")
        );

        TransactionSection deliverySection = new TransactionSection(
                "Ingresos por canales de delivery",
                R.color.info,
                R.drawable.icon_svg_mobile,
                deliveryItems,
                "TOTAL INGRESOS POR CANALES",
                "S/9,027.60",
                R.color.info_5
        );
        deliverySection.setColumnType(TransactionSection.ColumnType.FOUR_COLUMNS);
        transactionSectionDelivery.setData(deliverySection);

        // Configurar secciones de 2 columnas
        TransactionSectionView transactionSectionPropinas = findViewById(R.id.transactionSectionPropinas);
        TransactionSectionView transactionSectionCreditos = findViewById(R.id.transactionSectionCreditos);

        // Datos para "Ingresos por propinas" (2 columnas con header de tabla)
        List<TransactionItem> propinasItems = Arrays.asList(
                new TransactionItem("POS", "S/30.00")
        );

        List<TransactionTotal> propinasTotals = Arrays.asList(
                new TransactionTotal("TOTAL INGRESOS PROPINAS", "S/30.00", R.color.info_5)
        );

        TransactionSection propinasSection = new TransactionSection(
                "Ingresos por propinas",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                propinasItems,
                propinasTotals,
                TransactionSection.ColumnType.TWO_COLUMNS
        );

        transactionSectionPropinas.setData(propinasSection);

        // Datos para "Ingresos por créditos cobrados" (2 columnas con header de tabla - vacío)
        List<TransactionItem> creditosItems = new ArrayList<>();

        TransactionSection creditosSection = new TransactionSection(
                "Ingresos por créditos cobrados",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                creditosItems,
                new ArrayList<>(),
                TransactionSection.ColumnType.TWO_COLUMNS,
                "No hay ingresos por créditos cobrados"
        );

        transactionSectionCreditos.setData(creditosSection);

        // Configurar gráficos y pestañas
        setupChartTabs();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupChartTabs() {
        TextView tabGrafico1 = findViewById(R.id.tabGrafico1);
        TextView tabGrafico2 = findViewById(R.id.tabGrafico2);
        LinearLayout grafico1Container = findViewById(R.id.grafico1Container);
        LinearLayout grafico2Container = findViewById(R.id.grafico2Container);

        // Click en pestaña 1 (Top ventas)
        tabGrafico1.setOnClickListener(v -> {
            tabGrafico1.setTextColor(getResources().getColor(R.color.info, null));
            tabGrafico2.setTextColor(getResources().getColor(R.color.text_65, null));

            grafico1Container.setVisibility(View.VISIBLE);
            grafico2Container.setVisibility(View.GONE);
        });

        // Click en pestaña 2 (Productos estrella)
        tabGrafico2.setOnClickListener(v -> {
            tabGrafico2.setTextColor(getResources().getColor(R.color.info, null));
            tabGrafico1.setTextColor(getResources().getColor(R.color.text_65, null));

            grafico1Container.setVisibility(View.GONE);
            grafico2Container.setVisibility(View.VISIBLE);
        });

        // Configurar gráficos
        setupCharts();
    }

    private void setupCharts() {
        setupCategoriesBarChart();
        setupProductsPieChart();
    }

    private void setupCategoriesBarChart() {
        com.github.mikephil.charting.charts.BarChart barChart = findViewById(R.id.barChartCategorias);
        LinearLayout listaCategorias = findViewById(R.id.listaCategorias);

        // Configurar el gráfico de barras vertical
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setExtraBottomOffset(15f);
        barChart.setExtraTopOffset(10f);
        barChart.setExtraLeftOffset(10f);
        barChart.setExtraRightOffset(10f);

        // Configurar eje X (abajo - para los porcentajes)
        com.github.mikephil.charting.components.XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getResources().getColor(R.color.info, null));
        xAxis.setTextSize(11f);
        xAxis.setLabelCount(5);

        // Etiquetas personalizadas para porcentajes
        final String[] labels = {"95,8%", "76,12%", "59,20%", "25,20%", "10,19%"};
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));

        // Configurar eje Y (izquierda - oculto)
        com.github.mikephil.charting.components.YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);

        // Datos de ejemplo para categorías (porcentajes como valores)
        java.util.ArrayList<com.github.mikephil.charting.data.BarEntry> entries = new java.util.ArrayList<>();
        entries.add(new com.github.mikephil.charting.data.BarEntry(0f, 95.8f));
        entries.add(new com.github.mikephil.charting.data.BarEntry(1f, 76.12f));
        entries.add(new com.github.mikephil.charting.data.BarEntry(2f, 59.20f));
        entries.add(new com.github.mikephil.charting.data.BarEntry(3f, 25.20f));
        entries.add(new com.github.mikephil.charting.data.BarEntry(4f, 10.19f));

        com.github.mikephil.charting.data.BarDataSet dataSet = new com.github.mikephil.charting.data.BarDataSet(entries, "Categorías");
        dataSet.setColors(
                getResources().getColor(R.color.info, null),
                getResources().getColor(R.color.primary, null),
                getResources().getColor(R.color.warning, null),
                getResources().getColor(R.color.secondary_1, null),
                getResources().getColor(R.color.secondary_3, null)
        );
        dataSet.setDrawValues(false);

        com.github.mikephil.charting.data.BarData data = new com.github.mikephil.charting.data.BarData(dataSet);
        data.setBarWidth(0.4f); // Barras más delgadas
        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(1000);

        // Crear lista de categorías
        addCategoryItem(listaCategorias, "Bebidas con alcohol", "S/989.09", R.color.info);
        addCategoryItem(listaCategorias, "Bebidas sin alcohol", "S/497.25", R.color.primary);
        addCategoryItem(listaCategorias, "Menú", "S/352.32", R.color.warning);
        addCategoryItem(listaCategorias, "Platos a la carta", "S/170.87", R.color.secondary_1);
        addCategoryItem(listaCategorias, "Postres", "S/90.25", R.color.secondary_3);
    }

    private void setupProductsPieChart() {
        com.github.mikephil.charting.charts.PieChart pieChart = findViewById(R.id.pieChartProductos);
        LinearLayout listaProductos = findViewById(R.id.listaProductos);

        // Configurar el gráfico - sin agujero en el centro (pie chart sólido)
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.white, null));
        pieChart.setEntryLabelTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);

        // Datos de ejemplo para productos
        java.util.ArrayList<com.github.mikephil.charting.data.PieEntry> entries = new java.util.ArrayList<>();
        entries.add(new com.github.mikephil.charting.data.PieEntry(55f, "55%"));
        entries.add(new com.github.mikephil.charting.data.PieEntry(30f, "30%"));
        entries.add(new com.github.mikephil.charting.data.PieEntry(15f, "15%"));

        com.github.mikephil.charting.data.PieDataSet dataSet = new com.github.mikephil.charting.data.PieDataSet(entries, "Productos");
        dataSet.setColors(new int[]{
                getResources().getColor(R.color.info, null),
                getResources().getColor(R.color.yellow, null),
                getResources().getColor(R.color.primary, null)
        });
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(0f);

        com.github.mikephil.charting.data.PieData data = new com.github.mikephil.charting.data.PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1000);

        // Crear lista de productos
        addProductItem(listaProductos, "Pilsen", "S/90.39", R.color.info);
        addProductItem(listaProductos, "Ensalada Cesar's", "S/40.39", R.color.yellow);
        addProductItem(listaProductos, "Torta helada", "S/10.39", R.color.primary);
    }

    private void addCategoryItem(LinearLayout container, String label, String value, int colorRes) {
        View item = getLayoutInflater().inflate(R.layout.item_category_chart, container, false);

        android.widget.ImageView colorIndicator = item.findViewById(R.id.colorIndicator);
        TextView tvLabel = item.findViewById(R.id.tvCategoryLabel);
        TextView tvValue = item.findViewById(R.id.tvCategoryValue);

        colorIndicator.setColorFilter(getResources().getColor(colorRes, null));
        tvLabel.setText(label);
        tvValue.setText(value);

        container.addView(item);
    }

    private void addProductItem(LinearLayout container, String label, String value, int colorRes) {
        View item = getLayoutInflater().inflate(R.layout.item_product_chart, container, false);

        android.widget.ImageView colorIndicator = item.findViewById(R.id.colorIndicator);
        TextView tvLabel = item.findViewById(R.id.tvProductLabel);
        TextView tvValue = item.findViewById(R.id.tvProductValue);

        colorIndicator.setColorFilter(getResources().getColor(colorRes, null));
        tvLabel.setText(label);
        tvValue.setText(value);

        container.addView(item);
    }
}





