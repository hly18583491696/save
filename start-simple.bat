@echo off
chcp 65001 > nul
echo ========================================
echo    宿舍管理系统简化启动脚本
echo ========================================
echo.

echo 正在启动后端服务...
start "后端服务" cmd /k ".\mvnw.cmd spring-boot:run"
echo 后端服务启动中，请等待...
echo.

echo 等待10秒后启动前端服务...
timeout /t 10 /nobreak > nul
echo.

echo 正在启动前端服务...
cd dormitory-frontend
start "前端服务" cmd /k "npm run dev"
echo 前端服务启动中...
cd ..
echo.

echo ========================================
echo    🎉 启动完成！
echo ========================================
echo 后端服务: http://localhost:8080
echo 前端服务: http://localhost:5173
echo.
echo 💡 提示:
echo - 请确保已安装Java 21+和Node.js 16+
echo - 请确保MySQL数据库已启动
echo - 首次启动前端需要先运行: cd dormitory-frontend && npm install
echo - 要停止服务请关闭对应的命令行窗口
echo ========================================
echo.
pause