# 🎯 Sistema de Modales Reutilizables - Guía Completa

## ✅ ¿Qué se ha creado?

Se ha implementado un **sistema completo de modales reutilizables** que te permite crear diálogos personalizados sin necesidad de duplicar código. Ahora puedes cambiar:

- ✅ El **título** y su **ícono**
- ✅ El **contenido** (cualquier layout XML)
- ✅ La **cantidad de botones** (1, 2, 3, o más)
- ✅ El **tipo de botón** (colores predefinidos)
- ✅ Los **colores personalizados** de texto e íconos
- ✅ El **fondo** de cada botón
- ✅ La **altura** del modal
- ✅ Si muestra o no el **botón X** para cerrar

---

## 📦 Archivos Creados

### **Clases Java** (Modelos y UI)

1. **`models/ModalButton.java`** - Configuración de cada botón
2. **`models/ModalConfig.java`** - Configuración del modal completo
3. **`ui/BaseModalFragment.java`** - DialogFragment reutilizable
4. **`ui/ModalUsageExamples.java`** - Ejemplos de uso
5. **`CuadreStockExample.java`** - Ejemplo práctico real

### **Layouts XML**

1. **`layout/fragment_base_modal.xml`** - Layout base del modal
2. **`layout/content_cuadre_stock.xml`** - Contenido del cuadre de stock
3. **`layout/content_simple_message.xml`** - Contenido simple para mensajes

### **Recursos Drawable**

1. **`drawable/background_button_primary.xml`** - Fondo verde
2. **`drawable/background_button_info.xml`** - Fondo azul (ya existía)
3. **`drawable/background_button_warning.xml`** - Fondo naranja
4. **`drawable/background_button_danger.xml`** - Fondo rojo
5. **`drawable/background_button_success.xml`** - Fondo verde éxito
6. **`drawable/background_button_secondary.xml`** - Fondo gris azulado

### **Colores**

Se agregó el color **`success`** en `values/colors.xml`

---

## 🚀 Cómo Usar

### **Ejemplo 1: Modal con 1 Botón**

```java
ModalConfig config = new ModalConfig()
    .setTitle("Información")
    .setTitleIconResId(R.drawable.icon_svg_info_circle)
    .setContentLayoutResId(R.layout.content_simple_message)
    .setHeightPercent(0.5f) // 50% de pantalla
    .addButton(new ModalButton(
        "Aceptar",
        R.drawable.icon_svg_check_circle,
        ModalButton.ButtonType.INFO,
        () -> {
            // Tu código aquí
        }
    ));

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "mi_modal");
```

### **Ejemplo 2: Modal con 2 Botones (Cancelar/Confirmar)**

```java
ModalConfig config = new ModalConfig()
    .setTitle("¿Está seguro?")
    .setTitleIconResId(R.drawable.icon_svg_cash_register)
    .setContentLayoutResId(R.layout.content_simple_message)
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
        "Confirmar",
        R.drawable.icon_svg_check_circle,
        ModalButton.ButtonType.SUCCESS,
        () -> {
            // Confirmar
        }
    ));

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "confirmacion");
```

### **Ejemplo 3: Modal de Cuadre de Stock (3 Botones)**

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
        () -> { /* Cerrar */ }
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

---

## ⚠️ IMPORTANTE: Acceso al Contenido del Modal

**NO hagas esto** (causará NullPointerException):
```java
BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "cuadre_stock");

// ❌ ESTO CAUSA CRASH - el diálogo aún no existe
modal.getDialog().setOnShowListener(dialog -> { ... });
```

**SI necesitas acceder a las vistas del contenido**, usa este enfoque:

```java
BaseModalFragment modal = new BaseModalFragment() {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // ✅ Aquí SÍ puedes acceder al contenido
        View contentView = getContentView();
        if (contentView != null) {
            EditText buscar = contentView.findViewById(R.id.et_buscar_productos);
            buscar.addTextChangedListener(new TextWatcher() { ... });
        }
    }
};

modal.setConfig(config);
modal.show(getSupportFragmentManager(), "cuadre_stock");
```

---

## 🎨 Tipos de Botones Disponibles

