package com.example.cajaac.models;

import android.view.View;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de configuración para el modal reutilizable
 *
 * Ejemplo de uso con padding directo en dp:
 * <pre>
 * ModalConfig config = new ModalConfig()
 *     .setTitle("Título")
 *     .setHorizontalPaddingDp(64); // 64dp fijo
 * </pre>
 *
 * Ejemplo de uso con recurso de dimensión (adaptativo según tamaño de pantalla):
 * <pre>
 * ModalConfig config = new ModalConfig()
 *     .setTitle("Título")
 *     .setHorizontalPaddingRes(R.dimen.modal_padding_horizontal); // Se adapta automáticamente
 * </pre>
 */
public class ModalConfig {
    private String title;
    @DrawableRes
    private int titleIconResId;
    @LayoutRes
    private int contentLayoutResId;
    private View contentView;
    private List<ModalButton> buttons;
    private float heightPercent = 0.9f; // 90% por defecto
    private boolean showCloseButton = true;
    @ColorRes
    private int backgroundColor = -1;
    private boolean cancelable = true;
    private int horizontalPaddingDp = 32; // 32dp por defecto
    @DimenRes
    private int horizontalPaddingResId = 0; // Recurso de dimensión opcional

    public ModalConfig() {
        this.buttons = new ArrayList<>();
    }

    public ModalConfig(String title, @DrawableRes int titleIconResId, @LayoutRes int contentLayoutResId) {
        this.title = title;
        this.titleIconResId = titleIconResId;
        this.contentLayoutResId = contentLayoutResId;
        this.buttons = new ArrayList<>();
    }

    // Builder pattern methods
    public ModalConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public ModalConfig setTitleIconResId(@DrawableRes int titleIconResId) {
        this.titleIconResId = titleIconResId;
        return this;
    }

    public ModalConfig setContentLayoutResId(@LayoutRes int contentLayoutResId) {
        this.contentLayoutResId = contentLayoutResId;
        return this;
    }

    public ModalConfig setContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public ModalConfig addButton(ModalButton button) {
        this.buttons.add(button);
        return this;
    }

    public ModalConfig setButtons(List<ModalButton> buttons) {
        this.buttons = buttons;
        return this;
    }

    public ModalConfig setHeightPercent(float heightPercent) {
        this.heightPercent = heightPercent;
        return this;
    }

    public ModalConfig setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        return this;
    }

    public ModalConfig setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ModalConfig setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public ModalConfig setHorizontalPaddingDp(int horizontalPaddingDp) {
        this.horizontalPaddingDp = horizontalPaddingDp;
        this.horizontalPaddingResId = 0; // Limpiar el recurso si se usa valor directo
        return this;
    }

    /**
     * Establece el padding horizontal usando un recurso de dimensión
     * @param horizontalPaddingResId Recurso de dimensión (ej: R.dimen.modal_padding_horizontal)
     * @return Esta instancia para encadenamiento
     */
    public ModalConfig setHorizontalPaddingRes(@DimenRes int horizontalPaddingResId) {
        this.horizontalPaddingResId = horizontalPaddingResId;
        this.horizontalPaddingDp = 0; // Limpiar el valor directo si se usa recurso
        return this;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public int getTitleIconResId() {
        return titleIconResId;
    }

    public int getContentLayoutResId() {
        return contentLayoutResId;
    }

    public View getContentView() {
        return contentView;
    }

    public List<ModalButton> getButtons() {
        return buttons;
    }

    public float getHeightPercent() {
        return heightPercent;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public int getHorizontalPaddingDp() {
        return horizontalPaddingDp;
    }

    public int getHorizontalPaddingResId() {
        return horizontalPaddingResId;
    }
}

