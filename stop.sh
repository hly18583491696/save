#!/bin/bash

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}    停止宿舍管理系统服务${NC}"
echo -e "${BLUE}========================================${NC}"
echo

echo -e "${YELLOW}正在停止服务...${NC}"

# 从PID文件停止服务
if [ -f ".backend.pid" ]; then
    BACKEND_PID=$(cat .backend.pid)
    if kill -0 $BACKEND_PID 2>/dev/null; then
        echo "停止后端服务 (PID: $BACKEND_PID)..."
        kill $BACKEND_PID
        echo -e "${GREEN}✅ 后端服务已停止${NC}"
    else
        echo -e "${YELLOW}后端服务进程不存在${NC}"
    fi
    rm -f .backend.pid
fi

if [ -f ".frontend.pid" ]; then
    FRONTEND_PID=$(cat .frontend.pid)
    if kill -0 $FRONTEND_PID 2>/dev/null; then
        echo "停止前端服务 (PID: $FRONTEND_PID)..."
        kill $FRONTEND_PID
        echo -e "${GREEN}✅ 前端服务已停止${NC}"
    else
        echo -e "${YELLOW}前端服务进程不存在${NC}"
    fi
    rm -f .frontend.pid
fi

# 通过端口停止服务
echo "检查端口占用情况..."

# 停止8080端口的服务（后端）
BACKEND_PID=$(lsof -ti:8080 2>/dev/null)
if [ ! -z "$BACKEND_PID" ]; then
    echo "发现后端服务占用8080端口 (PID: $BACKEND_PID)"
    kill -9 $BACKEND_PID 2>/dev/null
    echo -e "${GREEN}✅ 后端服务已强制停止${NC}"
fi

# 停止5173端口的服务（前端）
FRONTEND_PID=$(lsof -ti:5173 2>/dev/null)
if [ ! -z "$FRONTEND_PID" ]; then
    echo "发现前端服务占用5173端口 (PID: $FRONTEND_PID)"
    kill -9 $FRONTEND_PID 2>/dev/null
    echo -e "${GREEN}✅ 前端服务已强制停止${NC}"
fi

# 清理可能的残留进程
echo "清理可能的残留进程..."
pkill -f "spring-boot:run" 2>/dev/null
pkill -f "vite" 2>/dev/null
pkill -f "npm run dev" 2>/dev/null

echo
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}    🎉 服务停止完成！${NC}"
echo -e "${BLUE}========================================${NC}"
echo