### **Con ButtonType (Colores Automáticos)**

```java
new ModalButton("Texto", R.drawable.icono, ModalButton.ButtonType.INFO, () -> {})
```

| Tipo | Color | Uso Recomendado |
|------|-------|-----------------|
| `NORMAL` | Gris | Botones neutrales (Cancelar) |
| `PRIMARY` | Verde #6DC560 | Acción principal |
| `INFO` | Azul #3B82F6 | Información/Guardar |
| `WARNING` | Naranja #FF8947 | Advertencias |
| `DANGER` | Rojo #EF4444 | Acciones destructivas |
| `SUCCESS` | Verde #22C55E | Confirmaciones |
| `SECONDARY` | Gris Azulado #636E95 | Acciones secundarias |

### **🎯 Esquinas Redondeadas Automáticas**

El sistema **automáticamente** aplica las esquinas redondeadas correctas según la posición del botón:

- **Botón único**: Ambas esquinas inferiores redondeadas ↙️↘️
- **Primer botón (izquierda)**: Solo esquina inferior izquierda redondeada ↙️
- **Botones del medio**: Sin esquinas redondeadas
- **Último botón (derecha)**: Solo esquina inferior derecha redondeada ↘️

**No tienes que preocuparte por esto**, el sistema lo maneja automáticamente.

### **Con Colores Personalizados**

```java
new ModalButton(
    "Texto",
    R.drawable.icono,
    R.color.mi_color_texto,
    R.color.mi_color_icono,
    () -> {}
)
```

### **Con Fondo Personalizado**

```java
new ModalButton(
    "Texto",
    R.drawable.icono,
    R.color.white,
    R.color.white,
    R.drawable.mi_fondo_custom,
    () -> {}
)
```

---

## 🛠️ Opciones de Configuración

### **ModalConfig**

```java
new ModalConfig()
    .setTitle("Título")                              // Título del modal
    .setTitleIconResId(R.drawable.icono)            // Ícono junto al título
    .setContentLayoutResId(R.layout.mi_contenido)   // Layout del contenido
    .setContentView(miVista)                         // O una vista directamente
    .setHeightPercent(0.8f)                          // Altura (0.1 a 1.0)
    .setHorizontalPaddingDp(32)                      // Padding horizontal en dp (por defecto 32dp)
    .setShowCloseButton(true)                        // Mostrar botón X
    .setCancelable(true)                             // Cerrar tocando fuera
    .setBackgroundColor(R.color.mi_color)           // Color de fondo
    .addButton(boton1)                               // Agregar botones
    .addButton(boton2)
```

**Opciones de padding horizontal:**
- `setHorizontalPaddingDp(0)` - Sin padding (modal a pantalla completa de ancho)
- `setHorizontalPaddingDp(16)` - Padding pequeño
- `setHorizontalPaddingDp(32)` - **Por defecto** - Padding estándar
- `setHorizontalPaddingDp(48)` - Padding grande
- `setHorizontalPaddingDp(64)` - Padding extra grande

---

## 💡 Casos de Uso Comunes

### **1. Alerta Simple (Solo "OK")**

```java
new ModalConfig()
    .setTitle("¡Éxito!")
    .setTitleIconResId(R.drawable.icon_svg_check_circle)
    .setContentLayoutResId(R.layout.content_simple_message)
    .setHeightPercent(0.4f)
    .setShowCloseButton(false)
    .addButton(new ModalButton(
        "Entendido",
        R.drawable.icon_svg_check_circle,
        ModalButton.ButtonType.SUCCESS,
        () -> {}
    ));
```

### **2. Confirmación de Eliminación**

```java
new ModalConfig()
    .setTitle("¿Eliminar este elemento?")
    .setTitleIconResId(R.drawable.icon_svg_info_circle)
    .setContentLayoutResId(R.layout.content_simple_message)
    .setCancelable(false) // No se puede cerrar tocando fuera
    .addButton(new ModalButton(
        "Cancelar",
        R.drawable.icon_svg_circle_xmark,
        ModalButton.ButtonType.NORMAL,
        () -> {}
    ))
    .addButton(new ModalButton(
        "Eliminar",
        R.drawable.icon_svg_xmark,
        ModalButton.ButtonType.DANGER,
        () -> eliminarElemento()
    ));
```

