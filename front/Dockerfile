# 베이스 이미지 선택 (Node.js 18)
FROM node:18

# 앱 디렉토리 생성 및 작업 디렉토리로 설정
WORKDIR /usr/src/app

# 앱 종속성 설치
COPY package*.json ./
RUN npm install

# 앱 소스 복사
COPY . .

# 빌드
RUN npm run build

# 포트 3000으로 노출
EXPOSE 3000

# 앱 실행
CMD [ "npm", "run", "start" ]