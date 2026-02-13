package com.example.cajaac.ui;

import com.example.cajaac.R;
import com.example.cajaac.models.ModalButton;
import com.example.cajaac.models.ModalConfig;

/**
 * GUÍA DE USO DEL SISTEMA DE MODALES REUTILIZABLES
 *
 * Este archivo contiene ejemplos de cómo usar el sistema de modales reutilizables.
 *
 * COMPONENTES PRINCIPALES:
 * - BaseModalFragment: Fragmento base del modal
 * - ModalConfig: Configuración del modal (título, contenido, botones, etc.)
 * - ModalButton: Configuración de cada botón
 *
 * ========================================
 * EJEMPLO 1: Modal Simple con 1 Botón
 * ========================================
 */
public class ModalUsageExamples {

    /**
     * Ejemplo 1: Modal simple con un solo botón
     */
    public static void showSimpleModal() {
        // Crear configuración del modal
        ModalConfig config = new ModalConfig()
                .setTitle("Confirmación")
                .setTitleIconResId(R.drawable.icon_svg_info_circle)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setHeightPercent(0.5f) // 50% de la pantalla
                .addButton(new ModalButton(
                        "Aceptar",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.INFO,
                        () -> {
                            // Acción al hacer click
                            System.out.println("Botón clickeado");
                        }
                ));

        // Crear y mostrar el modal
        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "simple_modal");
    }

    /**
     * Ejemplo 2: Modal con 2 botones (Cancelar y Confirmar)
     */
    public static void showConfirmationModal() {
        ModalConfig config = new ModalConfig()
                .setTitle("¿Está seguro?")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setHeightPercent(0.6f)
                .addButton(new ModalButton(
                        "Cancelar",
                        R.drawable.icon_svg_circle_xmark,
                        R.color.text_85,
                        R.color.text_85,
                        () -> {
                            // Cerrar modal
                        }
                ))
                .addButton(new ModalButton(
                        "Confirmar",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.SUCCESS,
                        () -> {
                            // Confirmar acción
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "confirmation_modal");
    }

    /**
     * Ejemplo 3: Modal con 3 botones (como el de Cuadre de Stock)
     */
    public static void showCuadreStockModal() {
        ModalConfig config = new ModalConfig()
                .setTitle("Cuadre de stock pendiente")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_cuadre_stock)
                .setHeightPercent(0.9f)
                .addButton(new ModalButton(
                        "Cancelar",
                        R.drawable.icon_svg_circle_xmark,
                        R.color.text_85,
                        R.color.text_85,
                        () -> {
                            // Cancelar
                        }
                ))
                .addButton(new ModalButton(
                        "Guardar borrador",
                        R.drawable.icon_svg_save_disk,
                        R.color.info,
                        R.color.info,
                        () -> {
                            // Guardar como borrador
                        }
                ))
                .addButton(new ModalButton(
                        "Guardar cuadre",
                        R.drawable.icon_svg_save_disk_solid,
                        ModalButton.ButtonType.INFO,
                        () -> {
                            // Guardar cuadre final
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "cuadre_stock_modal");
    }

    /**
     * Ejemplo 4: Modal con botones de diferentes tipos
     */
    public static void showMultiTypeButtonsModal() {
        ModalConfig config = new ModalConfig()
                .setTitle("Acciones disponibles")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setHeightPercent(0.7f)
                .addButton(new ModalButton(
                        "Cancelar",
                        R.drawable.icon_svg_xmark,
                        ModalButton.ButtonType.NORMAL,
                        () -> {}
                ))
                .addButton(new ModalButton(
                        "Advertencia",
                        R.drawable.icon_svg_info_circle,
                        ModalButton.ButtonType.WARNING,
                        () -> {}
                ))
                .addButton(new ModalButton(
                        "Peligro",
                        R.drawable.icon_svg_xmark,
                        ModalButton.ButtonType.DANGER,
                        () -> {}
                ))
                .addButton(new ModalButton(
                        "Éxito",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.SUCCESS,
                        () -> {}
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "multi_type_modal");
    }

    /**
     * Ejemplo 5: Modal sin botón de cerrar
     */
    public static void showModalWithoutCloseButton() {
        ModalConfig config = new ModalConfig()
                .setTitle("Acción requerida")
                .setTitleIconResId(R.drawable.icon_svg_info_circle)
                .setContentLayoutResId(R.layout.content_simple_message)
                .setShowCloseButton(false) // Sin botón X
                .setCancelable(false) // No se puede cerrar tocando fuera
                .addButton(new ModalButton(
                        "Continuar",
                        R.drawable.icon_svg_check_circle,
                        ModalButton.ButtonType.PRIMARY,
                        () -> {
                            // Acción requerida
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "required_action_modal");
    }

    /**
     * Ejemplo 6: Modal con botón personalizado (colores y fondo custom)
     */
    public static void showCustomButtonModal() {
        ModalConfig config = new ModalConfig()
                .setTitle("Personalizado")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_simple_message)
                .addButton(new ModalButton(
                        "Custom",
                        R.drawable.icon_svg_check_circle,
                        R.color.white,                     // Color del texto
                        R.color.white,                     // Color del ícono
                        R.drawable.background_button_info, // Fondo personalizado
                        () -> {}
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "custom_button_modal");
    }

    /**
     * Ejemplo 7: Acceder al contenido del modal después de crearlo
     */
    public static void showModalAndAccessContent() {
        ModalConfig config = new ModalConfig()
                .setTitle("Formulario")
                .setTitleIconResId(R.drawable.icon_svg_cash_register)
                .setContentLayoutResId(R.layout.content_cuadre_stock)
                .addButton(new ModalButton(
                        "Guardar",
                        R.drawable.icon_svg_save_disk_solid,
                        ModalButton.ButtonType.INFO,
                        () -> {
                            // Obtener datos del formulario
                            // View content = modal.getContentView();
                            // EditText input = content.findViewById(R.id.et_buscar_productos);
                            // String value = input.getText().toString();
                        }
                ));

        BaseModalFragment modal = BaseModalFragment.newInstance(config);
        // modal.show(fragmentManager, "form_modal");
    }
}



