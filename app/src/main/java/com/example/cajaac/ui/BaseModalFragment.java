package com.example.cajaac.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cajaac.R;
import com.example.cajaac.adapters.ModalButtonAdapter;
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
     * Configura los botones del modal usando un RecyclerView con ModalButtonAdapter
     */
    private void setupButtons(View view) {
        RecyclerView buttonsContainer = view.findViewById(R.id.modal_buttons_container);

        if (config.getButtons() == null || config.getButtons().isEmpty()) {
            buttonsContainer.setVisibility(View.GONE);
            return;
        }

        buttonsContainer.setVisibility(View.VISIBLE);
        buttonsContainer.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        ModalButtonAdapter adapter = new ModalButtonAdapter();
        buttonsContainer.setAdapter(adapter);

        // Esperar a que el RecyclerView tenga su ancho medido para distribuir botones equitativamente
        buttonsContainer.post(() -> {
            adapter.setRecyclerViewWidth(buttonsContainer.getWidth());
            adapter.submitList(config.getButtons());
        });
    }

    /**
     * Configura la altura del modal
     */
    private void setupModalHeight(View view) {
        View modalContainer = view.findViewById(R.id.modal_container_bg);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) modalContainer.getLayoutParams();
        params.matchConstraintPercentHeight = config.getHeightPercent();
        modalContainer.setLayoutParams(params);
    }

    /**
     * Configura el padding horizontal del modal
     */
    private void setupHorizontalPadding(View view) {
        View modalBackground = view.findViewById(R.id.modal_background);
        int paddingPx;

        // Usar recurso de dimensión si está disponible, de lo contrario usar valor directo en dp
        if (config.getHorizontalPaddingResId() != 0) {
            paddingPx = (int) requireContext().getResources().getDimension(config.getHorizontalPaddingResId());
        } else {
            paddingPx = dpToPx(config.getHorizontalPaddingDp());
        }

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




