import pandas as pd
import numpy as np
import torch
from transformers import BertTokenizer, BertModel

print("라이브러리들이 잘 설치되었습니다!")

# BERT 모델 불러오기 테스트
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased')
model = BertModel.from_pretrained('bert-base-multilingual-cased')

# 토크나이저에서 경고 해결을 위한 예시
text = "안녕하세요, AI 모델 테스트 중입니다!"
tokens = tokenizer(text, clean_up_tokenization_spaces=False)  # 경고 해결
print("토큰화 결과:", tokens)

print("BERT 모델이 잘 불러와졌습니다!")