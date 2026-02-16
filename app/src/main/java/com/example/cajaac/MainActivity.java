package com.example.cajaac;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cajaac.adapters.CustomSpinnerAdapter;
import com.example.cajaac.models.DatosCaja;
import com.example.cajaac.models.ModalButton;
import com.example.cajaac.models.ModalConfig;
import com.example.cajaac.models.TransactionItem;
import com.example.cajaac.models.TransactionSection;
import com.example.cajaac.models.TransactionTotal;
import com.example.cajaac.ui.BaseModalFragment;
import com.example.cajaac.ui.ExpandableCardView;
import com.example.cajaac.ui.TransactionSectionView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerProductos, spinnerPeriodo;

    // Referencias a los TextViews de datos de caja
    private TextView tvNumeroCaja;
    private TextView tvTotalCaja;
    private TextView tvNombreUsuario;
    private TextView tvMontoApertura;
    private TextView tvTurno;
    private TextView tvFechaApertura;
    private TextView tvFechaResumen;
    private TextView tvMontoEfectivo;
    private TextView tvMontoTarjetas;
    private TextView tvTotalCajaResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. ESTO ES LO ÚNICO QUE SE EJECUTA INMEDIATAMENTE
        // El usuario verá el esqueleto principal al instante (0.1 segundos)
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(android.graphics.Color.BLACK);

        initializeCajaViews();
        setupCerrarCajaButton();
        setupImprimirCierre();

        // 2. CREAMOS EL HANDLER PARA DIFERIR LA CARGA PESADA DE LA UI
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // Usamos post() para enviar esto a la cola.
        // Android primero dibujará la vista, y justo después ejecutará este bloque.
        mainHandler.post(() -> {
            // Carga de las listas estáticas
            setupTransactionSections();
            setupExpandableCards();
            setupDeliverySections();
            setupPropinasCreditosSections();
            initSpinners();

            // 3. LOS GRÁFICOS Y LOS INFLATES SON LO MÁS PESADO
            // Le damos 100ms de respiro al procesador para que la animación
            // de apertura de la app sea 100% fluida antes de empezar a inflar los 17 items.
            mainHandler.postDelayed(() -> {
                setupChartTabs();
            }, 100);
        });

        // 4. EL TRABAJO DE BASE DE DATOS / API DEBE IR EN UN HILO SECUNDARIO
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // --- HILO SECUNDARIO (Background) ---
            // Aquí puedes hacer Thread.sleep, consultas a BD, o llamadas Retrofit.
            // Simulamos que traer los datos tarda un poco...
            DatosCaja datos = obtenerDatosPesadosSimulados();

            // --- VOLVEMOS AL HILO PRINCIPAL PARA PINTAR LOS TEXTOS ---
            mainHandler.post(() -> {
                actualizarDatosCaja(datos);
            });
        });
    }

    private DatosCaja obtenerDatosPesadosSimulados() {
        return new DatosCaja(
                "Caja N°02",
                "S/ 7,937.79",
                "Mónica González",
                "S/10.00",
                "Mañana",
                "30/06/22 - 04:00 P.M.",
                "Resumen hasta 27/12/2025, 09:48 AM",
                "S/ 1,062.70",
                "S/ 6,875.09"
        );
    }

    /**
     * Inicializa las referencias a los TextViews de datos de caja
     */
    private void initializeCajaViews() {
        tvNumeroCaja = findViewById(R.id.tvNumeroCaja);
        tvTotalCaja = findViewById(R.id.tvTotalCaja);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvMontoApertura = findViewById(R.id.tvMontoApertura);
        tvTurno = findViewById(R.id.tvTurno);
        tvFechaApertura = findViewById(R.id.tvFechaApertura);
        tvFechaResumen = findViewById(R.id.tvFechaResumen);
        tvMontoEfectivo = findViewById(R.id.tvMontoEfectivo);
        tvMontoTarjetas = findViewById(R.id.tvMontoTarjetas);
        tvTotalCajaResumen = findViewById(R.id.tvTotalCajaResumen);
    }

    /**
     * Configura el botón para cerrar caja y abrir, cuadre de stock
     */
    private void setupCerrarCajaButton() {
        View btnCerrarCaja = findViewById(R.id.btnCuadreStock);
        btnCerrarCaja.setOnClickListener(v -> showCuadreStockModal());
    }
    private void setupImprimirCierre() {
        View btnImprimirCierre = findViewById(R.id.btnCerrarCaja);
        btnImprimirCierre.setOnClickListener(v -> showImprimirCierreModal());
    }

    private void showCuadreStockModal() {
        // Configurar el modal
        ModalConfig config = new ModalConfig()
                .setTitle("Cuadre de stock pendiente")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_cuadre_stock)
                .setHeightPercent(0.9f) // 90% de la pantalla
                .setShowCloseButton(true)
                .setCancelable(true);

        BaseModalFragment modal = BaseModalFragment.newInstance(config);

        config.addButton(new ModalButton(
                "Cancelar",
                R.drawable.icon_svg_circle_xmark,
                R.color.text_85,
                R.color.text_85,
                () -> modal.dismiss()
        ))
        .addButton(new ModalButton(
                "Guardar borrador",
                R.drawable.icon_svg_save_disk,
                R.color.info,
                R.color.info,
                () -> modal.dismiss()
        ))
        .addButton(new ModalButton(
                "Guardar cuadre",
                R.drawable.icon_svg_save_disk_solid,
                ModalButton.ButtonType.INFO,
                () -> modal.dismiss()
        ));

        // Crear y mostrar el modal
        modal.show(getSupportFragmentManager(), "cuadre_stock");
    }
    private void showImprimirCierreModal() {
        ModalConfig config = new ModalConfig()
                .setTitle("Imprimir cierre")
                .setTitleIconResId(R.drawable.icon_svg_print_blue)
                .setContentLayoutResId(R.layout.content_imprimir_cierre)
                .setHeightPercent(0.9f)
                .setHorizontalPaddingRes(R.dimen.modal_padding_horizontal) // Padding más pequeño para modal más ancho
                .setShowCloseButton(true)
                .setCancelable(true);

        BaseModalFragment modal = BaseModalFragment.newInstance(config);

        config.addButton(new ModalButton(
                "Cancelar",
                R.drawable.icon_svg_circle_xmark,
                R.color.text_85,
                R.color.text_85,
                () -> modal.dismiss()
        ))
        .addButton(new ModalButton(
                "Imprimir",
                R.drawable.icon_svg_printer_solid,
                ModalButton.ButtonType.INFO,
                () -> modal.dismiss()
        ));

        modal.show(getSupportFragmentManager(), "imprimir_cierre");
    }

    /**
     * Actualiza la UI con los datos de la caja
     * @param datos Objeto con los datos de la caja
     */
    public void actualizarDatosCaja(DatosCaja datos) {
        if (datos == null) return;

        if (datos.getNumeroCaja() != null) {
            tvNumeroCaja.setText(datos.getNumeroCaja());
        }
        if (datos.getTotalCaja() != null) {
            tvTotalCaja.setText(datos.getTotalCaja());
            tvTotalCajaResumen.setText(datos.getTotalCaja());
        }
        if (datos.getNombreUsuario() != null) {
            tvNombreUsuario.setText(datos.getNombreUsuario());
        }
        if (datos.getMontoApertura() != null) {
            tvMontoApertura.setText(datos.getMontoApertura());
        }
        if (datos.getTurno() != null) {
            tvTurno.setText(datos.getTurno());
        }
        if (datos.getFechaApertura() != null) {
            tvFechaApertura.setText(datos.getFechaApertura());
        }
        if (datos.getFechaResumen() != null) {
            tvFechaResumen.setText(datos.getFechaResumen());
        }
        if (datos.getMontoEfectivo() != null) {
            tvMontoEfectivo.setText(datos.getMontoEfectivo());
        }
        if (datos.getMontoTarjetas() != null) {
            tvMontoTarjetas.setText(datos.getMontoTarjetas());
        }
    }

    /**
     * Método de ejemplo para cargar datos de caja
     * Aquí conectarías con tu backend/API
     */

    // TODO: Método para obtener datos del backend
    /*
    private void fetchDatosFromBackend() {
        // Ejemplo con Retrofit, Volley, etc.
        // api.getDatosCaja().enqueue(new Callback<DatosCaja>() {
        //     @Override
        //     public void onResponse(Call<DatosCaja> call, Response<DatosCaja> response) {
        //         if (response.isSuccessful() && response.body() != null) {
        //             actualizarDatosCaja(response.body());
        //         }
        //     }
        //
        //     @Override
        //     public void onFailure(Call<DatosCaja> call, Throwable t) {
        //         // Manejar error
        //     }
        // });
    }
    */

    private void setupTransactionSections() {
        TransactionSectionView transactionSectionIngresos = findViewById(R.id.transactionSectionIngresos);
        TransactionSectionView transactionSectionEgresos = findViewById(R.id.transactionSectionEgresos);
        TransactionSectionView transactionSectionCorrelativos = findViewById(R.id.transactionSectionCorrelativos);
        TransactionSectionView transactionSectionHechas = findViewById(R.id.transactionSectionHechas);
        TransactionSectionView transactionSectionAnuladas = findViewById(R.id.transactionSectionAnuladas);
        TransactionSectionView transactionSectionTable = findViewById(R.id.transactionSectionTable);
        TransactionSectionView transactionSectionEgresosTable = findViewById(R.id.transactionSectionEgresosTable);

        transactionSectionIngresos.setData(createIngresosSection());
        transactionSectionEgresos.setData(createEgresosSection());
        transactionSectionCorrelativos.setData(createCorrelativosSection());
        transactionSectionHechas.setData(createTransaccionesHechasSection());
        transactionSectionAnuladas.setData(createTransaccionesAnuladasSection());
        transactionSectionTable.setData(createMovimientosIngresosSection());
        transactionSectionEgresosTable.setData(createMovimientosEgresosSection());
    }

    private TransactionSection createIngresosSection() {
        List<TransactionItem> items = Arrays.asList(
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

        return new TransactionSection(
                "Ingresos",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                items,
                "TOTAL INGRESOS",
                "S/ 7,937.79",
                R.color.primary_5
        );
    }

    private TransactionSection createEgresosSection() {
        List<TransactionItem> items = Arrays.asList(
                new TransactionItem("Egresos extra", "S/ 0.00"),
                new TransactionItem("Compras", "S/ 0.00"),
                new TransactionItem("Pag. de compras al crédito", "S/ 0.00"),
                new TransactionItem("Descuentos en soles", "S/ 0.00"),
                new TransactionItem("Descuentos en dolares", "S/ 0.00"),
                new TransactionItem("Descuentos en euros", "S/ 0.00")
        );

        return new TransactionSection(
                "Egresos",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_down,
                items,
                "TOTAL EGRESOS",
                "S/ 0.00",
                R.color.danger_5
        );
    }

    private TransactionSection createCorrelativosSection() {
        List<TransactionItem> items = Arrays.asList(
                new TransactionItem("Facturas", "Serie F002 del 00002266 al 00002267"),
                new TransactionItem("Boletas", "Serie B002 del 00130834 al 00131035"),
                new TransactionItem("Nota de Ventas", "-"),
                new TransactionItem("Nota de credito Boletas", "-"),
                new TransactionItem("Nota de debito Boletas", "-"),
                new TransactionItem("Nota de credito Facturas", "-"),
                new TransactionItem("Nota de debito Facturas", "-")
        );

        // Establecer tamaño de texto personalizado de 10sp para todos los items
        for (TransactionItem item : items) {
            item.setCustomTextSize(10);
        }

        return new TransactionSection(
                "Correlativos usados",
                R.color.info,
                R.drawable.icon_svg_list,
                items,
                false,
                TransactionSection.ColumnType.DEFAULT
        );
    }

    private TransactionSection createTransaccionesHechasSection() {
        List<TransactionItem> items = Arrays.asList(
                new TransactionItem("N° Facturas", "0", "S/1,062.70"),
                new TransactionItem("N° Boletas", "0", "S/0.00"),
                new TransactionItem("N° Nota de Ventas", "0", "S/0.00"),
                new TransactionItem("N° Nota de credito Boletas", "0", "S/6,875.09"),
                new TransactionItem("N° Nota de debito Boletas", "0", "S/0.00"),
                new TransactionItem("N° Nota de credito Facturas", "0", "S/0.00"),
                new TransactionItem("N° Nota de debito Facturas", "0", "S/0.00")
        );

        return new TransactionSection(
                "Transacciones hechas",
                R.color.info,
                R.drawable.icon_svg_arrow_left_arrow_right,
                items,
                "TOTAL TRANSACCIONES HECHAS",
                "0",
                "S/8,945.41",
                R.color.info_5,
                true,
                TransactionSection.ColumnType.THREE_COLUMNS
        );
    }

    private TransactionSection createTransaccionesAnuladasSection() {
        List<TransactionItem> items = Arrays.asList(
                new TransactionItem("N° Facturas", "0"),
                new TransactionItem("N° Boletas", "0"),
                new TransactionItem("N° Nota de Ventas", "0"),
                new TransactionItem("N° Nota de credito Boletas", "0"),
                new TransactionItem("N° Nota de debito Boletas", "0"),
                new TransactionItem("N° Nota de credito Facturas", "0"),
                new TransactionItem("N° Nota de debito Facturas", "0")
        );

        return new TransactionSection(
                "Transacciones anuladas",
                R.color.info,
                R.drawable.icon_svg_circle_xmark,
                items,
                "TOTAL TRANSACCIONES ANULADAS",
                "0",
                R.color.info_5
        );
    }

    private TransactionSection createMovimientosIngresosSection() {
        // Crear items
        TransactionItem item1 = new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                "Ingreso por confirmación de Delivery #31765 con forma de pago en línea", "S/20.00");
        item1.setDeliveryCode("#31776");
        item1.setPaymentMethod("en línea");

        TransactionItem item2 = new TransactionItem("00/00/0000\n04:05 P.M.", "Jean Pierre Santillán García",
                "Ingreso por confirmación de Delivery #31765 con forma de pago en línea", "S/20.00");
        item2.setDeliveryCode("#31765");
        item2.setPaymentMethod("en línea");

        List<TransactionItem> items = Arrays.asList(item1, item2);

        List<TransactionTotal> totals = Arrays.asList(
                new TransactionTotal("TOTAL INGRESOS (Efectivo y Tarjeta)", "S/ 0.00", R.color.info_5),
                new TransactionTotal("TOTAL INGRESOS POR DELIVERY (En línea, transferencia, Yape, Plin)", "S/ 40.00", R.color.info_5)
        );

        return new TransactionSection(
                "Movimientos de ingresos extra",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                items,
                totals,
                TransactionSection.ColumnType.TABLE,
                "No hay ingresos registrados"
        );
    }

    private TransactionSection createMovimientosEgresosSection() {
        List<TransactionItem> items = Arrays.asList(
//                new TransactionItem("15/01/2025\n10:30 A.M.", "María López", "Proveedor ABC", "Compra de insumos", "S/150.00"),
//                new TransactionItem("15/01/2025\n02:15 P.M.", "Carlos Ruiz", "Distribuidora XYZ", "Pago de factura pendiente", "S/320.50"),
//                new TransactionItem("16/01/2025\n09:00 A.M.", "Ana García", "Servicios Generales", "Mantenimiento de equipos", "S/85.00")
        );

        List<TransactionTotal> totals = Arrays.asList(
                new TransactionTotal("TOTAL EGRESOS (Efectivo y Tarjeta)", "S/ 0.00", R.color.info_5),
                new TransactionTotal("TOTAL EGRESOS POR DELIVERY (En línea, transferencia, Yape, Plin)", "S/ 0.00", R.color.info_5)
        );

        return new TransactionSection(
                "Movimientos de egresos extra",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_down,
                items,
                totals,
                TransactionSection.ColumnType.FIVE_COLUMNS,
                "No hay egresos registrados"
        );
    }

    private void setupExpandableCards() {
        ExpandableCardView expandableCardVentas = findViewById(R.id.expandableCardVentas);

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
    }

    private void setupDeliverySections() {
        TransactionSectionView transactionSectionDelivery = findViewById(R.id.transactionSectionDelivery);

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
    }

    private void setupPropinasCreditosSections() {
        TransactionSectionView transactionSectionPropinas = findViewById(R.id.transactionSectionPropinas);
        TransactionSectionView transactionSectionCreditos = findViewById(R.id.transactionSectionCreditos);

        // Propinas
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
                TransactionSection.ColumnType.TWO_COLUMNS,
                "No hay ingresos por propinas"
        );

        transactionSectionPropinas.setData(propinasSection);

        // Créditos
        List<TransactionItem> creditosItems = Arrays.asList(
//                new TransactionItem("POS", "S/30.00")
        );

        List<TransactionTotal> creditosTotals = Arrays.asList(
                new TransactionTotal("TOTAL INGRESOS CRÉDITOS", "S/30.00", R.color.info_5)
        );


        TransactionSection creditosSection = new TransactionSection(
                "Ingresos por créditos cobrados",
                R.color.info,
                R.drawable.icon_svg_arrow_trend_up_blue,
                creditosItems,
                creditosTotals,
                TransactionSection.ColumnType.TWO_COLUMNS,
                "No hay ingresos por créditos cobrados"
        );

        transactionSectionCreditos.setData(creditosSection);
    }

    private void setupChartTabs() {
        android.view.ViewGroup tabGrafico1 = findViewById(R.id.tabGrafico1);
        android.view.ViewGroup tabGrafico2 = findViewById(R.id.tabGrafico2);
        androidx.constraintlayout.widget.ConstraintLayout grafico1Container = findViewById(R.id.grafico1Container);
        androidx.constraintlayout.widget.ConstraintLayout grafico2Container = findViewById(R.id.grafico2Container);

        // Click en pestaña 1 (Top ventas)
        tabGrafico1.setOnClickListener(v -> {
            updateTabStyle(tabGrafico1, true);
            updateTabStyle(tabGrafico2, false);

            grafico1Container.setVisibility(View.VISIBLE);
            grafico2Container.setVisibility(View.GONE);
        });

        // Clic en pestaña 2 (Productos estrella)
        tabGrafico2.setOnClickListener(v -> {
            updateTabStyle(tabGrafico2, true);
            updateTabStyle(tabGrafico1, false);

            grafico1Container.setVisibility(View.GONE);
            grafico2Container.setVisibility(View.VISIBLE);
        });

        // Configurar gráficos
        setupCharts();
    }

    private void updateTabStyle(android.view.ViewGroup tab, boolean isSelected) {
        int color = getResources().getColor(isSelected ? R.color.info : R.color.text_85, null);

        // Cambiar fondo del contenedor
        if (isSelected) {
            tab.setBackgroundResource(R.drawable.tab_indicator_background);
        } else {
            tab.setBackgroundResource(R.drawable.tab_indicator_background_unselected);
        }

        // Iterar sobre hijos para cambiar colores de texto e iconos
        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            } else if (child instanceof android.widget.ImageView) {
                ((android.widget.ImageView) child).setColorFilter(color);
            }
        }
    }

    private void setupCharts() {
        setupCategoriesBarChart();
        setupProductsPieChart();
        setupProductosEstrella();
        setupNestedScrollBehavior();
    }

    private void initSpinners() {
        spinnerProductos = findViewById(R.id.spinnerProductos);
        spinnerPeriodo = findViewById(R.id.spinnerPeriodo);

        // Datos para spinner de productos
        String[] productos = {
                "Productos", "Insumos", "Recetas y subrecetas"
        };

        // Datos para spinner de período
        String[] periodos = {
                "Últimos 7 días", "Últimos 15 días", "Último mes", "Últimos 3 meses", "Este año"
        };

        // Configurar adapters personalizados
        CustomSpinnerAdapter adapterProductos = new CustomSpinnerAdapter(this, productos);
        spinnerProductos.setAdapter(adapterProductos);

        CustomSpinnerAdapter adapterPeriodos = new CustomSpinnerAdapter(this, periodos);
        spinnerPeriodo.setAdapter(adapterPeriodos);

        // Establecer valores por defecto
        spinnerProductos.setSelection(0);
        spinnerPeriodo.setSelection(0);
    }
    private void setupNestedScrollBehavior() {
        androidx.core.widget.NestedScrollView nestedScrollProductos = findViewById(R.id.nestedScrollProductos);
        android.widget.ScrollView mainScrollView = findViewById(R.id.mainScrollView);

        if (nestedScrollProductos != null && mainScrollView != null) {
            nestedScrollProductos.setOnTouchListener((v, event) -> {
                int action = event.getAction();

                switch (action) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        // Deshabilitar COMPLETAMENTE el scroll del padre
                        disableParentScroll(v, true);
                        break;

                    case android.view.MotionEvent.ACTION_UP:
                    case android.view.MotionEvent.ACTION_CANCEL:
                        // Re-habilitar el scroll del padre
                        disableParentScroll(v, false);
                        break;
                }

                return false;
            });
        }
    }

    private void disableParentScroll(View view, boolean disable) {
        android.view.ViewParent parent = view.getParent();
        while (parent != null) {
            if (parent instanceof android.widget.ScrollView) {
                if (disable) {
                    // Deshabilitar scroll completamente
                    ((android.widget.ScrollView) parent).setOnTouchListener((v, event) -> true);
                } else {
                    // Rehabilitar scroll
                    ((android.widget.ScrollView) parent).setOnTouchListener(null);
                }
                break;
            }
            parent = parent.getParent();
        }
        view.getParent().requestDisallowInterceptTouchEvent(disable);
    }

    private void setupCategoriesBarChart() {
        com.example.cajaac.ui.CustomBarChartView barChart = findViewById(R.id.barChartCategorias);
        LinearLayout listaCategorias = findViewById(R.id.listaCategorias);
        listaCategorias.removeAllViews(); // Limpiar lista previa

        // Crear datos para las barras
        java.util.List<com.example.cajaac.ui.CustomBarChartView.BarItem> items = new java.util.ArrayList<>();
        items.add(new com.example.cajaac.ui.CustomBarChartView.BarItem("95.8%", 95.8f, getResources().getColor(R.color.info, null)));
        items.add(new com.example.cajaac.ui.CustomBarChartView.BarItem("76.12%", 76.12f, getResources().getColor(R.color.primary, null)));
        items.add(new com.example.cajaac.ui.CustomBarChartView.BarItem("59.20%", 59.20f, getResources().getColor(R.color.warning, null)));
        items.add(new com.example.cajaac.ui.CustomBarChartView.BarItem("25.20%", 25.20f, getResources().getColor(R.color.secondary_1, null)));
        items.add(new com.example.cajaac.ui.CustomBarChartView.BarItem("10.19%", 10.19f, getResources().getColor(R.color.secondary_3, null)));

        barChart.setBars(items);

        // Crear lista de categorías (colores coinciden con las barras)
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
        entries.add(new com.github.mikephil.charting.data.PieEntry(15f, "15%"));
        entries.add(new com.github.mikephil.charting.data.PieEntry(30f, "30%"));
        entries.add(new com.github.mikephil.charting.data.PieEntry(55f, "55%"));

        com.github.mikephil.charting.data.PieDataSet dataSet = new com.github.mikephil.charting.data.PieDataSet(entries, "Productos");
        dataSet.setColors(new int[]{
                getResources().getColor(R.color.primary, null),
                getResources().getColor(R.color.yellow, null),
                getResources().getColor(R.color.info, null)

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

    private void setupProductosEstrella() {
        // Referencias a las vistas
        TextView tvNombre = findViewById(R.id.tvProductoEstrellaNombre);
        TextView tvCategoria = findViewById(R.id.tvProductoEstrellaCategoria);
        TextView tvVentas = findViewById(R.id.tvProductoEstrellaVentas);
        TextView tvPorcentaje = findViewById(R.id.tvProductoEstrellaPorcentaje);
        LinearLayout listaOtros = findViewById(R.id.listaOtrosProductosEstrella);

        // Configurar producto #1
        tvNombre.setText("Ceviche de conchas negras");
        tvCategoria.setText("Fuente");
        tvVentas.setText("10,000");
        tvPorcentaje.setText("90,45%");

        // Limpiar y agregar lista de otros productos
        listaOtros.removeAllViews();

        addProductoEstrellaItem(listaOtros, "#2", "P", "Cerveza Pilsen", "650ml", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#3", "R", "Lomo saltado", "Plato", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#4", "P", "Ronda criolla familiar", "Fuente", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#5", "R", "Tallarines verdes con chuleta...", "Personal", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#6", "I", "Pollo", "Entero", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#7", "I", "Arroz", "Kg", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#8", "P", "Gaseosa Inca Kola", "1.5L", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#9", "R", "Ceviche mixto", "Fuente", "000 VENTAS", "52,45%", R.color.primary);
        addProductoEstrellaItem(listaOtros, "#10", "I", "Cebolla", "Kg", "000 VENTAS", "52,45%", R.color.primary);
    }

    private void addProductoEstrellaItem(LinearLayout container, String ranking, String letra,
                                          String nombre, String categoria, String ventas,
                                          String porcentaje, int colorPorcentaje) {
        View item = getLayoutInflater().inflate(R.layout.item_producto_estrella, container, false);

        TextView tvRanking = item.findViewById(R.id.tvRanking);
        TextView tvLetraInicial = item.findViewById(R.id.tvLetraInicial);
        TextView tvNombre = item.findViewById(R.id.tvNombreProducto);
        TextView tvCategoria = item.findViewById(R.id.tvCategoriaProducto);
        TextView tvVentas = item.findViewById(R.id.tvVentasProducto);
        TextView tvPorcentaje = item.findViewById(R.id.tvPorcentajeProducto);

        tvRanking.setText(ranking);
        tvLetraInicial.setText(letra);
        tvNombre.setText(nombre);
        tvCategoria.setText(categoria);
        tvVentas.setText(ventas);
        tvPorcentaje.setText(porcentaje);
        tvPorcentaje.setTextColor(getResources().getColor(colorPorcentaje, null));

        // Configurar badge según el tipo (I, P, R)
        int badgeBackground;
        int badgeTextColor;

        switch (letra) {
            case "I": // Insumo
                badgeBackground = R.drawable.rectangular_badge_primary;
                badgeTextColor = R.color.primary;
                break;
            case "P": // Producto
                badgeBackground = R.drawable.rectangular_badge_info;
                badgeTextColor = R.color.info;
                break;
            case "R": // Receta
                badgeBackground = R.drawable.rectangular_badge_secondary;
                badgeTextColor = R.color.secondary;
                break;
            default: // Por defecto usar info
                badgeBackground = R.drawable.rectangular_badge_info;
                badgeTextColor = R.color.info;
                break;
        }

        tvLetraInicial.setBackgroundResource(badgeBackground);
        tvLetraInicial.setTextColor(getResources().getColor(badgeTextColor, null));

        container.addView(item);
    }
}

