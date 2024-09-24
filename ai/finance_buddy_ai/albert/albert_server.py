from flask import Flask, request, jsonify
import torch
from transformers import AlbertTokenizer, AlbertForSequenceClassification

# Flask 애플리케이션 초기화
app = Flask(__name__)

# 모델과 토크나이저 로드
MODEL_PATH = '/Users/klolarion/IdeaProjects/finance_buddy/ai/finance_buddy_ai/albert/results/checkpoint-9'  # 모델 경로
tokenizer = AlbertTokenizer.from_pretrained('albert-base-v2')
model = AlbertForSequenceClassification.from_pretrained(MODEL_PATH)
model.eval()

# 인코딩 매핑 (훈련 시 사용한 매핑을 동일하게 설정)
type_mapping = {'연금': 0, '채권': 1, '펀드': 2}
risk_mapping = {'안전': 0, '위험': 1, '고위험': 2}
duration_mapping = {'단기': 0, '중기': 1, '장기': 2}
return_type_mapping = {'저수익': 0, '고수익': 1}

# 인덱스에서 레이블로의 역매핑
reverse_type_mapping = {v: k for k, v in type_mapping.items()}
reverse_risk_mapping = {v: k for k, v in risk_mapping.items()}
reverse_duration_mapping = {v: k for k, v in duration_mapping.items()}
reverse_return_type_mapping = {v: k for k, v in return_type_mapping.items()}

# Flask 라우트 설정
@app.route('/predict', methods=['POST'])
def predict():
    data = request.json
    input_text = data.get('input_text', '')

    # 입력 텍스트 토큰화
    inputs = tokenizer(input_text, return_tensors='pt', padding='max_length', max_length=128, truncation=True)

    # 모델 예측
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits
        predictions = torch.argmax(logits, dim=1).tolist()

    # 각 레이블별 예측 결과 매핑
    predicted_type = reverse_type_mapping[predictions[0]]
    predicted_risk = reverse_risk_mapping[predictions[1]]
    predicted_duration = reverse_duration_mapping[predictions[2]]
    predicted_return = reverse_return_type_mapping[predictions[3]]

    # 결과 반환
    result = {
        'input': input_text,
        'predicted_type': predicted_type,
        'predicted_risk_level': predicted_risk,
        'predicted_duration': predicted_duration,
        'predicted_return_type': predicted_return
    }
    return jsonify(result)

# Flask 서버 실행
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)