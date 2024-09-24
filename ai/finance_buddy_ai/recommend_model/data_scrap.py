import time

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
    try:
        response = requests.get(url, params=params, verify=False)  # SSL 검증 비활성화
        if response.status_code == 200:
            try:
                data = response.json().get('response', {}).get('body', {}).get('items', {}).get('item', [])
                return data if isinstance(data, list) else [data]
            except json.JSONDecodeError:
                print(response)
                print(f"JSON 디코딩 오류: 응답 내용을 확인하세요. 응답 내용: {response.text}")
                return []
        else:
            print(f"API 요청 실패: {response.status_code}")
            return []
    except requests.exceptions.RequestException as e:
        print(f"요청 예외 발생: {e}")
        return []

# API URL 및 파라미터 설정
bond_url = "http://apis.data.go.kr/1160100/service/GetBondIssuInfoService/getBondBasiInfo?pageNo=1&numOfRows=15000&resultType=json&serviceKey=key"
fund_url = "http://apis.data.go.kr/1160100/service/GetFundProductInfoService/getStandardCodeInfo?serviceKey=key&resultType=json&pageNo=1&numOfRows=15000"
pension_url = "http://apis.data.go.kr/1160100/service/GetRetirementPensionInfoService/getFundInfo?serviceKey=keyresultType=json&pageNo=1&numOfRows=15000"





# API 데이터 가져오기
bond_data = fetch_data_from_api(bond_url, None)
# 채권 데이터 변환 및 저장
def process_bond_data(bond_data):
    bond_records = []
    today = datetime.today().date()

    for item in bond_data:
        name = item.get('isinCdNm', '')
        type_ = '채권'
        issuer = item.get('bondIsurNm', '')
        issue_date = item.get('bondIssuDt', None)
        expiry_date = item.get('bondExprDt', None)
        price = None
        currency = item.get('bondIssuCurCdNm', 'KRW')
        category = item.get('scrsItmsKcdNm', '')
        grnDcdNm = item.get('grnDcdNm', 'N/A')
        interest_rate = float(item.get('bondSrfcInrt', 0))

        # 위험 수준 설정
        if grnDcdNm == '무보증':
            risk_level = '고위험'
        elif grnDcdNm == '일반':
            risk_level = '위험'
        else:
            risk_level = 'N/A'

        # 만기일을 기준으로 duration 설정
        try:
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
                duration = '미정'
        except ValueError:
            print(f"Invalid date format for bond: {expiry_date}")
            duration = '미정'

        # 이자율을 기준으로 return_type 설정
        return_type = '저수익' if interest_rate <= 3.0 else '고수익'

        # 날짜 형식 변환
        try:
            if issue_date:
                issue_date = datetime.strptime(issue_date, '%Y%m%d').date()
        except ValueError:
            print(f"Invalid date format for issue_date: {issue_date}")
            issue_date = None

        bond_records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type))

    return bond_records

# 채권 데이터 저장 함수
def save_bond_to_db(bond_records):
    try:
        cursor.executemany('''
        INSERT INTO financial_products (name, type, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ''', bond_records)
        conn.commit()
        print(f"{len(bond_records)}개의 채권 데이터가 저장되었습니다.")
    except mysql.connector.Error as err:
        conn.rollback()
        print(f"데이터 삽입 오류: {err}")

pension_data = fetch_data_from_api(pension_url, None)

# 연금 데이터 변환 및 저장
def process_pension_data(pension_data):
    pension_records = []
    for item in pension_data:
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

        pension_records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type))

    return pension_records

# 연금 데이터 저장 함수
def save_pension_to_db(pension_records):
    try:
        cursor.executemany('''
        INSERT INTO financial_products (name, type, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ''', pension_records)
        conn.commit()
        print(f"{len(pension_records)}개의 연금 데이터가 저장되었습니다.")
    except mysql.connector.Error as err:
        conn.rollback()
        print(f"데이터 삽입 오류: {err}")


fund_data = fetch_data_from_api(fund_url, None)
# 펀드 데이터 변환 및 저장
def process_fund_data(fund_data):
    fund_records = []
    for item in fund_data:
        name = item.get('fndNm', '')
        type_ = '펀드'
        issuer = None  # 실제 발행사 정보는 제공되지 않음
        issue_date = item.get('setpDt', None)
        expiry_date = None
        price = None
        currency = 'KRW'
        category = item.get('ctg', '')

        # 펀드 유형에 따른 속성 설정
        fnd_type = item.get('fndTp', '')

        if fnd_type == '파생형':
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


        # 날짜 형식 변환
        if issue_date:
            issue_date = datetime.strptime(issue_date, '%Y%m%d').date()

        fund_records.append((name, type_, issuer, issue_date, expiry_date, price, currency, category, risk_level,
                             interest_rate, duration, return_type))

    return fund_records

# 펀드 데이터 저장 함수
def save_fund_to_db(fund_records):
    try:
        cursor.executemany('''
        INSERT INTO financial_products (name, type, issuer, issue_date, expiry_date, price, currency, category, risk_level, interest_rate, duration, return_type)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ''', fund_records)
        conn.commit()
        print(f"{len(fund_records)}개의 펀드 데이터가 저장되었습니다.")
    except mysql.connector.Error as err:
        conn.rollback()
        print(f"데이터 삽입 오류: {err}")


# 각각의 데이터셋을 처리하고 저장
fund_records = process_fund_data(fund_data)
save_fund_to_db(fund_records)

bond_records = process_bond_data(bond_data)
save_bond_to_db(bond_records)

pension_records = process_pension_data(pension_data)
save_pension_to_db(pension_records)

# MySQL 연결 종료
cursor.close()
conn.close()