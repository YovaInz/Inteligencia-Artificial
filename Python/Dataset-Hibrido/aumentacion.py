import cv2
import os
import numpy as np

# Ruta directa a tu dataset final (el que ya tiene las fotos de 160x160)
directorio_dataset = "Python/Dataset-Hibrido/Dataset"

print("Iniciando aumentación de datos (Data Augmentation)...")

# Recorrer cada carpeta de persona en tu dataset
for clase in os.listdir(directorio_dataset):
    ruta_clase = os.path.join(directorio_dataset, clase)
    
    # Ignorar archivos sueltos, solo procesar carpetas
    if not os.path.isdir(ruta_clase):
        continue
        
    print(f"Multiplicando imágenes para: {clase}")
    
    # Obtener lista de imágenes actuales
    imagenes = os.listdir(ruta_clase)
    fotos_generadas = 0
    
    for nombre_img in imagenes:
        # Evitar re-aumentar imágenes que ya fueron aumentadas si corres el script 2 veces
        if nombre_img.startswith("aug_"):
            continue
            
        ruta_img = os.path.join(ruta_clase, nombre_img)
        img = cv2.imread(ruta_img)
        
        if img is None:
            continue
            
        # 1. Efecto Espejo (Flip horizontal)
        img_espejo = cv2.flip(img, 1)
        cv2.imwrite(os.path.join(ruta_clase, f"aum_espejo_{nombre_img}"), img_espejo)
        
        # 2. Rotación (15 grados)
        h, w = img.shape[:2]
        centro = (w // 2, h // 2)
        M_rot = cv2.getRotationMatrix2D(centro, 15, 1.0)
        # BORDER_REPLICATE ayuda a que los bordes vacíos tras rotar se llenen con los colores cercanos
        img_rot = cv2.warpAffine(img, M_rot, (w, h), borderMode=cv2.BORDER_REPLICATE)
        cv2.imwrite(os.path.join(ruta_clase, f"aum_rot15_{nombre_img}"), img_rot)
        
        # 3. Cambio de Brillo (Más oscuro: 70% del brillo original)
        img_oscura = np.clip(img * 0.7, 0, 255).astype(np.uint8)
        cv2.imwrite(os.path.join(ruta_clase, f"aum_oscuro_{nombre_img}"), img_oscura)
        
        # 4. Cambio de Brillo (Más brillante: 130% del brillo original)
        img_brillante = np.clip(img * 1.3, 0, 255).astype(np.uint8)
        cv2.imwrite(os.path.join(ruta_clase, f"aum_brillante_{nombre_img}"), img_brillante)
        
        fotos_generadas += 4

    print(f"  -> Se generaron {fotos_generadas} imágenes nuevas para {clase}.")

print("\n¡Aumentación de datos completada!")