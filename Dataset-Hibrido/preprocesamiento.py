import cv2
import os

# 1. Configuración de directorios
directorio_origen = "Python/Dataset-Hibrido/dataset_crudo" # Carpeta con las fotos originales/descargadas
directorio_destino = "Python/Dataset-Hibrido/Dataset"      # Carpeta final estructurada para el modelo

# 2. Cargar el detector de rostros de OpenCV (Haar Cascade)
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

# Tamaño objetivo según tu metodología
tamano_objetivo = (160, 160)

# Crear directorio destino si no existe
if not os.path.exists(directorio_destino):
    os.makedirs(directorio_destino)

print("Iniciando preprocesamiento de imágenes in-the-wild...")

# 3. Recorrer cada subcarpeta (cada persona) en el directorio crudo
for nombre_clase in os.listdir(directorio_origen):
    ruta_clase_origen = os.path.join(directorio_origen, nombre_clase)
    
    # Ignorar archivos sueltos, solo leer carpetas
    if not os.path.isdir(ruta_clase_origen):
        continue
        
    ruta_clase_destino = os.path.join(directorio_destino, nombre_clase)
    
    # Crear la carpeta para esta persona en el destino final
    if not os.path.exists(ruta_clase_destino):
        os.makedirs(ruta_clase_destino)
        
    print(f"Procesando imágenes de: {nombre_clase}")
    fotos_procesadas = 0
    
    # 4. Leer y procesar cada imagen de la persona
    for nombre_archivo in os.listdir(ruta_clase_origen):
        ruta_imagen = os.path.join(ruta_clase_origen, nombre_archivo)
        
        # Leer la imagen
        imagen = cv2.imread(ruta_imagen)
        if imagen is None:
            continue # Saltar si el archivo está corrupto o no es imagen
            
        # Convertir a escala de grises para facilitar la detección
        gray = cv2.cvtColor(imagen, cv2.COLOR_BGR2GRAY)
        
        # Detectar rostros
        rostros = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(60, 60))
        
        # 5. Cropping y redimensionamiento
        # Si detecta rostros, procesa el primero que encuentre en la foto
        if len(rostros) > 0:
            (x, y, w, h) = rostros[0]
            
            # Recortar el rostro de la imagen original a color
            rostro_recortado = imagen[y:y+h, x:x+w]
            
            # Alinear a 160x160 píxeles
            rostro_160 = cv2.resize(rostro_recortado, tamano_objetivo)
            
            # Guardar la imagen procesada en la carpeta del Dataset final
            ruta_guardado = os.path.join(ruta_clase_destino, f"rostro_{fotos_procesadas}.jpg")
            cv2.imwrite(ruta_guardado, rostro_160)
            fotos_procesadas += 1

    print(f"  -> {fotos_procesadas} rostros procesados y guardados con éxito.")

print("\n¡Preprocesamiento completado!")