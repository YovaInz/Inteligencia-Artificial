import shap
import xgboost
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split

np.random.seed(67)
n_clientes = 100

data = {
    'edad': np.random.randint(18, 70, n_clientes),
    'historial de siniestros': np.random.randint(0, 5, n_clientes),
    'valor del vehiculo': np.random.randint(10000, 60000, n_clientes),
    'años de licencia': np.random.randint(1, 40, n_clientes)
}

df = pd.DataFrame(data)

# Creamos una variable objetivo (riesgo) basada en reglas lógicas + un poco de ruido
# Riesgo aumenta con siniestros y baja con años de licencia
df['riesgo'] = (df['historial de siniestros'] * 0.2) - (df['años de licencia'] * 0.01) + np.random.normal(0, 0.1, n_clientes)

# 2. PREPARACIÓN DE LOS DATOS
X = df.drop('riesgo', axis=1)
y = df['riesgo']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 3. ENTRENAMIENTO DEL MODELO "CAJA NEGRA"
# Usamos XGBoost, que es muy potente pero difícil de interpretar a simple vista
model = xgboost.XGBRegressor(n_estimators=100).fit(X_train, y_train)
print("Predicción de riesgo para el primer cliente de test:", model.predict(X_test[0:1])[0])

# 4. IMPLEMENTACIÓN DE XAI (SHAP)
# El "Explainer" analiza el modelo y los datos para entender las decisiones
explainer = shap.Explainer(model)
shap_values = explainer(X_test, check_additivity=False)
    
# 5. VISUALIZACIÓN DE LA EXPLICACIÓN
# El gráfico de cascada (waterfall) es el estándar en XAI
shap.plots.waterfall(shap_values[0])
# Gráfico resumen de todo el modelo
shap.summary_plot(shap_values, X_test)