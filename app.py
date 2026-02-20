import streamlit as st
import pandas as pd
import pickle
import shap

# Load model and encoders
with open("customer_churn_model", "rb") as f:
    model_data = pickle.load(f)
model = model_data["model"]
feature_names = model_data["Features_names"]

with open("encoders.pkl", "rb") as f:
    encoders = pickle.load(f)

# UI setup
st.set_page_config(page_title="Customer Churn Predictor", layout="centered")
st.title("🔍 Customer Churn Predictor with Explanation")

# Input
user_input = {}
for col in feature_names:
    if col in encoders:
        user_input[col] = st.selectbox(f"{col}", encoders[col].classes_)
    elif col == "tenure":
        user_input[col] = st.slider(col, 0, 72, 12)
    elif col == "MonthlyCharges":
        user_input[col] = st.slider(col, 0.0, 150.0, 70.0)
    elif col == "TotalCharges":
        user_input[col] = st.slider(col, 0.0, 9000.0, 2000.0)
    else:
        user_input[col] = st.number_input(col)

# Prepare input for model
input_df = pd.DataFrame([user_input])
for col, encoder in encoders.items():
    if col in input_df.columns:
        input_df[col] = encoder.transform(input_df[col])
input_df = input_df[feature_names]

# Predict
prediction = model.predict(input_df)[0]
probability = model.predict_proba(input_df)[0][1]

st.subheader("🎯 Prediction Result")
st.markdown(f"*Prediction:* {'🛑 Churn' if prediction == 1 else '✅ No Churn'}")
st.markdown(f"*Churn Probability:* {probability:.2%}")

# SHAP Explanation
st.subheader("📊 Top Feature Contributions")

explainer = shap.TreeExplainer(model)
shap_values = explainer(input_df)
exp = shap_values[0]

# Process SHAP values
feature_names_list = list(exp.feature_names)
data_1d = exp.data if isinstance(exp.data, (list, tuple)) else exp.data.flatten()
values_1d = exp.values if isinstance(exp.values, (list, tuple)) else exp.values.flatten()

min_len = min(len(feature_names_list), len(data_1d), len(values_1d))
feature_names_list = feature_names_list[:min_len]
data_1d = data_1d[:min_len]
values_1d = values_1d[:min_len]

# Create DataFrame
contributions = pd.DataFrame({
    "Feature": feature_names_list,
    "Value": data_1d,
    "SHAP Value": values_1d
})

# Sort and display top 5
contributions["AbsImpact"] = contributions["SHAP Value"].abs()
top_features = contributions.sort_values(by="AbsImpact", ascending=False).head(5)

st.dataframe(
    top_features.drop(columns="AbsImpact").style.format({
        "Value": "{:.2f}",
        "SHAP Value": "{:+.3f}"
    })
)