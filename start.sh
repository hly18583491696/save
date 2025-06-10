#!/bin/bash

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    宿舍管理系统一键启动脚本${NC}"
echo -e "${BLUE}========================================${NC}"
echo

echo -e "${YELLOW}[1/4] 检查环境...${NC}"
echo "检查Java环境..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java环境未安装或未配置到PATH${NC}"
    echo "请安装Java 21或更高版本"
    exit 1
fi

echo "检查Node.js环境..."
if ! command -v node &> /dev/null; then
    echo -e "${RED}❌ Node.js环境未安装或未配置到PATH${NC}"
    echo "请安装Node.js 16或更高版本"
    exit 1
fi

echo "检查npm环境..."
if ! command -v npm &> /dev/null; then
    echo -e "${RED}❌ npm未安装或未配置到PATH${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 环境检查完成${NC}"
echo

echo -e "${YELLOW}[2/4] 安装前端依赖...${NC}"
cd dormitory-frontend
if [ ! -d "node_modules" ]; then
    echo "正在安装前端依赖，请稍候..."
    npm install
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ 前端依赖安装失败${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ 前端依赖安装完成${NC}"
else
    echo -e "${GREEN}✅ 前端依赖已存在，跳过安装${NC}"
fi
cd ..
echo

echo -e "${YELLOW}[3/4] 启动后端服务...${NC}"
echo "正在启动Spring Boot后端服务..."
# 检查操作系统类型
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
    # Windows (Git Bash)
    ./mvnw.cmd spring-boot:run &
else
    # Linux/Mac
    ./mvnw spring-boot:run &
fi
BACKEND_PID=$!
echo -e "${GREEN}✅ 后端服务启动中... (PID: $BACKEND_PID)${NC}"
echo "后端服务地址: http://localhost:8080"
echo

echo "等待后端服务启动..."
sleep 10
echo

echo -e "${YELLOW}[4/4] 启动前端服务...${NC}"
echo "正在启动Vue前端服务..."
cd dormitory-frontend
npm run dev &
FRONTEND_PID=$!
echo -e "${GREEN}✅ 前端服务启动中... (PID: $FRONTEND_PID)${NC}"
echo "前端服务地址: http://localhost:5173"
cd ..
echo

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}    🎉 启动完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo "后端服务: http://localhost:8080"
echo "前端服务: http://localhost:5173"
echo
echo -e "${YELLOW}💡 提示:${NC}"
echo "- 请确保MySQL数据库已启动"
echo "- 首次启动可能需要等待依赖下载"
echo "- 按 Ctrl+C 停止所有服务"
echo -e "${BLUE}========================================${NC}"
echo

# 保存PID到文件，方便后续停止
echo $BACKEND_PID > .backend.pid
echo $FRONTEND_PID > .frontend.pid

# 等待用户中断
trap 'echo -e "\n${YELLOW}正在停止服务...${NC}"; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; rm -f .backend.pid .frontend.pid; echo -e "${GREEN}服务已停止${NC}"; exit 0' INT

# 保持脚本运行
wait