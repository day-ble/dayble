#!/bin/bash

DEPLOY_BASE_PATH=/home/ubuntu/itcast
LOG_PATH=/home/ubuntu/deploy.log
ERR_LOG_PATH=/home/ubuntu/deploy_err.log

declare -A MODULES
MODULES=(
  ["admin"]="admin"
  ["b2c"]="b2c"
  ["schedule"]="schedule"
)

declare -A PORTS
PORTS=(
  ["admin"]="8082"
  ["b2c"]="8080"
  ["schedule"]="8081"
)

echo "$(date) >>> 배포 시작" >> $LOG_PATH

for MODULE in "${!MODULES[@]}"
do
  MODULE_NAME=${MODULES[$MODULE]}
  MODULE_DEPLOY_PATH=$DEPLOY_BASE_PATH/$MODULE_NAME
  PORT=${PORTS[$MODULE]}

  echo ">>> $MODULE_NAME 배포 시작" >> $LOG_PATH

  # 배포 경로 생성
  mkdir -p $MODULE_DEPLOY_PATH
  echo ">>> $MODULE_NAME 디렉토리 생성 완료" >> $LOG_PATH

  # 빌드된 JAR 파일 찾기
  BUILD_JAR=$(ls /home/ubuntu/itcast/$MODULE_NAME/build/libs/*.jar 2>> $ERR_LOG_PATH)
  if [ ! -f "$BUILD_JAR" ]; then
    echo ">>> $MODULE_NAME JAR 파일을 찾을 수 없습니다. 배포 중단." >> $ERR_LOG_PATH
    continue
  fi
  JAR_NAME=$(basename $BUILD_JAR)
  echo ">>> $MODULE_NAME build 파일명: $JAR_NAME" >> $LOG_PATH

  # JAR 파일 복사
  cp $BUILD_JAR $MODULE_DEPLOY_PATH 2>> $ERR_LOG_PATH
  if [ $? -ne 0 ]; then
    echo ">>> $MODULE_NAME JAR 복사 실패. 배포 중단." >> $ERR_LOG_PATH
    continue
  fi
  echo ">>> $MODULE_NAME build 파일 복사 완료" >> $LOG_PATH

  # 실행 중인 애플리케이션 종료
  echo ">>> $MODULE_NAME 실행 중인 애플리케이션 종료" >> $LOG_PATH
  PID=$(lsof -t -i:$PORT)
  if [ -n "$PID" ]; then
    kill -15 $PID
    sleep 5  # 프로세스 종료 대기
    PID_CHECK=$(lsof -t -i:$PORT)
    if [ -n "$PID_CHECK" ]; then
      echo ">>> $MODULE_NAME 강제 종료 중 (PID: $PID_CHECK)" >> $LOG_PATH
      kill -9 $PID_CHECK
    fi
    echo ">>> $MODULE_NAME 기존 프로세스 종료 완료" >> $LOG_PATH
  else
    echo ">>> $MODULE_NAME 실행 중인 프로세스 없음" >> $LOG_PATH
  fi

  # 새 JAR 실행
  DEPLOY_JAR=$MODULE_DEPLOY_PATH/$JAR_NAME
  echo ">>> $MODULE_NAME 새 JAR 실행: $DEPLOY_JAR" >> $LOG_PATH
  nohup java -jar -Dspring.profiles.active=prod \
      -Dserver.port=$PORT $DEPLOY_JAR >> $MODULE_DEPLOY_PATH/deploy.log 2>> $MODULE_DEPLOY_PATH/deploy_err.log &
  if [ $? -eq 0 ]; then
    echo ">>> $MODULE_NAME 배포 완료 및 실행 성공" >> $LOG_PATH
  else
    echo ">>> $MODULE_NAME 실행 실패" >> $ERR_LOG_PATH
  fi
done

echo "$(date) >>> 전체 배포 완료" >> $LOG_PATH
