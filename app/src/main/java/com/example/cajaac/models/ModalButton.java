package com.example.cajaac.models;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * Clase que representa la configuración de un botón para el modal
 */
public class ModalButton {
    private String text;
    @DrawableRes
    private int iconResId;
    @ColorRes
    private int textColorResId;
    @ColorRes
    private int iconTintResId;
    @DrawableRes
    private int backgroundResId;
    private OnClickListener onClickListener;
    private ButtonType buttonType;

    public enum ButtonType {
        NORMAL,     // Botón normal sin fondo
        PRIMARY,    // Botón con fondo de color primario
        SECONDARY,  // Botón con fondo de color secundario
        INFO,       // Botón con fondo de color info
        WARNING,    // Botón con fondo de color warning
        DANGER,     // Botón con fondo de color danger
        SUCCESS     // Botón con fondo de color success
    }

    public interface OnClickListener {
        void onClick();
    }

    public ModalButton(String text, @DrawableRes int iconResId, ButtonType buttonType, OnClickListener listener) {
        this.text = text;
        this.iconResId = iconResId;
        this.buttonType = buttonType;
        this.onClickListener = listener;
        this.textColorResId = -1;
        this.iconTintResId = -1;
        this.backgroundResId = -1;
    }

    public ModalButton(String text, @DrawableRes int iconResId, @ColorRes int textColorResId,
                       @ColorRes int iconTintResId, OnClickListener listener) {
        this.text = text;
        this.iconResId = iconResId;
        this.textColorResId = textColorResId;
        this.iconTintResId = iconTintResId;
        this.onClickListener = listener;
        this.buttonType = ButtonType.NORMAL;
        this.backgroundResId = -1;
    }

    public ModalButton(String text, @DrawableRes int iconResId, @ColorRes int textColorResId,
                       @ColorRes int iconTintResId, @DrawableRes int backgroundResId, OnClickListener listener) {
        this.text = text;
        this.iconResId = iconResId;
        this.textColorResId = textColorResId;
        this.iconTintResId = iconTintResId;
        this.backgroundResId = backgroundResId;
        this.onClickListener = listener;
        this.buttonType = ButtonType.NORMAL;
    }

    // Getters
    public String getText() {
        return text;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getTextColorResId() {
        return textColorResId;
    }

    public int getIconTintResId() {
        return iconTintResId;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public ButtonType getButtonType() {
        return buttonType;
    }

    // Setters
    public ModalButton setText(String text) {
        this.text = text;
        return this;
    }

    public ModalButton setIconResId(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public ModalButton setTextColorResId(int textColorResId) {
        this.textColorResId = textColorResId;
        return this;
    }

    public ModalButton setIconTintResId(int iconTintResId) {
        this.iconTintResId = iconTintResId;
        return this;
    }

    public ModalButton setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        return this;
    }

    public ModalButton setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public ModalButton setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
        return this;
    }
}

