# Resumen de Pruebas Realizadas

## Ventas y Reservas
- Se validó que el sistema permite realizar reservas correctamente, incluyendo selección de asiento, nombre del cliente y tipo de descuento.
- Las reservas registran correctamente el ID del cliente, el asiento reservado, el tipo de descuento aplicado y el precio final.
- Se evita la doble reserva de un mismo asiento.
- Es posible la modificación del asiento de una reserva, este proceso también incluye validación de si el nuevo asiento se encuentra disponible o no

## Descuentos
- Se comprobó que se aplican correctamente los descuentos: 10% para estudiantes y 15% para tercera edad.
- Se integró una lista `discounts` para gestionar los tipos válidos y validar la entrada del usuario. De esta forma también es escalable el crear nuevos 
descuentos solo modificando esta lista

## Datos y Consistencia
- Las reservas están asociadas correctamente a los clientes (arreglo `customers`) y a los identificadores de venta (`salesIds`).
- Al eliminar una reserva, también se eliminan los datos correspondientes en `customers` y `salesIds`, manteniendo la consistencia.

## Validación de Asientos
- Se validó el rango permitido para filas (0–4) y columnas (0–9).
- Se añadieron controles para manejar entradas inválidas (como texto en lugar de números).

## Estabilidad y Optimización
- Se aseguraron datos confiables y consistentes mediante el uso de arreglos y listas.
- Se realizaron pruebas de modificación, eliminación y actualización de datos para confirmar el rendimiento correcto del sistema.
