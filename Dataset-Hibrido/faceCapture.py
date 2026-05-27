import cv2
import os
import time
import glob

# Configuración inicial
nombre_persona = input("Ingresa el nombre de la persona a capturar (ej. Alumno1): ")
directorio_base = "Python/Dataset-Hibrido/Dataset"
path_rostro = os.path.join(directorio_base, nombre_persona)

# Crear directorio si no existe
if not os.path.exists(path_rostro):
    os.makedirs(path_rostro)

# Encontrar el número más alto de imagen existente para evitar sobreescribir
archivos_existentes = glob.glob(os.path.join(path_rostro, "img_*.jpg"))
numeros_existentes = []
for archivo in archivos_existentes:
    try:
        numero = int(os.path.basename(archivo).split('_')[1].split('.')[0])
        numeros_existentes.append(numero)
    except (IndexError, ValueError):
        pass  # Ignorar archivos que no sigan el patrón
 
if numeros_existentes:
    contador_fotos = max(numeros_existentes) + 1
    limite_fotos = contador_fotos + 100 # Continuar desde el último número + 100 fotos nuevas
else:
    limite_fotos = 100 # Número de fotos a tomar por persona
    contador_fotos = 0

# Cargar el clasificador de rostros de OpenCV (Haar Cascade)
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

# Iniciar captura de video
cap = cv2.VideoCapture(1)


print(f"Iniciando captura para {nombre_persona}. Presiona 'q' para salir antes de tiempo.")
print(f"Continuando desde imagen {contador_fotos}.")

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Convertir a escala de grises para la detección
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    
    # Detectar rostros
    rostros = face_cascade.detectMultiScale(gray, scaleFactor=1.2, minNeighbors=5, minSize=(60, 60))

    for (x, y, w, h) in rostros:
        # Recortar el rostro (Cropping)
        rostro_recortado = frame[y:y+h, x:x+w]
        
        # Alineación/Redimensionamiento uniforme a 160x160
        rostro_160 = cv2.resize(rostro_recortado, (160, 160))
        
        # Guardar la imagen
        ruta_imagen = os.path.join(path_rostro, f"img_{contador_fotos}.jpg")
        cv2.imwrite(ruta_imagen, rostro_160)
        contador_fotos += 1
        time.sleep(0.25)
        
        # Dibujar un rectángulo para visualización
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
        cv2.putText(frame, f"Capturas: {contador_fotos}/{limite_fotos}", (x, y-10), 
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)

    cv2.imshow('Captura de Dataset', frame)

    # Detener si se alcanza el límite o se presiona 'q'
    if cv2.waitKey(1) & 0xFF == ord('q') or contador_fotos >= limite_fotos:
        break

cap.release()
cv2.destroyAllWindows()
print("Captura finalizada.")