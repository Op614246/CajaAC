package com.example.cajaac;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cajaac.models.ModalButton;
import com.example.cajaac.models.ModalConfig;
import com.example.cajaac.ui.BaseModalFragment;

/**
 * Ejemplo de cómo usar el modal reutilizable de Cuadre de Stock
 * desde una Activity
 */
public class CuadreStockExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        // Ejemplo: mostrar el modal al hacer clic en un botón
        // findViewById(R.id.btn_abrir_modal).setOnClickListener(v -> showCuadreStockModal());
    }

    /**
     * Muestra el modal de Cuadre de Stock usando el sistema reutilizable
     */
    private void showCuadreStockModal() {
        // Configurar el modal
        ModalConfig config = new ModalConfig()
                .setTitle("Cuadre de stock pendiente")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_cuadre_stock)
                .setHeightPercent(0.9f) // 90% de la pantalla
                .setShowCloseButton(true)
                .setCancelable(true)
                .addButton(new ModalButton(
                        "Cancelar",
                        R.drawable.icon_svg_circle_xmark,
                        R.color.text_85,
                        R.color.text_85,
                        () -> {
                            // Simplemente cerrar el modal
                        }
                ))
                .addButton(new ModalButton(
                        "Guardar borrador",
                        R.drawable.icon_svg_save_disk,
                        R.color.info,
                        R.color.info,
                        () -> guardarBorrador()
                ))
                .addButton(new ModalButton(
                        "Guardar cuadre",
                        R.drawable.icon_svg_save_disk_solid,
                        ModalButton.ButtonType.INFO,
                        () -> guardarCuadre()
                ));

        // Crear el modal
        BaseModalFragment modal = BaseModalFragment.newInstance(config);

        // Mostrar el modal
        modal.show(getSupportFragmentManager(), "cuadre_stock");
    }

    /**
     * IMPORTANTE: Si necesitas acceder a las vistas del contenido,
     * usa este enfoque creando una subclase de BaseModalFragment
     */
    private void showCuadreStockModalConAccesoAlContenido() {
        ModalConfig config = new ModalConfig()
                .setTitle("Cuadre de stock pendiente")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_cuadre_stock)
                .setHeightPercent(0.9f)
                .addButton(new ModalButton("Cancelar", R.drawable.icon_svg_circle_xmark,
                        R.color.text_85, R.color.text_85, () -> {}))
                .addButton(new ModalButton("Guardar borrador", R.drawable.icon_svg_save_disk,
                        R.color.info, R.color.info, () -> guardarBorrador()))
                .addButton(new ModalButton("Guardar cuadre", R.drawable.icon_svg_save_disk_solid,
                        ModalButton.ButtonType.INFO, () -> guardarCuadre()));

        // Crear subclase anónima para acceder al contenido
        BaseModalFragment modal = new BaseModalFragment() {
            @Override
            public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);

                // Ahora sí puedes acceder a las vistas del contenido
                View contentView = getContentView();
                if (contentView != null) {
                    // Ejemplo: configurar el campo de búsqueda
                    // EditText buscar = contentView.findViewById(R.id.et_buscar_productos);
                    // buscar.addTextChangedListener(new TextWatcher() { ... });
                }
            }
        };

        modal.setConfig(config);
        modal.show(getSupportFragmentManager(), "cuadre_stock");
    }

    /**
     * Lógica para guardar como borrador
     */
    private void guardarBorrador() {
        // TODO: Implementar lógica para guardar como borrador
        System.out.println("Guardando como borrador...");
    }

    /**
     * Lógica para guardar el cuadre final
     */
    private void guardarCuadre() {
        // TODO: Implementar lógica para guardar el cuadre
        System.out.println("Guardando cuadre...");
    }

    /**
     * EJEMPLO ALTERNATIVO: Modal simple de confirmación
     */
    private void showSimpleConfirmation() {
        ModalConfig config = new ModalConfig()
                .setTitle("¿Continuar?")
                .setTitleIconResId(R.drawable.icon_svg_info_circle)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setHeightPercent(0.5f)
                .addButton(new ModalButton(
                        "No",
                        R.drawable.icon_svg_circle_xmark,
                        R.color.text_85,
                        R.color.text_85,
                        () -> {
                            // No hacer nada
                        }
                ))
                .addButton(new ModalButton(
                        "Sí",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.SUCCESS,
                        () -> {
                            // Confirmar acción
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        modal.show(getSupportFragmentManager(), "confirmation");
    }

    /**
     * EJEMPLO: Modal con un solo botón
     */
    private void showAlert() {
        ModalConfig config = new ModalConfig()
                .setTitle("Información")
                .setTitleIconResId(R.drawable.icon_svg_info_circle)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setHeightPercent(0.4f)
                .setShowCloseButton(false) // Sin botón X
                .addButton(new ModalButton(
                        "Entendido",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.INFO,
                        () -> {
                            // Cerrar modal
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        modal.show(getSupportFragmentManager(), "alert");
    }
}


