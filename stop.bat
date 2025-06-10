@echo off
chcp 65001 > nul
echo ========================================
echo    停止宿舍管理系统服务
echo ========================================
echo.

echo 正在停止Java后端服务...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do (
    echo 找到后端服务进程: %%a
    taskkill /f /pid %%a > nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ 后端服务已停止
    ) else (
        echo ❌ 停止后端服务失败
    )
)

echo 正在停止Node.js前端服务...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":5173" ^| find "LISTENING"') do (
    echo 找到前端服务进程: %%a
    taskkill /f /pid %%a > nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ 前端服务已停止
    ) else (
        echo ❌ 停止前端服务失败
    )
)

echo.
echo 正在清理可能残留的Java和Node进程...
taskkill /f /im java.exe > nul 2>&1
taskkill /f /im node.exe > nul 2>&1

echo ========================================
echo    🎉 服务停止完成！
echo ========================================
echo.
pause