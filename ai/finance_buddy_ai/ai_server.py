import joblib
import numpy as np
from flask import Flask, request, jsonify

# Flask 애플리케이션 초기화
app = Flask(__name__)

# 모델과 전처리 객체 로드
model = joblib.load('language_model/l_investment_recommendation_model.pkl')
scaler = joblib.load('language_model/l_scaler.pkl')
label_encoders = joblib.load('language_model/l_label_encoders.pkl')

# 입력 문장에서 특징 추출 함수
def extract_features_from_text(input_text):
    features = {
        'risk_level': '고위험' if '고위험' in input_text else ('위험' if '위험' in input_text else '안전'),
        'duration': '단기' if '단기' in input_text else ('중기' if '중기' in input_text else '장기'),
        'type': '채권' if '채권' in input_text else ('연금' if '연금' in input_text else '펀드'),
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

    feature_array = np.array([
        processed_features.get('risk_level', 0),
        processed_features.get('duration', 0),
        processed_features.get('type', 0)
    ]).reshape(1, -1)

    scaled_features = scaler.transform(feature_array)
    return scaled_features

# 예측을 수행하는 엔드포인트
@app.route('/predict', methods=['POST'])
def predict():
    data = request.json  # JSON 데이터를 받아옴
    input_text = data.get('input_text', '')  # 사용자가 입력한 문장

    print(input_text)

    try:
        # 입력 문장에서 특징 추출
        features = extract_features_from_text(input_text)

        # 특징을 전처리하여 모델 입력 형태로 변환
        processed_features = preprocess_input_features(features)

        # 모델 예측 수행
        prediction = model.predict(processed_features)
        prediction_label = label_encoders['type'].inverse_transform(prediction)[0]

        # 결과 반환 (예측된 추천 타입과 특징)
        return jsonify({
            'input': input_text,
            'extracted_features': features,
            'predicted_type': prediction_label
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 400

# Flask 서버 실행
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)