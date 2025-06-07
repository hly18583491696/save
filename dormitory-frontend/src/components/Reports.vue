<template>
  <div class="reports-container">
    <div class="page-header">
      <h2>统计报表</h2>
      <p>查看宿舍管理系统的各项统计数据和报表</p>
    </div>
    
    <div class="reports-grid">
      <!-- 入住率统计 -->
      <div class="report-card">
        <div class="card-header">
          <h3>入住率统计</h3>
          <div class="card-actions">
            <button class="btn btn-primary">导出报表</button>
          </div>
        </div>
        <div class="card-content">
          <div class="chart-placeholder">
            <i class="fas fa-chart-pie"></i>
            <p>入住率统计图表</p>
          </div>
          <div class="stats-summary">
            <div class="stat-item">
              <span class="label">总入住率</span>
              <span class="value">85%</span>
            </div>
            <div class="stat-item">
              <span class="label">男生宿舍入住率</span>
              <span class="value">88%</span>
            </div>
            <div class="stat-item">
              <span class="label">女生宿舍入住率</span>
              <span class="value">82%</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 学生分布统计 -->
      <div class="report-card">
        <div class="card-header">
          <h3>学生分布统计</h3>
          <div class="card-actions">
            <button class="btn btn-primary">导出报表</button>
          </div>
        </div>
        <div class="card-content">
          <div class="chart-placeholder">
            <i class="fas fa-chart-bar"></i>
            <p>学生分布统计图表</p>
          </div>
          <div class="stats-summary">
            <div class="stat-item">
              <span class="label">在校学生总数</span>
              <span class="value">450人</span>
            </div>
            <div class="stat-item">
              <span class="label">已分配宿舍</span>
              <span class="value">380人</span>
            </div>
            <div class="stat-item">
              <span class="label">待分配宿舍</span>
              <span class="value">70人</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 宿舍使用情况 -->
      <div class="report-card">
        <div class="card-header">
          <h3>宿舍使用情况</h3>
          <div class="card-actions">
            <button class="btn btn-primary">导出报表</button>
          </div>
        </div>
        <div class="card-content">
          <div class="chart-placeholder">
            <i class="fas fa-chart-line"></i>
            <p>宿舍使用趋势图表</p>
          </div>
          <div class="stats-summary">
            <div class="stat-item">
              <span class="label">总房间数</span>
              <span class="value">120间</span>
            </div>
            <div class="stat-item">
              <span class="label">已使用房间</span>
              <span class="value">102间</span>
            </div>
            <div class="stat-item">
              <span class="label">空闲房间</span>
              <span class="value">18间</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 维修统计 -->
      <div class="report-card">
        <div class="card-header">
          <h3>维修统计</h3>
          <div class="card-actions">
            <button class="btn btn-primary">导出报表</button>
          </div>
        </div>
        <div class="card-content">
          <div class="chart-placeholder">
            <i class="fas fa-tools"></i>
            <p>维修统计图表</p>
          </div>
          <div class="stats-summary">
            <div class="stat-item">
              <span class="label">本月维修申请</span>
              <span class="value">15件</span>
            </div>
            <div class="stat-item">
              <span class="label">已完成维修</span>
              <span class="value">12件</span>
            </div>
            <div class="stat-item">
              <span class="label">待处理维修</span>
              <span class="value">3件</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 详细报表表格 -->
    <div class="detailed-reports">
      <div class="section-header">
        <h3>详细报表</h3>
        <div class="filter-controls">
          <select class="form-select">
            <option>本月</option>
            <option>本季度</option>
            <option>本年度</option>
          </select>
          <button class="btn btn-outline">筛选</button>
        </div>
      </div>
      
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>日期</th>
              <th>入住人数</th>
              <th>退宿人数</th>
              <th>维修申请</th>
              <th>入住率</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>2024-01-15</td>
              <td>5</td>
              <td>2</td>
              <td>3</td>
              <td>85%</td>
            </tr>
            <tr>
              <td>2024-01-14</td>
              <td>3</td>
              <td>1</td>
              <td>1</td>
              <td>84%</td>
            </tr>
            <tr>
              <td>2024-01-13</td>
              <td>7</td>
              <td>0</td>
              <td>2</td>
              <td>86%</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Reports',
  setup() {
    return {
      // 可以在这里添加报表相关的数据和方法
    }
  }
}
</script>

<style scoped>
.reports-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
}

.page-header p {
  margin: 0;
  color: #6b7280;
  font-size: 16px;
}

.reports-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.report-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
  background: #f9fafb;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.card-content {
  padding: 24px;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  background: #f9fafb;
  border-radius: 8px;
  margin-bottom: 20px;
  color: #9ca3af;
}

.chart-placeholder i {
  font-size: 48px;
  margin-bottom: 12px;
}

.stats-summary {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f3f4f6;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-item .label {
  color: #6b7280;
  font-size: 14px;
}

.stat-item .value {
  font-weight: 600;
  color: #1f2937;
  font-size: 16px;
}

.detailed-reports {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
  background: #f9fafb;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.filter-controls {
  display: flex;
  gap: 12px;
  align-items: center;
}

.form-select {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  background: white;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #f3f4f6;
}

.data-table th {
  background: #f9fafb;
  font-weight: 600;
  color: #374151;
  font-size: 14px;
}

.data-table td {
  color: #6b7280;
  font-size: 14px;
}

.data-table tbody tr:hover {
  background: #f9fafb;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-outline {
  background: transparent;
  color: #374151;
  border: 1px solid #d1d5db;
}

.btn-outline:hover {
  background: #f3f4f6;
}

@media (max-width: 768px) {
  .reports-grid {
    grid-template-columns: 1fr;
  }
  
  .section-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .filter-controls {
    justify-content: flex-end;
  }
}
</style>