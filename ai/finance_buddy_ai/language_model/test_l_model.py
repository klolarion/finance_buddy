import joblib
import numpy as np

# 모델 및 전처리 객체 로드
model = joblib.load('l_investment_recommendation_model.pkl')
scaler = joblib.load('l_scaler.pkl')
label_encoders = joblib.load('l_label_encoders.pkl')

# 테스트 함수
def predict_investment(input_text):
    # 입력 텍스트에서 특징 추출
    features = extract_features_from_text(input_text)
    # 특징을 전처리하여 모델 입력 형태로 변환
    processed_features = preprocess_input_features(features)
    # 모델을 사용하여 예측 수행
    prediction = model.predict(processed_features)
    # 예측된 값을 원래 라벨로 변환
    prediction_label = label_encoders['type'].inverse_transform(prediction)[0]
    print(f"입력: {input_text}\n예측된 투자 추천: {prediction_label}")

# 특징 추출 함수
def extract_features_from_text(input_text):
    # 초기값 설정
    risk_level = '안전'
    duration = '장기'
    investment_type = '펀드'

    # 위험 수준 추출
    if '고위험' in input_text or '위험' in input_text:
        risk_level = '고위험'
    elif '저위험' in input_text or '안전' in input_text:
        risk_level = '안전'
    elif '위험' in input_text:
        risk_level = '위험'

    # 기간 추출
    if '단기' in input_text:
        duration = '단기'
    elif '중기' in input_text:
        duration = '중기'
    elif '장기' in input_text:
        duration = '장기'

    # 투자 유형 추출
    if '채권' in input_text:
        investment_type = '채권'
    elif '펀드' in input_text:
        investment_type = '펀드'
    elif '연금' in input_text:
        investment_type = '연금'

    # 추출된 특징 반환
    features = {
        'risk_level': risk_level,
        'duration': duration,
        'type': investment_type,
    }
    print(f"추출된 특징: {features}")
    return features

# 입력 특징 전처리 함수
def preprocess_input_features(features):
    processed_features = {}

    for key, value in features.items():
        encoder = label_encoders.get(key)
        if encoder:
            try:
                encoded_value = encoder.transform([value])[0]
            except ValueError:
                print(f"'{value}'는 인코딩된 값에 없어서 기본값으로 처리합니다.")
                encoded_value = encoder.transform([encoder.classes_[0]])[0]
        else:
            encoded_value = 0

        processed_features[key] = encoded_value

    # 피처 배열 생성
    feature_array = np.array([
        processed_features.get('risk_level', 0),
        processed_features.get('duration', 0),
        processed_features.get('type', 0)
    ]).reshape(1, -1)

    # 스케일링 적용
    scaled_features = scaler.transform(feature_array)
    return scaled_features.reshape(1, -1)

# 테스트 실행
input_text = "채권상품 있을까 ?."
predict_investment(input_text)