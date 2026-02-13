# Sistema de Modales Reutilizables para Android

Este sistema permite crear modales (DialogFragments) personalizados de forma rápida y reutilizable, sin necesidad de crear múltiples archivos XML o clases Java para cada modal.

## 📋 Componentes del Sistema

### 1. **BaseModalFragment** (`ui/BaseModalFragment.java`)
DialogFragment base que maneja toda la lógica de presentación del modal.

### 2. **ModalConfig** (`models/ModalConfig.java`)
Clase de configuración que define las características del modal:
- Título e ícono
- Contenido (puede ser un layout XML o una Vista)
- Lista de botones
- Altura del modal (porcentaje de la pantalla)
- Color de fondo
- Mostrar/ocultar botón de cerrar
- Cancelable o no

### 3. **ModalButton** (`models/ModalButton.java`)
Clase que define cada botón del modal:
- Texto
- Ícono
- Color del texto e ícono
- Fondo (puede ser un tipo predefinido o personalizado)
- Acción al hacer clic

### 4. **Layouts**
- `fragment_base_modal.xml`: Layout base del modal
- `content_cuadre_stock.xml`: Ejemplo de contenido personalizado

## 🎨 Tipos de Botones Predefinidos

El sistema incluye estos tipos de botones con estilos predefinidos:

- `NORMAL`: Botón sin fondo (texto e ícono en color gris)
- `PRIMARY`: Botón verde (#6DC560)
- `INFO`: Botón azul (#3B82F6)
- `WARNING`: Botón naranja (#FF8947)
- `DANGER`: Botón rojo (#EF4444)
- `SUCCESS`: Botón verde éxito (#22C55E)
- `SECONDARY`: Botón gris azulado (#636E95)

## 🚀 Guía de Uso

### Ejemplo Básico: Modal con 1 Botón

```java
// 1. Crear la configuración del modal
ModalConfig config = new ModalConfig()
    .setTitle("Confirmación")
    .setTitleIconResId(R.drawable.icon_svg_info_circle)
    .setContentLayoutResId(R.layout.mi_contenido_personalizado)
    .setHeightPercent(0.5f) // 50% de la pantalla
    .addButton(new ModalButton(
        "Aceptar",
        R.drawable.icon_svg_check,
        ModalButton.ButtonType.INFO,
        () -> {
            // Acción al hacer clic
            Toast.makeText(context, "Aceptado", Toast.LENGTH_SHORT).show();
        }
    ));

// 2. Crear el modal
BaseModalFragment modal = BaseModalFragment.newInstance(config);

// 3. Mostrar el modal
modal.show(getSupportFragmentManager(), "mi_modal");
```

### Ejemplo: Modal con 2 Botones (Cancelar y Confirmar)

```java
ModalConfig config = new ModalConfig()
    .setTitle("¿Está seguro?")
    .setTitleIconResId(R.drawable.icon_svg_cash_register)
    .setContentLayoutResId(R.layout.content_confirmation)
    .setHeightPercent(0.6f)
    .addButton(new ModalButton(
        "Cancelar",
        R.drawable.icon_svg_circle_xmark,
        R.color.text_85,      // Color del texto
        R.color.text_85,      // Color del ícono
        () -> {
            // Cerrar modal
            modal.dismiss();
        }
    ))
    .addButton(new ModalButton(
        "Confirmar",
        R.drawable.icon_svg_check,
        ModalButton.ButtonType.SUCCESS,
        () -> {
            // Confirmar acción
            realizarAccion();
            modal.dismiss();
        }
    ));

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "confirmation");
```

### Ejemplo: Modal con 3 Botones (Como Cuadre de Stock)

```java
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
        () -> modal.dismiss()
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

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "cuadre_stock");
```

### Ejemplo: Botón con Fondo Personalizado

```java
ModalButton customButton = new ModalButton(
    "Mi Botón",
    R.drawable.mi_icono,
    R.color.white,                      // Color del texto
    R.color.white,                      // Color del ícono
    R.drawable.mi_background_custom,    // Fondo personalizado
    () -> {
        // Acción
    }
);
```

## 🎯 Características Avanzadas

### 1. Modal sin Botón de Cerrar (X)

```java
config.setShowCloseButton(false);
```

### 2. Modal No Cancelable (no se cierra al tocar fuera)

```java
config.setCancelable(false);
```

### 3. Cambiar Altura del Modal

```java
config.setHeightPercent(0.7f); // 70% de la pantalla
```

### 4. Acceder al Contenido del Modal

```java
BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "my_modal");

// Acceder a las vistas del contenido
View contentView = modal.getContentView();
if (contentView != null) {
    EditText input = contentView.findViewById(R.id.mi_campo);
    String valor = input.getText().toString();
}
```

### 5. Usar una Vista Personalizada en lugar de Layout XML

```java
// Inflar tu vista personalizada
View miVista = getLayoutInflater().inflate(R.layout.mi_layout, null);
EditText campo = miVista.findViewById(R.id.campo);
campo.setText("Valor inicial");

// Configurar el modal con la vista
ModalConfig config = new ModalConfig()
    .setTitle("Mi Modal")
    .setTitleIconResId(R.drawable.icon_svg_info_circle)
    .setContentView(miVista)  // Usar vista en lugar de layout
    .addButton(...);
```

## 📁 Estructura de Archivos

```
app/src/main/
├── java/com/example/cajaac/
│   ├── models/
│   │   ├── ModalButton.java        # Configuración de botones
│   │   └── ModalConfig.java        # Configuración del modal
│   └── ui/
│       ├── BaseModalFragment.java  # DialogFragment base
│       └── ModalUsageExamples.java # Ejemplos de uso
├── res/
│   ├── layout/
│   │   ├── fragment_base_modal.xml     # Layout base del modal
│   │   └── content_cuadre_stock.xml    # Ejemplo de contenido
│   └── drawable/
│       ├── background_button_primary.xml
│       ├── background_button_info.xml
│       ├── background_button_warning.xml
│       ├── background_button_danger.xml
│       ├── background_button_success.xml
│       └── background_button_secondary.xml
```

## 🎨 Personalización de Colores

Los colores de los botones se definen en `res/values/colors.xml`:

```xml
<color name="primary">#6DC560</color>
<color name="info">#3B82F6</color>
<color name="warning">#FF8947</color>
<color name="danger">#EF4444</color>
<color name="success">#22C55E</color>
<color name="secondary">#636E95</color>
```

## 💡 Consejos

1. **Reutilización**: Crea layouts de contenido separados para diferentes tipos de modales
2. **Consistencia**: Usa los tipos de botones predefinidos para mantener consistencia en la UI
3. **Altura**: Ajusta la altura según el contenido (0.5f para mensajes cortos, 0.9f para formularios)
4. **Validación**: Siempre valida datos antes de cerrar el modal en el onClick del botón

## 🔄 Migración de Modales Existentes

Para convertir un modal existente al nuevo sistema:

1. Extrae el contenido del modal a un layout separado
2. Crea la configuración usando `ModalConfig`
3. Define los botones con `ModalButton`
4. Reemplaza la llamada al modal antiguo por `BaseModalFragment.newInstance(config)`

Ver `ModalUsageExamples.java` para más ejemplos prácticos.

