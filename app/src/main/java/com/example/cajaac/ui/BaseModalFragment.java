package com.example.cajaac.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.cajaac.R;
import com.example.cajaac.models.ModalButton;
import com.example.cajaac.models.ModalConfig;

/**
 * DialogFragment base reutilizable para crear modales personalizados
 * Permite configurar título, ícono, contenido y botones dinámicamente
 */
public class BaseModalFragment extends DialogFragment {

    private static final String ARG_CONFIG = "modal_config";
    private ModalConfig config;
    private View contentView;

    public BaseModalFragment() {
        // Required empty public constructor
    }

    /**
     * Crea una nueva instancia del modal con la configuración proporcionada
     * @param config Configuración del modal
     * @return Nueva instancia del modal
     */
    public static BaseModalFragment newInstance(ModalConfig config) {
        BaseModalFragment fragment = new BaseModalFragment();
        fragment.config = config;
        return fragment;
    }

    /**
     * Permite establecer la configuración después de crear la instancia
     * @param config Configuración del modal
     */
    public void setConfig(ModalConfig config) {
        this.config = config;
    }

    /**
     * Permite establecer la vista de contenido personalizada
     * @param contentView Vista de contenido
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer estilo de diálogo sin título
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialogStyle);

        if (config != null) {
            setCancelable(config.isCancelable());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Configurar el diálogo para que ocupe toda la pantalla
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            // Hacer el fondo del diálogo transparente
            getDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT)
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_modal, container, false);

        if (config == null) {
            throw new IllegalStateException("ModalConfig no puede ser null. Usa setConfig() o newInstance(config)");
        }

        setupModal(view, inflater);
        return view;
    }

    /**
     * Configura todos los elementos del modal basándose en la configuración
     */
    private void setupModal(View view, LayoutInflater inflater) {
        setupHeader(view);
        setupContent(view, inflater);
        setupButtons(view);
        setupModalHeight(view);
        setupHorizontalPadding(view);
        setupBackground(view);
    }

    /**
     * Configura el header del modal (título e ícono)
     */
    private void setupHeader(View view) {
        ImageView titleIcon = view.findViewById(R.id.modal_title_icon);
        TextView title = view.findViewById(R.id.modal_title);
        ImageView closeButton = view.findViewById(R.id.btn_close_modal);

        // Configurar ícono del título
        if (config.getTitleIconResId() != 0) {
            titleIcon.setImageResource(config.getTitleIconResId());
            titleIcon.setVisibility(View.VISIBLE);
        } else {
            titleIcon.setVisibility(View.GONE);
        }

        // Configurar título
        if (config.getTitle() != null && !config.getTitle().isEmpty()) {
            title.setText(config.getTitle());
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }

        // Configurar botón de cerrar
        if (config.isShowCloseButton()) {
            closeButton.setVisibility(View.VISIBLE);
            closeButton.setOnClickListener(v -> dismiss());
        } else {
            closeButton.setVisibility(View.GONE);
        }
    }

    /**
     * Configura el contenido del modal
     */
    private void setupContent(View view, LayoutInflater inflater) {
        FrameLayout contentContainer = view.findViewById(R.id.modal_content_container);

        // Si hay una vista de contenido establecida, usarla
        if (contentView != null) {
            contentContainer.removeAllViews();
            contentContainer.addView(contentView);
        }
        // Si hay un layout de contenido, inflarlo
        else if (config.getContentLayoutResId() != 0) {
            View content = inflater.inflate(config.getContentLayoutResId(), contentContainer, false);
            contentContainer.removeAllViews();
            contentContainer.addView(content);
        }
        // Si hay una vista en la configuración, usarla
        else if (config.getContentView() != null) {
            contentContainer.removeAllViews();
            contentContainer.addView(config.getContentView());
        }
    }

