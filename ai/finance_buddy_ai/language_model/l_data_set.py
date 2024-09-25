import pandas as pd
import mysql.connector

# 사용자 입력 데이터를 생성하는 함수
def generate_user_data():
    user_data = []

    # 연금 예시
    for i in range(500):
        user_data.append({"sentence": f"연금 상품으로 안전하게 투자할 방법 추천해줘.", "risk_level": "안전", "duration": "장기", "type": "연금"})
        user_data.append({"sentence": f"연금으로 장기적인 투자 방법이 궁금해.", "risk_level": "안전", "duration": "장기", "type": "연금"})
        user_data.append({"sentence": f"안정적인 연금 투자 상품 추천해.", "risk_level": "안전", "duration": "장기", "type": "연금"})

    # 채권 예시
    for i in range(500):
        user_data.append({"sentence": f"안전한 채권형 상품 추천해줘.", "risk_level": "안전", "duration": "장기", "type": "채권"})
        user_data.append({"sentence": f"단기로 안전한 채권형 투자하고 싶어.", "risk_level": "안전", "duration": "단기", "type": "채권"})
        user_data.append({"sentence": f"고위험 단기 채권형 투자 추천해줘.", "risk_level": "고위험", "duration": "단기", "type": "채권"})
        user_data.append({"sentence": f"중기 투자에 적합한 채권형 상품 있어?", "risk_level": "위험", "duration": "중기", "type": "채권"})
        user_data.append({"sentence": f"위험 부담이 적은 장기 채권 추천해줘.", "risk_level": "위험", "duration": "장기", "type": "채권"})

    # 펀드 예시
    for i in range(500):
        user_data.append({"sentence": f"펀드형으로 안전하게 투자할 수 있는 방법 알려줘.", "risk_level": "안전", "duration": "장기", "type": "펀드"})
        user_data.append({"sentence": f"단기 고위험 펀드형 상품 추천해줘.", "risk_level": "고위험", "duration": "단기", "type": "펀드"})
        user_data.append({"sentence": f"중기 펀드 투자에 좋은 상품 있을까?", "risk_level": "위험", "duration": "중기", "type": "펀드"})
        user_data.append({"sentence": f"고위험 펀드로 투자할 만한 것 추천해.", "risk_level": "고위험", "duration": "장기", "type": "펀드"})
        user_data.append({"sentence": f"위험 부담 적은 장기 펀드 투자 방법 뭐야?", "risk_level": "위험", "duration": "장기", "type": "펀드"})

    return pd.DataFrame(user_data)

# DB에서 금융 상품 데이터를 불러오는 함수
def fetch_db_data():
    conn = mysql.connector.connect(
        host='localhost',
        user='root',
        password='1234',
        database='finance_buddy_db'
    )
    query = "SELECT type, risk_level, duration FROM financial_products"
    df_db = pd.read_sql(query, conn)
    conn.close()
    return df_db

# 사용자 입력 데이터 생성
df_user = generate_user_data()

# DB에서 데이터 불러오기
df_db = fetch_db_data()

# DB 데이터와 사용자 입력 데이터를 결합
df_combined = pd.concat([df_db, df_user[['type', 'risk_level', 'duration']]], ignore_index=True)

# 결합된 데이터셋을 CSV 파일로 저장
output_path = "combined_training_dataset.csv"
df_combined.to_csv(output_path, index=False)
print(f"데이터셋이 '{output_path}'에 저장되었습니다.")