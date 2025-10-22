#!/bin/bash

# Heroku 배포 스크립트
# Weather Service API를 Heroku에 배포합니다

echo "=================================="
echo "Weather Service - Heroku 배포 스크립트"
echo "=================================="
echo ""

# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Heroku CLI 설치 확인
echo -e "${YELLOW}[1/8] Heroku CLI 설치 확인...${NC}"
if ! command -v heroku &> /dev/null; then
    echo -e "${RED}❌ Heroku CLI가 설치되어 있지 않습니다.${NC}"
    echo "설치 방법:"
    echo "  Windows: https://devcenter.heroku.com/articles/heroku-cli"
    echo "  macOS: brew tap heroku/brew && brew install heroku"
    echo "  Linux: curl https://cli-assets.heroku.com/install.sh | sh"
    exit 1
fi
echo -e "${GREEN}✓ Heroku CLI 설치됨${NC}"
echo ""

# Heroku 로그인 확인
echo -e "${YELLOW}[2/8] Heroku 로그인 확인...${NC}"
if ! heroku auth:whoami &> /dev/null; then
    echo -e "${RED}❌ Heroku에 로그인되어 있지 않습니다.${NC}"
    echo "다음 명령어로 로그인하세요:"
    echo "  heroku login"
    exit 1
fi
HEROKU_USER=$(heroku auth:whoami)
echo -e "${GREEN}✓ 로그인됨: ${HEROKU_USER}${NC}"
echo ""

# 앱 이름 입력
echo -e "${YELLOW}[3/8] Heroku 앱 설정...${NC}"
read -p "Heroku 앱 이름을 입력하세요 (예: my-weather-service): " APP_NAME

if [ -z "$APP_NAME" ]; then
    echo -e "${RED}❌ 앱 이름이 필요합니다.${NC}"
    exit 1
fi

# 앱 존재 여부 확인
if heroku apps:info --app $APP_NAME &> /dev/null; then
    echo -e "${GREEN}✓ 기존 앱 사용: ${APP_NAME}${NC}"
else
    echo "새 앱을 생성합니다: ${APP_NAME}"
    if heroku create $APP_NAME; then
        echo -e "${GREEN}✓ 앱 생성 완료${NC}"
    else
        echo -e "${RED}❌ 앱 생성 실패${NC}"
        exit 1
    fi
fi
echo ""

# PostgreSQL 애드온 확인/추가
echo -e "${YELLOW}[4/8] PostgreSQL 데이터베이스 설정...${NC}"
if heroku addons --app $APP_NAME | grep -q "heroku-postgresql"; then
    echo -e "${GREEN}✓ PostgreSQL이 이미 설치되어 있습니다${NC}"
else
    echo "PostgreSQL 애드온을 추가합니다..."
    if heroku addons:create heroku-postgresql:essential-0 --app $APP_NAME; then
        echo -e "${GREEN}✓ PostgreSQL 추가 완료${NC}"
        echo "데이터베이스 초기화를 기다립니다 (약 30초)..."
        sleep 30
    else
        echo -e "${RED}❌ PostgreSQL 추가 실패${NC}"
        exit 1
    fi
fi
echo ""

# 환경 변수 설정
echo -e "${YELLOW}[5/8] 환경 변수 설정...${NC}"
read -p "OpenWeatherMap API Key를 입력하세요: " API_KEY

if [ -z "$API_KEY" ]; then
    echo -e "${RED}❌ API Key가 필요합니다.${NC}"
    echo "OpenWeatherMap API Key는 https://openweathermap.org/api 에서 발급받을 수 있습니다."
    exit 1
fi

echo "환경 변수를 설정합니다..."
heroku config:set OPENWEATHER_API_KEY=$API_KEY --app $APP_NAME
heroku config:set SPRING_PROFILE=prod --app $APP_NAME
heroku config:set JAVA_OPTS="-Xmx300m -Xss512k -XX:CICompilerCount=2" --app $APP_NAME

echo -e "${GREEN}✓ 환경 변수 설정 완료${NC}"
echo ""

# Git 저장소 확인
echo -e "${YELLOW}[6/8] Git 저장소 확인...${NC}"
if [ ! -d .git ]; then
    echo "Git 저장소를 초기화합니다..."
    git init
    git add .
    git commit -m "Initial commit for Heroku deployment"
    echo -e "${GREEN}✓ Git 저장소 생성 완료${NC}"
else
    echo -e "${GREEN}✓ Git 저장소 존재${NC}"
    
    # 변경사항 커밋
    if ! git diff-index --quiet HEAD --; then
        echo "변경사항을 커밋합니다..."
        git add .
        git commit -m "Deploy to Heroku - $(date '+%Y-%m-%d %H:%M:%S')"
    fi
fi

# Heroku remote 추가
if ! git remote | grep -q heroku; then
    echo "Heroku remote를 추가합니다..."
    heroku git:remote --app $APP_NAME
    echo -e "${GREEN}✓ Heroku remote 추가됨${NC}"
fi
echo ""

# 배포
echo -e "${YELLOW}[7/8] Heroku에 배포 중...${NC}"
echo "이 과정은 몇 분이 걸릴 수 있습니다..."
echo ""

if git push heroku main 2>/dev/null || git push heroku master 2>/dev/null; then
    echo -e "${GREEN}✓ 배포 완료!${NC}"
else
    echo -e "${RED}❌ 배포 실패${NC}"
    echo "로그를 확인하세요: heroku logs --tail --app $APP_NAME"
    exit 1
fi
echo ""

# 배포 확인
echo -e "${YELLOW}[8/8] 배포 확인...${NC}"
sleep 5

APP_URL="https://${APP_NAME}.herokuapp.com"
echo ""
echo -e "${GREEN}=================================="
echo "✓ 배포 성공!"
echo "==================================${NC}"
echo ""
echo "앱 정보:"
echo "  - 앱 이름: $APP_NAME"
echo "  - URL: $APP_URL"
echo "  - Swagger: ${APP_URL}/swagger-ui.html"
echo "  - Health Check: ${APP_URL}/api/v1/health"
echo ""
echo "유용한 명령어:"
echo "  - 로그 확인: heroku logs --tail --app $APP_NAME"
echo "  - 앱 열기: heroku open --app $APP_NAME"
echo "  - 앱 재시작: heroku restart --app $APP_NAME"
echo "  - 환경 변수 확인: heroku config --app $APP_NAME"
echo "  - DB 접속: heroku pg:psql --app $APP_NAME"
echo ""
echo -e "${YELLOW}브라우저에서 앱을 여시겠습니까? (y/n)${NC}"
read -p "> " OPEN_BROWSER

if [ "$OPEN_BROWSER" = "y" ] || [ "$OPEN_BROWSER" = "Y" ]; then
    heroku open --app $APP_NAME
fi

echo ""
echo -e "${GREEN}배포가 완료되었습니다! 🎉${NC}"