### **3. Modal de Formulario**

```java
ModalConfig config = new ModalConfig()
    .setTitle("Nuevo Producto")
    .setTitleIconResId(R.drawable.icon_svg_cash_register)
    .setContentLayoutResId(R.layout.formulario_producto)
    .setHeightPercent(0.8f)
    .addButton(new ModalButton(
        "Cancelar",
        R.drawable.icon_svg_circle_xmark,
        R.color.text_85,
        R.color.text_85,
        () -> {}
    ))
    .addButton(new ModalButton(
        "Guardar",
        R.drawable.icon_svg_save_disk_solid,
        ModalButton.ButtonType.INFO,
        () -> guardarProducto()
    ));

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "form");

// Acceder a las vistas del formulario
modal.getDialog().setOnShowListener(dialog -> {
    View content = modal.getContentView();
    EditText nombre = content.findViewById(R.id.et_nombre);
    // ...configurar campos
});
```

### **4. Modal Ancho (Sin padding)**

```java
ModalConfig config = new ModalConfig()
    .setTitle("Tabla de Datos")
    .setTitleIconResId(R.drawable.icon_svg_list)
    .setContentLayoutResId(R.layout.tabla_completa)
    .setHeightPercent(0.9f)
    .setHorizontalPaddingDp(0) // Sin padding - Modal ancho
    .addButton(new ModalButton(
        "Cerrar",
        R.drawable.icon_svg_circle_xmark,
        ModalButton.ButtonType.NORMAL,
        () -> {}
    ));
```

### **5. Modal Pequeño (Padding grande)**

```java
ModalConfig config = new ModalConfig()
    .setTitle("Mensaje")
    .setTitleIconResId(R.drawable.icon_svg_info_circle)
    .setContentLayoutResId(R.layout.content_simple_message)
    .setHeightPercent(0.5f)
    .setHorizontalPaddingDp(64) // Padding grande - Modal estrecho
    .addButton(new ModalButton(
        "OK",
        R.drawable.icon_svg_check_circle,
        ModalButton.ButtonType.INFO,
        () -> {}
    ));
```

---

## 📋 Migración de Modales Existentes

Si tienes un modal como `CuadreStock.java` que usa su propio layout, puedes migrarlo así:

### **Antes:**
```java
CuadreStock modal = CuadreStock.newInstance();
modal.show(getSupportFragmentManager(), "cuadre");
```

### **Después:**
```java
ModalConfig config = new ModalConfig()
    .setTitle("Cuadre de stock pendiente")
    .setTitleIconResId(R.drawable.icon_svg_cash_register)
    .setContentLayoutResId(R.layout.content_cuadre_stock)
    .setHeightPercent(0.9f)
    .addButton(new ModalButton(...))
    .addButton(new ModalButton(...))
    .addButton(new ModalButton(...));

BaseModalFragment modal = BaseModalFragment.newInstance(config);
modal.show(getSupportFragmentManager(), "cuadre");
```

**Beneficios:**
- No necesitas crear un archivo Java nuevo para cada modal
- Reutilizas toda la lógica de presentación
- Más fácil de mantener

---

## 🎓 Ejemplo Completo en una Activity

Ver el archivo **`CuadreStockExample.java`** para un ejemplo completo funcionando.

---

## ✨ Ventajas del Sistema

1. **Reutilizable** - Un solo componente para todos los modales
2. **Flexible** - Configurable para cualquier caso de uso
3. **Consistente** - UI uniforme en toda la app
4. **Mantenible** - Cambios en un solo lugar
5. **Tipado** - Estilos predefinidos para casos comunes
6. **Personalizable** - Colores y fondos custom cuando sea necesario

---

## 📚 Archivos de Referencia

- **Ejemplos:** `ui/ModalUsageExamples.java`
- **Ejemplo Real:** `CuadreStockExample.java`
- **Documentación Completa:** `MODALES_README.md`

---

¡Ahora puedes crear modales personalizados en segundos! 🚀



