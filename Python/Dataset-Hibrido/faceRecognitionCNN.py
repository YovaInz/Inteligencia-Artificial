# Integrantes
# - Angel Amaury Arredondo Gonzalez
# - Cynthia Urias Beltran
# - Cesar Yovanni Inzunza Aguilar

import cv2
import os
import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# Configuración
IMG_SIZE      = (160, 160)
BATCH_SIZE    = 32
DATASET_PATH  = 'Python/Dataset-Hibrido/Dataset'
MODEL_PATH    = 'Python/Dataset-Hibrido/dataset-hibrido-cnn.h5'
CONFIDENCE_THRESHOLD = 0.6   # Confianza mínima para mostrar nombre (0-1)

# Cargar o entrenar modelo
if os.path.exists(MODEL_PATH):
    print(f"Modelo encontrado en '{MODEL_PATH}'. Cargando...")
    model = tf.keras.models.load_model(MODEL_PATH)

    # Reconstruir class_names desde el dataset (mismo orden que durante el entrenamiento)
    class_names = sorted([
        d for d in os.listdir(DATASET_PATH)
        if os.path.isdir(os.path.join(DATASET_PATH, d))
    ])
    print(f"  Clases cargadas: {class_names}")
else:
    print("Modelo no encontrado. Iniciando entrenamiento...")

    train_datagen = ImageDataGenerator(rescale=1./255, validation_split=0.2)

    train_generator = train_datagen.flow_from_directory(
        DATASET_PATH,
        target_size=IMG_SIZE,
        batch_size=BATCH_SIZE,
        class_mode='sparse',
        subset='training'
    )
    validation_generator = train_datagen.flow_from_directory(
        DATASET_PATH,
        target_size=IMG_SIZE,
        batch_size=BATCH_SIZE,
        class_mode='sparse',
        subset='validation'
    )

    base_model = tf.keras.applications.MobileNetV2(
        input_shape=IMG_SIZE + (3,),
        include_top=False,
        weights='imagenet'
    )
    base_model.trainable = False

    model = models.Sequential([
        base_model,
        layers.GlobalAveragePooling2D(),
        layers.Dense(len(train_generator.class_indices), activation='softmax')
    ])
    model.compile(
        optimizer='adam',
        loss='sparse_categorical_crossentropy',
        metrics=['accuracy']
    )

    model.fit(train_generator, validation_data=validation_generator, epochs=3)

    # Guardar modelo entrenado
    model.save(MODEL_PATH)
    print(f"Modelo guardado en '{MODEL_PATH}'")

    class_names = list(train_generator.class_indices.keys())
    print(f"  Clases: {class_names}")

model.summary()

# Detección en tiempo real
face_cascade = cv2.CascadeClassifier(
    cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
)

cap = cv2.VideoCapture(1)
if not cap.isOpened():
    cap = cv2.VideoCapture(0)

print("\nPresiona 'q' para salir.")

while True:
    ret, frame = cap.read()
    if not ret:
        break

    gray   = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    rostros = face_cascade.detectMultiScale(
        gray, scaleFactor=1.2, minNeighbors=5, minSize=(60, 60)
    )

    for (x, y, w, h) in rostros:
        rostro_recortado = frame[y:y+h, x:x+w]
        rostro_rgb       = cv2.cvtColor(rostro_recortado, cv2.COLOR_BGR2RGB)
        rostro_160       = cv2.resize(rostro_rgb, IMG_SIZE)
        rostro_norm      = rostro_160.astype('float32') / 255.0
        rostro_input     = np.expand_dims(rostro_norm, axis=0)   # (1, 160, 160, 3)

        # Predicción
        predicciones  = model.predict(rostro_input, verbose=0)[0]
        clase_idx     = int(np.argmax(predicciones))
        confianza     = float(predicciones[clase_idx])

        if confianza >= CONFIDENCE_THRESHOLD:
            nombre = class_names[clase_idx]
            color  = (0, 255, 0)   # verde -> reconocido
        else:
            nombre = "Desconocido"
            color  = (0, 0, 255)   # rojo -> no reconocido

        # Dibujar cuadro y etiqueta
        cv2.rectangle(frame, (x, y), (x+w, y+h), color, 2)

        etiqueta = f"{nombre}  {confianza*100:.1f}%"
        (tw, th), _ = cv2.getTextSize(etiqueta, cv2.FONT_HERSHEY_SIMPLEX, 0.65, 2)
        cv2.rectangle(frame, (x, y - th - 12), (x + tw + 6, y), color, -1)
        cv2.putText(
            frame, etiqueta,
            (x + 3, y - 6),
            cv2.FONT_HERSHEY_SIMPLEX, 0.65,
            (0, 0, 0), 2
        )

    cv2.imshow('Reconocimiento Facial', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
print("Sesión finalizada.")
