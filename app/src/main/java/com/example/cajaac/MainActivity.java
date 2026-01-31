package com.example.cajaac;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        List<TransactionItem> ingresosTableItems = Arrays.asList(
                new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                        "Ingreso por confirmación de Delivery #31765 con forma de pago En línea", "S/20.00"),
                new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                        "Ingreso por confirmación de Delivery #31765 con forma de pago En línea", "S/20.00")
        );

        // Para probar sin items, cambia la línea anterior por:
        // List<TransactionItem> tableItems = Arrays.asList();

        List<TransactionTotal> ingresosTotals = Arrays.asList(
                new TransactionTotal("TOTAL INGRESOS (Efectivo y Tarjeta)", "S/ 0.00", R.color.info_5),
                new TransactionTotal("TOTAL INGRESOS POR DELIVERY (En línea, transferencia, Yape, Plin)", "S/ 40.00", R.color.info_5)
        );

        TransactionSection ingresosTableSection = new TransactionSection(
                "Movimientos de ingresos extra",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                ingresosTableItems,
                ingresosTotals,
                TransactionSection.ColumnType.TABLE,
                "No hay ingresos registrados"
        );

        // Tabla de egresos (5 columnas: Fecha, Usuario, Recibido de, Concepto, Monto)
        // Lista vacía para mostrar el mensaje
        List<TransactionItem> egresosTableItems = Arrays.asList(
                new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                        "Juan Pérez", "Pago de servicios", "S/150.00"),
                new TransactionItem("00/00/0000\n05:30 P.M.", "María López",
                        "Proveedor ABC", "Compra de insumos", "S/320.00"),
                new TransactionItem("00/00/0000\n06:15 P.M.", "Carlos Ramírez",
                        "Servicio Delivery", "Pago de comisión", "S/85.00")
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
        transactionSectionTable.setData(ingresosTableSection);
        transactionSectionEgresosTable.setData(egresosTableSection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}





