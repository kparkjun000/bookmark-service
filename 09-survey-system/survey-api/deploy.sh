#!/bin/bash

# Heroku 배포 스크립트

echo "==================================="
echo "Survey API - Heroku 배포 스크립트"
echo "==================================="
echo ""

# 앱 이름
APP_NAME="survey-system-api"

# 1. Heroku 로그인 상태 확인
echo "1. Heroku 로그인 상태 확인..."
if ! heroku auth:whoami > /dev/null 2>&1; then
    echo "Heroku에 로그인되어 있지 않습니다."
    echo "heroku login 명령을 실행하세요."
    exit 1
fi
echo "✓ 로그인됨"
echo ""

# 2. Git 변경사항 확인
echo "2. Git 변경사항 확인..."
if [[ -n $(git status -s) ]]; then
    echo "변경사항이 있습니다. 커밋하시겠습니까? (y/n)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        git add .
        git commit -m "Update for deployment"
        echo "✓ 커밋 완료"
    fi
fi
echo ""

# 3. Heroku 앱 확인
echo "3. Heroku 앱 확인..."
if ! heroku apps:info -a $APP_NAME > /dev/null 2>&1; then
    echo "앱이 존재하지 않습니다."
    exit 1
fi
echo "✓ 앱 확인됨: $APP_NAME"
echo ""

# 4. Heroku에 배포
echo "4. Heroku에 배포 중..."
git push heroku master

if [ $? -eq 0 ]; then
    echo ""
    echo "==================================="
    echo "✓ 배포 완료!"
    echo "==================================="
    echo ""
    echo "앱 URL: https://$APP_NAME.herokuapp.com"
    echo "Swagger UI: https://$APP_NAME.herokuapp.com/swagger-ui.html"
    echo ""
    echo "로그 확인: heroku logs --tail -a $APP_NAME"
    echo "앱 열기: heroku open -a $APP_NAME"
else
    echo ""
    echo "==================================="
    echo "✗ 배포 실패"
    echo "==================================="
    echo ""
    echo "오류 로그를 확인하세요: heroku logs --tail -a $APP_NAME"
    exit 1
fi

