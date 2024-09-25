import pickle

import mysql.connector
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder, StandardScaler

# MySQL 연결 설정
conn = mysql.connector.connect(
    host='localhost',  # MySQL 호스트 주소
    user='root',  # MySQL 사용자 이름
    password='1234',  # MySQL 비밀번호
    database='finance_buddy_db'  # 연결할 데이터베이스 이름
)

# 데이터 불러오기
query = """
SELECT name, type, issuer, issue_date, expiry_date, price, currency, category, 
       risk_level, interest_rate, duration, return_type 
FROM financial_products
"""
df = pd.read_sql(query, conn)

# DB 연결 종료
conn.close()

# 데이터 확인
print("데이터 샘플:\n", df.head())

# 1. 결측값 처리
df = df.fillna({
    'issuer': 'Unknown',   # 발행사 정보가 없는 경우 'Unknown'으로 채움
    'interest_rate': 0.0,  # 이자율 정보가 없는 경우 0으로 채움
    'price': 0.0           # 가격 정보가 없는 경우 0으로 채움
})

# 결측값이 남아 있는지 확인
print("결측값 개수:\n", df.isnull().sum())

# 2. 날짜 데이터 처리 (datetime 변환)
df['issue_date'] = pd.to_datetime(df['issue_date'], errors='coerce')
df['expiry_date'] = pd.to_datetime(df['expiry_date'], errors='coerce')

# 날짜 차이 계산 (예: issue_date와 expiry_date의 차이)
df['duration_days'] = (df['expiry_date'] - df['issue_date']).dt.days.fillna(0)

# 날짜 변환이 올바르게 되었는지 확인
invalid_dates = df[df['issue_date'].isna() | df['expiry_date'].isna()]
print("비정상적인 날짜가 있는 행:\n", invalid_dates.head())

# `issuer`, `name`, `issue_date`, `expiry_date` 컬럼 제거
df = df.drop(['issuer', 'name', 'issue_date', 'expiry_date'], axis=1)

# 3. 텍스트 데이터 인코딩
label_encoders = {}
text_columns = ['type', 'currency', 'category', 'risk_level', 'duration', 'return_type']

for col in text_columns:
    le = LabelEncoder()
    df[col] = le.fit_transform(df[col].astype(str))  # 모든 값이 문자열로 변환되어 인코딩되도록 처리
    label_encoders[col] = le

# 인코딩된 값이 정상적인지 확인
for col, le in label_encoders.items():
    print(f"{col} 인코딩 값 확인: {list(le.classes_)}")

print("인코딩 후 데이터 샘플:\n", df.head())

# 4. 수치 데이터 스케일링
scaler = StandardScaler()
numeric_columns = ['interest_rate', 'price', 'duration_days']  # 스케일링할 수치형 열
df[numeric_columns] = scaler.fit_transform(df[numeric_columns])

# 스케일링된 값이 정상적인지 확인
print("스케일링 후 데이터 샘플:\n", df.head())

# 5. 훈련/테스트 데이터 분할
# 특성과 타깃 변수 분리
X = df.drop(['return_type'], axis=1)  # 'return_type'을 예측 타깃으로 설정
y = df['return_type']

# 훈련 및 테스트 데이터 분할
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

print(f"훈련 데이터 크기: {X_train.shape}")
print(f"테스트 데이터 크기: {X_test.shape}")

# 6. 전처리된 데이터를 Parquet 파일로 저장
parquet_file_path = 'preprocessed_data.parquet'
df.to_parquet(parquet_file_path, index=False)
print("전처리된 데이터를 Parquet 파일로 저장했습니다.")


# 6. 전처리된 데이터를 Parquet 파일로 저장
parquet_file_path = 'preprocessed_data.parquet'
df.to_parquet(parquet_file_path, index=False)
print("전처리된 데이터를 Parquet 파일로 저장했습니다.")

# 추가: LabelEncoders와 Scaler 저장
encoder_path = 'label_encoders.pkl'
scaler_path = 'scaler.pkl'

with open(encoder_path, 'wb') as encoder_file:
    pickle.dump(label_encoders, encoder_file)
    print(f"LabelEncoders를 {encoder_path} 파일로 저장했습니다.")

with open(scaler_path, 'wb') as scaler_file:
    pickle.dump(scaler, scaler_file)
    print(f"Scaler를 {scaler_path} 파일로 저장했습니다.")