    /**
     * Configura los botones del modal dinámicamente
     */
    private void setupButtons(View view) {
        LinearLayout buttonsContainer = view.findViewById(R.id.modal_buttons_container);
        buttonsContainer.removeAllViews();

        if (config.getButtons() == null || config.getButtons().isEmpty()) {
            buttonsContainer.setVisibility(View.GONE);
            return;
        }

        buttonsContainer.setVisibility(View.VISIBLE);
        int buttonCount = config.getButtons().size();

        for (int i = 0; i < buttonCount; i++) {
            ModalButton buttonConfig = config.getButtons().get(i);

            // Determinar la posición del botón
            ButtonPosition position;
            if (buttonCount == 1) {
                position = ButtonPosition.SINGLE;
            } else if (i == 0) {
                position = ButtonPosition.LEFT;
            } else if (i == buttonCount - 1) {
                position = ButtonPosition.RIGHT;
            } else {
                position = ButtonPosition.MIDDLE;
            }

            // Crear el botón con su posición
            LinearLayout button = createButton(buttonConfig, position);
            buttonsContainer.addView(button);

            // Agregar separador si no es el último botón
            if (i < buttonCount - 1) {
                View separator = createSeparator();
                buttonsContainer.addView(separator);
            }
        }
    }

    /**
     * Enum para la posición del botón
     */
    private enum ButtonPosition {
        SINGLE,  // Botón único
        LEFT,    // Primer botón (izquierda)
        MIDDLE,  // Botón del medio
        RIGHT    // Último botón (derecha)
    }

    /**
     * Crea un botón basado en la configuración
     */
    private LinearLayout createButton(ModalButton buttonConfig, ButtonPosition position) {
        LinearLayout button = new LinearLayout(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        button.setLayoutParams(layoutParams);
        button.setOrientation(LinearLayout.VERTICAL);
        button.setGravity(android.view.Gravity.CENTER);
        button.setPadding(
                dpToPx(12),
                dpToPx(12),
                dpToPx(12),
                dpToPx(12)
        );
        button.setClickable(true);
        button.setFocusable(true);

        // Aplicar fondo según el tipo o configuración personalizada
        applyButtonBackground(button, buttonConfig, position);

        // Crear ícono
        ImageView icon = new ImageView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                dpToPx(24),
                dpToPx(24)
        );
        iconParams.bottomMargin = dpToPx(4);
        icon.setLayoutParams(iconParams);
        if (buttonConfig.getIconResId() != 0) {
            icon.setImageResource(buttonConfig.getIconResId());
        }

        // Aplicar tint al ícono
        applyIconTint(icon, buttonConfig);

        // Crear texto
        TextView text = new TextView(requireContext());
        text.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        text.setText(buttonConfig.getText());
        text.setTextAppearance(R.style.fontListasMedium);

        // Aplicar color al texto
        applyTextColor(text, buttonConfig);

        // Agregar vistas al botón
        button.addView(icon);
        button.addView(text);

        // Configurar click listener
        if (buttonConfig.getOnClickListener() != null) {
            button.setOnClickListener(v -> buttonConfig.getOnClickListener().onClick());
        }

        return button;
    }

    /**
     * Aplica el fondo al botón según su tipo o configuración y posición
     */
    private void applyButtonBackground(LinearLayout button, ModalButton buttonConfig, ButtonPosition position) {
        if (buttonConfig.getBackgroundResId() != -1 && buttonConfig.getBackgroundResId() != 0) {
            button.setBackgroundResource(buttonConfig.getBackgroundResId());
        } else {
            int backgroundResId = getBackgroundForTypeAndPosition(buttonConfig.getButtonType(), position);
            if (backgroundResId != 0) {
                button.setBackgroundResource(backgroundResId);
            }
        }
    }

