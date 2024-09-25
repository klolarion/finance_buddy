import joblib
import numpy as np

# 모델과 스케일러 로드
model_path = 'investment_recommendation_model.pkl'
encoder_path = 'label_encoders.pkl'
scaler_path = 'scaler.pkl'

model = joblib.load(model_path)
label_encoders = joblib.load(encoder_path)
scaler = joblib.load(scaler_path)

def extract_features_from_text(input_text):
    # 간단한 키워드 추출 로직
    features = {
        'risk_level': '고위험' if '위험' in input_text else '안전',
        'duration': '단기' if '단기' in input_text else '장기',
        'type': '채권형' if '채권' in input_text else '펀드형',
    }
    print(f"추출된 특징: {features}")
    return features

def preprocess_input_features(features):
    processed_features = {
        'risk_level': 0,
        'duration': 0,
        'type': 0,
        'price': 0,
        'interest_rate': 0,
        'currency': 0,
        'category': 0,
        'duration_days': 0
    }

    # 입력된 특징 값을 인코딩
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

    feature_array = np.array([
        processed_features['risk_level'],
        processed_features['duration'],
        processed_features['type'],
        processed_features['price'],
        processed_features['interest_rate'],
        processed_features['currency'],
        processed_features['category'],
        processed_features['duration_days']
    ]).reshape(1, -1)

    scaled_features = scaler.transform(feature_array)
    return scaled_features

def predict_investment(input_text):
    features = extract_features_from_text(input_text)
    processed_features = preprocess_input_features(features)

    prediction = model.predict(processed_features)
    prediction_label = label_encoders['return_type'].inverse_transform(prediction)[0]

    print(f"입력: {input_text}\n예측된 투자 추천: {prediction_label}")

# 사용자 입력 테스트
input_text = "안전한 채권형 투자 추천해줘"
predict_investment(input_text)