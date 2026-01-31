package com.example.cajaac;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cajaac.ui.ExpandableCardView;
import com.example.cajaac.ui.TransactionSectionView;

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
        List<TransactionItem> egresosTableItems = Arrays.asList();

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
        ExpandableCardView expandableCardDelivery = findViewById(R.id.expandableCardDelivery);

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

        // Datos para "Ingresos por canales de delivery"
        List<ExpandableCardView.ExpandableItem> deliveryItems = Arrays.asList(
                new ExpandableCardView.ExpandableItem("Ecommerce Cfi", "0", "S/546.00", "S/546.00"),
                new ExpandableCardView.ExpandableItem("Ecommerce Android", "0", "S/4,027.60", "S/4,027.60"),
                new ExpandableCardView.ExpandableItem("Rappi", "0", "S/1,331.30", "S/1,331.30"),
                new ExpandableCardView.ExpandableItem("Pedidos Ya", "0", "S/2,405.38", "S/2,405.38"),
                new ExpandableCardView.ExpandableItem("Justo", "0", "S/228.22", "S/228.22"),
                new ExpandableCardView.ExpandableItem("Didi Food", "0", "S/88.46", "S/88.46"),
                new ExpandableCardView.ExpandableItem("Ecommerce", "0", "S/1,331.30", "S/1,331.30"),
                new ExpandableCardView.ExpandableItem("Ecommerce IOS", "0", "S/195.91", "S/195.91")
        );

        ExpandableCardView.ExpandableCardData deliveryData = new ExpandableCardView.ExpandableCardData(
                R.drawable.icon_svg_list, R.color.info, "Ingresos por canales de delivery"
        )
                .setHeaders("Canal de delivery", "# Operaciones", "Importe total", "Prom. x operación")
                .setTotal("TOTAL INGRESOS POR CANALES", "S/9,027.60")
                .setItems(deliveryItems)
                .setCollapsedItemCount(8);

        expandableCardDelivery.setData(deliveryData);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}





