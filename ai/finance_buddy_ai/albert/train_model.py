import pandas as pd
from transformers import AlbertTokenizer, AlbertForSequenceClassification, Trainer, TrainingArguments
from sklearn.model_selection import train_test_split
import torch
from torch.utils.data import Dataset, DataLoader

# 데이터셋 생성 함수
def generate_dataset():
    # 사용자 입력 데이터 생성 (특징만 사용)
    user_data = []
    # 예시 데이터 생성
    types = ['연금', '채권', '펀드']
    risks = ['안전', '위험', '고위험']
    durations = ['단기', '중기', '장기']
    returns = ['저수익', '고수익']

    for t in types:
        for r in risks:
            for d in durations:
                for ret in returns:
                    user_data.append({
                        'type': t,
                        'risk_level': r,
                        'duration': d,
                        'return_type': ret
                    })

    df = pd.DataFrame(user_data)
    return df

# 데이터셋 로드 및 전처리
df = generate_dataset()

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

# 데이터셋 분할
train_data, val_data = train_test_split(
    df[['type_label', 'risk_label', 'duration_label', 'return_label']],
    test_size=0.2,
    random_state=42
)

# 사용자 정의 데이터셋 클래스
class FinancialDataset(Dataset):
    def __init__(self, data):
        self.data = data

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        labels = self.data.iloc[idx].values
        # 더미 input_ids와 attention_mask를 레이블의 길이에 맞춰 생성
        input_ids = torch.zeros(len(labels), 128, dtype=torch.long)
        attention_mask = torch.ones(len(labels), 128, dtype=torch.long)
        return {
            'input_ids': input_ids,
            'attention_mask': attention_mask,
            'labels': torch.tensor(labels, dtype=torch.long)
        }

# 데이터셋 로드
train_dataset = FinancialDataset(train_data)
val_dataset = FinancialDataset(val_data)

# 데이터 로더 설정
train_loader = DataLoader(train_dataset, batch_size=16, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=16, shuffle=False)

# ALBERT 토크나이저와 모델 로드
tokenizer = AlbertTokenizer.from_pretrained('albert-base-v2')
model = AlbertForSequenceClassification.from_pretrained('albert-base-v2', num_labels=4)

# 모델을 CPU에서 실행
device = torch.device("cpu")
model.to(device)

# 훈련 설정
training_args = TrainingArguments(
    output_dir='./results',
    num_train_epochs=3,
    per_device_train_batch_size=16,
    per_device_eval_batch_size=16,
    evaluation_strategy="epoch",
    save_strategy="epoch",
    logging_dir='./logs',
    logging_steps=10,
    use_cpu=True
)

# 훈련용 데이터 준비 함수
def collate_fn(batch):
    # 각 데이터의 레이블만 가져와서 모델에 전달
    input_ids = torch.stack([x['input_ids'] for x in batch]).view(-1, 128)
    attention_mask = torch.stack([x['attention_mask'] for x in batch]).view(-1, 128)
    labels = torch.stack([x['labels'] for x in batch]).view(-1, 4)
    return {
        'input_ids': input_ids,
        'attention_mask': attention_mask,
        'labels': labels
    }

# Trainer 초기화
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=train_dataset,
    eval_dataset=val_dataset,
    data_collator=collate_fn
)

# 모델 훈련
trainer.train()