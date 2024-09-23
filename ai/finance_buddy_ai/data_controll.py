import requests
import mysql.connector
from datetime import datetime
import json

# MySQL 연결 설정
conn = mysql.connector.connect(
    host='localhost',  # MySQL 호스트 주소
    user='root',  # MySQL 사용자 이름
    password='1234',  # MySQL 비밀번호
    database='finance_buddy_db'  # 연결할 데이터베이스 이름
)
cursor = conn.cursor()

# API 데이터 가져오는 함수
def fetch_data_from_api(url, params):
    response = requests.get(url, params=params)
    if response.status_code == 200:
        return response.json()  # API 데이터가 JSON 형식으로 반환된다고 가정
    else:
        print(f"API 요청 실패: {response.status_code}")
        return []

# API URL 및 파라미터 설정
fund_url = "https://apis.data.go.kr/1160100/service/GetFundProductInfoService/getStandardCodeInfo"
bond_url = "https://apis.data.go.kr/1160100/service/GetBondIssuInfoService/getBondBasiInfo"
pension_url = "https://apis.data.go.kr/1160100/service/GetRetirementPensionInfoService/getFundInfo"

params = {
    "serviceKey": "key",
    "type": "json",
    "pageNo": 1,
    "numOfRows": 5
}



# API 데이터 가져오기
fund_data = fetch_data_from_api(fund_url, params)
bond_data = fetch_data_from_api(bond_url, params)
pension_data = fetch_data_from_api(pension_url, params)

# 펀드 데이터 변환 및 저장
def process_fund_data(data):
    records = []
    for item in data:
        name = item.get('fndNm', '')
        type_ = '펀드'
        issuer = item.get('cmpyNm', None)
        issue_date = item.get('setpDt', None)
        expiry_date = None
        price = None
        currency = 'KRW'
        category = item.get('ctg', '')

        # 펀드 유형에 따른 속성 설정
        fnd_type = item.get('fndTp', '')

        if fnd_type == '파생':
            risk_level = '고위험'
            duration = '단기'
            return_type = '고수익'
            interest_rate = 8.0

        elif fnd_type == '주식형':
            risk_level = '위험'
            duration = '장기'
            return_type = '고수익'
            interest_rate = 5.0

        elif fnd_type == '채권형':
            risk_level = '안전'
            duration = '장기'
            return_type = '저수익'
            interest_rate = 1.5

        elif fnd_type == '혼합형':
            risk_level = '안전'
            duration = '중기'
            return_type = '저수익'
            interest_rate = 3.0

        elif fnd_type == '재간접형':
            risk_level = '안전'
            duration = '중기'
            return_type = '저수익'
            interest_rate = 2.5

        else:
            # 유형이 없는 경우 기본 설정 (기본값 설정 필요 시)
            risk_level = '미정'
            duration = '미정'
            return_type = '미정'
            interest_rate = 0.0

        # 날짜 형식 변환
        if issue_date:
            issue_date = datetime.strptime(issue_date, '%Y%m%d').date()

        # 변환된 데이터 레코드 추가
        records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level,
                        interest_rate, duration, return_type))

    return records

from datetime import datetime

# 채권 데이터 변환 및 저장
def process_bond_data(data):
    records = []
    today = datetime.today().date()  # 현재 날짜

    for item in data:
        name = item.get('isinCdNm', '')
        type_ = '채권'
        issuer = item.get('bondIsurNm', '')
        issue_date = item.get('bondIssuDt', None)
        expiry_date = item.get('bondExprDt', None)
        price = None
        currency = item.get('bondIssuCurCdNm', 'KRW')
        category = item.get('scrsItmsKcdNm', '')
        risk_level = item.get('grnDcdNm', 'N/A')
        interest_rate = float(item.get('bondSrfcInrt', 0))

        # 만기일을 기준으로 duration 설정
        if expiry_date:
            expiry_date = datetime.strptime(expiry_date, '%Y%m%d').date()
            duration_years = (expiry_date - today).days / 365.25

            if duration_years <= 1:
                duration = '단기'
            elif 1 < duration_years <= 5:
                duration = '중기'
            else:
                duration = '장기'
        else:
            duration = '미정'  # 만기일이 없을 경우 기본값 설정

        # 이자율을 기준으로 return_type 설정
        if interest_rate <= 3.0:
            return_type = '저수익'
        else:
            return_type = '고수익'

        # 날짜 형식 변환
        if issue_date:
            issue_date = datetime.strptime(issue_date, '%Y%m%d').date()

        # 변환된 데이터 레코드 추가
        records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type))

    return records
# 연금 데이터 변환 및 저장
def process_pension_data(data):
    records = []
    for item in data:
        name = item.get('fndNm', '')
        type_ = '연금'
        issuer = item.get('cmpyNm', '')
        issue_date = item.get('basDt', None)
        expiry_date = None
        price = float(item.get('basprc', 0))
        currency = 'KRW'
        category = '연금'
        risk_level = '낮음'
        interest_rate = None
        duration = '장기'
        return_type = '저수익'

        # 날짜 형식 변환
        if issue_date:
            issue_date = datetime.strptime(issue_date, '%Y%m%d').date()

        records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type))

    return records

# 데이터 저장 함수 (Batch Insert)
def save_to_db(records):
    try:
        cursor.executemany('''
        INSERT INTO financial_products (name, type, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ''', records)
        conn.commit()
        print(f"{len(records)}개의 데이터가 저장되었습니다.")
    except mysql.connector.Error as err:
        conn.rollback()
        print(f"데이터 삽입 오류: {err}")

# 각각의 데이터셋을 처리하고 저장
fund_records = process_fund_data(fund_data)
bond_records = process_bond_data(bond_data)
pension_records = process_pension_data(pension_data)

# 데이터 저장
save_to_db(fund_records)
save_to_db(bond_records)
save_to_db(pension_records)

# MySQL 연결 종료
cursor.close()
conn.close()