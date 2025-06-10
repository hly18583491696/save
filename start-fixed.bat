@echo off
chcp 65001 > nul
echo ========================================
echo    宿舍管理系统修复版启动脚本
echo ========================================
echo.

echo [1/3] 检查环境...
echo 检查Java环境...
java -version > nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java环境未安装或未配置到PATH
    echo 请安装Java 21或更高版本
    pause
    exit /b 1
)

echo 检查Node.js环境...
node -v > nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node.js环境未安装或未配置到PATH
    echo 请安装Node.js 16或更高版本
    pause
    exit /b 1
)

echo ✅ 环境检查完成
echo.

echo [2/3] 启动后端服务...
echo 正在启动Spring Boot后端服务...
echo 使用已编译的JAR文件启动...
start "后端服务" cmd /k "java -jar target\mycs-dormitory-management-0.0.1-SNAPSHOT.jar"
echo ✅ 后端服务启动中...
echo 后端服务地址: http://localhost:8080
echo.

echo 等待后端服务启动...
timeout /t 15 /nobreak > nul
echo.

echo [3/3] 启动前端服务...
echo 正在启动Vue前端服务...
cd dormitory-frontend
if not exist node_modules (
    echo 正在安装前端依赖，请稍候...
    npm install
    if %errorlevel% neq 0 (
        echo ❌ 前端依赖安装失败
        pause
        exit /b 1
    )
)
start "前端服务" cmd /k "npm run dev"
echo ✅ 前端服务启动中...
echo 前端服务地址: http://localhost:5173
cd ..
echo.

echo ========================================
echo    🎉 启动完成！
echo ========================================
echo 后端服务: http://localhost:8080
echo 前端服务: http://localhost:5173
echo.
echo 💡 提示:
echo - 请确保MySQL数据库已启动
echo - 如果后端启动失败，请先运行: mvnw.cmd clean package
echo - 关闭此窗口不会停止服务
echo - 要停止服务请关闭对应的命令行窗口
echo ========================================
echo.
pause