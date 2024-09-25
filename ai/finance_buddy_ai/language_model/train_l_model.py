import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.metrics import classification_report, accuracy_score
import joblib

# 1. 데이터 불러오기
dataset_path = 'combined_training_dataset.csv'
df = pd.read_csv(dataset_path)

# 2. 데이터 전처리
label_encoders = {}
for col in ['type', 'risk_level', 'duration']:
    le = LabelEncoder()
    df[col] = le.fit_transform(df[col])
    label_encoders[col] = le

# 특성과 타깃 변수 분리
X = df[['type', 'risk_level', 'duration']]
y = df['type']

# 3. 훈련/테스트 데이터 분할
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 4. 스케일링
scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

# 5. 모델 학습
model = RandomForestClassifier(n_estimators=100, random_state=42)
print("모델 학습 중...")
model.fit(X_train, y_train)

# 6. 모델 평가
print("모델 평가 중...")
y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)
print(f"모델 정확도: {accuracy:.4f}")
print("\n분류 보고서:\n", classification_report(y_test, y_pred))

# 7. 모델 저장
joblib.dump(model, 'l_investment_recommendation_model.pkl')
joblib.dump(scaler, 'l_scaler.pkl')
joblib.dump(label_encoders, 'l_label_encoders.pkl')
print("모델, 스케일러, 라벨 인코더가 저장되었습니다.")