import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report, accuracy_score
import joblib

# 1. 전처리된 데이터 불러오기
parquet_file_path = 'preprocessed_data.parquet'
df = pd.read_parquet(parquet_file_path)

# 데이터 확인
print("데이터 샘플:\n", df.head())

# 특성과 타깃 변수 분리
X = df.drop(['return_type'], axis=1)  # 'return_type'을 예측 타깃으로 설정
y = df['return_type']

# 훈련 및 테스트 데이터 분할
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 2. 스케일러 정의 및 학습
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 3. 모델 정의
model = RandomForestClassifier(n_estimators=100, random_state=42)
print(model)

# 4. 모델 학습
print("모델 학습 중...")
model.fit(X_train_scaled, y_train)

# 5. 모델 평가
print("모델 평가 중...")
y_pred = model.predict(X_test_scaled)
accuracy = accuracy_score(y_test, y_pred)

print(f"모델 정확도: {accuracy:.4f}")
print("\n분류 보고서:\n", classification_report(y_test, y_pred))

# 6. 모델과 스케일러 저장
model_filename = 'investment_recommendation_model.pkl'
scaler_filename = 'scaler.pkl'
joblib.dump(model, model_filename)
joblib.dump(scaler, scaler_filename)
print(f"모델이 '{model_filename}' 파일로 저장되었습니다.")
print(f"스케일러가 '{scaler_filename}' 파일로 저장되었습니다.")