    /**
     * Obtiene el recurso de fondo correcto según el tipo de botón y su posición
     */
    private int getBackgroundForTypeAndPosition(ModalButton.ButtonType type, ButtonPosition position) {
        String suffix;
        switch (position) {
            case LEFT:
                suffix = "_left";
                break;
            case MIDDLE:
                suffix = "_middle";
                break;
            case RIGHT:
                suffix = "_right";
                break;
            case SINGLE:
                suffix = "_single";
                break;
            default:
                suffix = "_right";
                break;
        }

        switch (type) {
            case PRIMARY:
                return getDrawableIdByName("background_button_primary" + suffix);
            case INFO:
                return getDrawableIdByName("background_button_info" + suffix);
            case WARNING:
                return getDrawableIdByName("background_button_warning" + suffix);
            case DANGER:
                return getDrawableIdByName("background_button_danger" + suffix);
            case SUCCESS:
                return getDrawableIdByName("background_button_success" + suffix);
            case SECONDARY:
                return getDrawableIdByName("background_button_secondary" + suffix);
            case NORMAL:
            default:
                return 0; // Sin fondo
        }
    }

    /**
     * Obtiene el ID de un drawable por su nombre
     */
    private int getDrawableIdByName(String name) {
        try {
            return getResources().getIdentifier(name, "drawable", requireContext().getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Aplica el color al ícono
     */
    private void applyIconTint(ImageView icon, ModalButton buttonConfig) {
        if (buttonConfig.getIconTintResId() != -1 && buttonConfig.getIconTintResId() != 0) {
            icon.setColorFilter(ContextCompat.getColor(requireContext(), buttonConfig.getIconTintResId()));
        } else {
            // Aplicar color según el tipo de botón
            switch (buttonConfig.getButtonType()) {
                case PRIMARY:
                case INFO:
                case WARNING:
                case DANGER:
                case SUCCESS:
                case SECONDARY:
                    icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
                    break;
                case NORMAL:
                default:
                    // Mantener color original o aplicar text_85
                    if (buttonConfig.getIconTintResId() == -1) {
                        icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.text_85));
                    }
                    break;
            }
        }
    }

    /**
     * Aplica el color al texto
     */
    private void applyTextColor(TextView text, ModalButton buttonConfig) {
        if (buttonConfig.getTextColorResId() != -1 && buttonConfig.getTextColorResId() != 0) {
            text.setTextColor(ContextCompat.getColor(requireContext(), buttonConfig.getTextColorResId()));
        } else {
            // Aplicar color según el tipo de botón
            switch (buttonConfig.getButtonType()) {
                case PRIMARY:
                case INFO:
                case WARNING:
                case DANGER:
                case SUCCESS:
                case SECONDARY:
                    text.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    break;
                case NORMAL:
                default:
                    text.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_85));
                    break;
            }
        }
    }

    /**
     * Crea un separador vertical entre botones
     */
    private View createSeparator() {
        View separator = new View(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(1),
                dpToPx(40)
        );
        separator.setLayoutParams(params);
        separator.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.text_5));
        return separator;
    }

    /**
     * Configura la altura del modal
     */
    private void setupModalHeight(View view) {
        LinearLayout modalContainer = view.findViewById(R.id.modal_container);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) modalContainer.getLayoutParams();
        params.matchConstraintPercentHeight = config.getHeightPercent();
        modalContainer.setLayoutParams(params);
    }

    /**
     * Configura el padding horizontal del modal
     */
    private void setupHorizontalPadding(View view) {
        View modalBackground = view.findViewById(R.id.modal_background);
        int paddingPx = dpToPx(config.getHorizontalPaddingDp());
        modalBackground.setPadding(
                paddingPx,
                modalBackground.getPaddingTop(),
                paddingPx,
                modalBackground.getPaddingBottom()
        );
    }

    /**
     * Configura el color de fondo del modal
     */
    private void setupBackground(View view) {
        if (config.getBackgroundColor() != -1 && config.getBackgroundColor() != 0) {
            View background = view.findViewById(R.id.modal_background);
            background.setBackgroundColor(ContextCompat.getColor(requireContext(), config.getBackgroundColor()));
        }
    }

    /**
     * Convierte dp a px
     */
    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * Obtiene la vista del contenido para acceder a sus elementos
     */
    public View getContentView() {
        if (getView() != null) {
            FrameLayout container = getView().findViewById(R.id.modal_content_container);
            if (container.getChildCount() > 0) {
                return container.getChildAt(0);
            }
        }
        return null;
    }
}




