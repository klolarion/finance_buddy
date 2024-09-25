import torch
from transformers import AlbertTokenizer, AlbertForSequenceClassification
from torch.utils.data import DataLoader, Dataset
import pandas as pd


# 사용자 정의 테스트 데이터셋 클래스
class FinancialTestDataset(Dataset):
    def __init__(self, data):
        self.data = data

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        labels = self.data.iloc[idx].values
        input_ids = torch.zeros(128, dtype=torch.long)  # 더미 input_ids
        attention_mask = torch.ones(128, dtype=torch.long)  # 더미 attention_mask
        return {
            'input_ids': input_ids,
            'attention_mask': attention_mask,
            'labels': torch.tensor(labels, dtype=torch.long)
        }


# 테스트 데이터 생성 함수
def generate_test_data():
    types = ['연금', '채권', '펀드']
    risks = ['안전', '위험', '고위험']
    durations = ['단기', '중기', '장기']
    returns = ['저수익', '고수익']

    test_data = []
    for t in types:
        for r in risks:
            for d in durations:
                for ret in returns:
                    test_data.append({
                        'type': t,
                        'risk_level': r,
                        'duration': d,
                        'return_type': ret
                    })

    df = pd.DataFrame(test_data)

    # 레이블 인코딩
    type_mapping = {label: idx for idx, label in enumerate(df['type'].unique())}
    risk_mapping = {label: idx for idx, label in enumerate(df['risk_level'].unique())}
    duration_mapping = {label: idx for idx, label in enumerate(df['duration'].unique())}
    return_type_mapping = {label: idx for idx, label in enumerate(df['return_type'].unique())}

    # 인코딩된 레이블 추가
    df['type_label'] = df['type'].map(type_mapping)
    df['risk_label'] = df['risk_level'].map(risk_mapping)
    df['duration_label'] = df['duration'].map(duration_mapping)
    df['return_label'] = df['return_type'].map(return_type_mapping)

    return df[['type_label', 'risk_label', 'duration_label', 'return_label']]


# 테스트 데이터 로드
test_data = generate_test_data()
test_dataset = FinancialTestDataset(test_data)
test_loader = DataLoader(test_dataset, batch_size=16, shuffle=False)

# 모델과 토크나이저 로드
tokenizer = AlbertTokenizer.from_pretrained('albert-base-v2')
model = AlbertForSequenceClassification.from_pretrained(
    '/Users/klolarion/IdeaProjects/finance_buddy/ai/finance_buddy_ai/albert/results/checkpoint-9')
model.eval()


# 모델 평가 함수
def evaluate_model(model, dataloader):
    model.eval()
    device = torch.device("cpu")
    model.to(device)

    total = [0, 0, 0, 0]  # 각 레이블의 예측 개수
    correct = [0, 0, 0, 0]  # 4개의 레이블 각각의 정확도 계산
    with torch.no_grad():
        for batch in dataloader:
            input_ids = batch['input_ids'].to(device)
            attention_mask = batch['attention_mask'].to(device)
            labels = batch['labels'].to(device)

            # 모델 예측
            outputs = model(input_ids=input_ids, attention_mask=attention_mask)
            predictions = torch.argmax(outputs.logits, dim=1)  # 다중 클래스 예측

            # 각 레이블별로 비교
            for i in range(4):
                correct[i] += (predictions == labels[:, i]).sum().item()
                total[i] += labels.size(0)

    # 레이블별 정확도 출력
    for i, label_name in enumerate(['type', 'risk_level', 'duration', 'return_type']):
        accuracy = correct[i] / total[i]
        print(f"{label_name} 정확도: {accuracy * 100:.2f}%")


# 모델 평가
evaluate_model(model, test_loader)
