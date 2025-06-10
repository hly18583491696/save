<template>
  <div class="delete-diagnostic">
    <div class="page-header">
      <h2><i class="fas fa-bug"></i> 删除操作诊断工具</h2>
      <p class="description">用于诊断住宿记录删除未同步到数据库的问题</p>
    </div>

    <div class="diagnostic-sections">
      <!-- 诊断结果 -->
      <div class="section">
        <div class="section-header">
          <h3><i class="fas fa-search"></i> 删除操作诊断</h3>
          <button class="btn btn-primary" @click="runDiagnostic" :disabled="loading">
            <i class="fas fa-play"></i> 运行诊断
          </button>
        </div>
        
        <div v-if="diagnosticResult" class="diagnostic-result">
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-label">总记录数</div>
              <div class="stat-value">{{ diagnosticResult.totalRecords }}</div>
            </div>
            <div class="stat-card">
              <div class="stat-label">活跃记录</div>
              <div class="stat-value">{{ diagnosticResult.activeRecords }}</div>
            </div>
            <div class="stat-card">
              <div class="stat-label">已删除记录</div>
              <div class="stat-value">{{ diagnosticResult.deletedRecords }}</div>
            </div>
            <div class="stat-card">
              <div class="stat-label">默认查询记录</div>
              <div class="stat-value">{{ diagnosticResult.defaultQueryRecords }}</div>
            </div>
          </div>
          
          <div class="analysis-section">
            <h4>分析结果</h4>
            <ul class="analysis-list">
              <li v-for="(issue, index) in diagnosticResult.analysis" :key="index">
                {{ issue }}
              </li>
            </ul>
          </div>
          
          <div v-if="diagnosticResult.recentDeletedRecords && diagnosticResult.recentDeletedRecords.length > 0" class="recent-deleted">
            <h4>最近删除的记录</h4>
            <div class="table-container">
              <table class="diagnostic-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>学生姓名</th>
                    <th>学号</th>
                    <th>删除状态</th>
                    <th>更新时间</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="record in diagnosticResult.recentDeletedRecords" :key="record.id">
                    <td>{{ record.id }}</td>
                    <td>{{ record.studentName }}</td>
                    <td>{{ record.studentNumber }}</td>
                    <td>
                      <span class="status-badge" :class="record.deleted === 1 ? 'deleted' : 'active'">
                        {{ record.deleted === 1 ? '已删除' : '活跃' }}
                      </span>
                    </td>
                    <td>{{ formatDateTime(record.updateTime) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- 测试删除操作 -->
      <div class="section">
        <div class="section-header">
          <h3><i class="fas fa-flask"></i> 测试删除操作</h3>
        </div>
        
        <div class="test-form">
          <div class="form-group">
            <label>住宿记录ID</label>
            <input 
              v-model="testId" 
              type="number" 
              placeholder="输入要测试删除的住宿记录ID"
              class="form-control"
            >
          </div>
          <button 
            class="btn btn-warning" 
            @click="testDelete" 
            :disabled="!testId || loading"
          >
            <i class="fas fa-test-tube"></i> 测试删除
          </button>
        </div>
        
        <div v-if="testResult" class="test-result">
          <h4>测试结果</h4>
          
          <div v-if="testResult.error" class="error-message">
            <i class="fas fa-exclamation-triangle"></i>
            {{ testResult.error }}
          </div>
          
          <div v-else class="test-details">
            <div class="detail-section">
              <h5>删除前状态</h5>
              <div v-if="testResult.beforeDelete" class="record-info">
                <p><strong>ID:</strong> {{ testResult.beforeDelete.id }}</p>
                <p><strong>学生姓名:</strong> {{ testResult.beforeDelete.studentName }}</p>
                <p><strong>删除状态:</strong> {{ testResult.beforeDelete.deleted }}</p>
                <p><strong>更新时间:</strong> {{ formatDateTime(testResult.beforeDelete.updateTime) }}</p>
              </div>
            </div>
            
            <div class="detail-section">
              <h5>删除操作结果</h5>
              <p><strong>受影响行数:</strong> {{ testResult.deleteResult }}</p>
            </div>
            
            <div class="detail-section">
              <h5>删除后状态</h5>
              <div v-if="testResult.afterDelete" class="record-info">
                <p><strong>ID:</strong> {{ testResult.afterDelete.id }}</p>
                <p><strong>学生姓名:</strong> {{ testResult.afterDelete.studentName }}</p>
                <p><strong>删除状态:</strong> {{ testResult.afterDelete.deleted }}</p>
                <p><strong>更新时间:</strong> {{ formatDateTime(testResult.afterDelete.updateTime) }}</p>
              </div>
              <div v-else class="no-record">
                <p>记录不存在或无法查询</p>
              </div>
            </div>
            
            <div class="detail-section">
              <h5>默认查询结果</h5>
              <p>{{ testResult.defaultQueryResult }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 解决方案建议 -->
      <div class="section">
        <div class="section-header">
          <h3><i class="fas fa-lightbulb"></i> 问题分析与解决方案</h3>
        </div>
        
        <div class="solutions">
          <div class="solution-card">
            <h4>问题1: 前端显示未更新</h4>
            <p>如果数据库中记录已被逻辑删除，但前端列表仍显示该记录，可能是：</p>
            <ul>
              <li>前端缓存未刷新</li>
              <li>前端查询API未正确过滤已删除记录</li>
              <li>前端删除成功后未重新加载列表</li>
            </ul>
            <p><strong>解决方案：</strong>检查前端删除成功后是否调用了 <code>loadAccommodations()</code> 方法</p>
          </div>
          
          <div class="solution-card">
            <h4>问题2: 逻辑删除配置问题</h4>
            <p>如果删除操作返回成功但数据库记录的deleted字段未更新，可能是：</p>
            <ul>
              <li>MyBatis-Plus逻辑删除配置不正确</li>
              <li>@TableLogic注解配置有误</li>
              <li>数据库表结构与实体类不匹配</li>
            </ul>
            <p><strong>解决方案：</strong>检查 <code>@TableLogic</code> 注解和MyBatis-Plus配置</p>
          </div>
          
          <div class="solution-card">
            <h4>问题3: 事务回滚</h4>
            <p>如果删除操作过程中发生异常导致事务回滚：</p>
            <ul>
              <li>检查删除操作是否抛出异常</li>
              <li>查看应用日志中的错误信息</li>
              <li>确认数据库连接和事务配置</li>
            </ul>
            <p><strong>解决方案：</strong>查看后端日志，确认删除操作是否真正执行</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'DeleteDiagnostic',
  setup() {
    const loading = ref(false)
    const diagnosticResult = ref(null)
    const testId = ref('')
    const testResult = ref(null)
    
    const runDiagnostic = async () => {
      loading.value = true
      try {
        const response = await fetch('http://localhost:8082/api/diagnostic/deletion-issues')
        const result = await response.json()
        
        if (result.success) {
          diagnosticResult.value = result.data
        } else {
          alert('诊断失败: ' + result.message)
        }
      } catch (error) {
        console.error('诊断失败:', error)
        alert('诊断失败: ' + error.message)
      } finally {
        loading.value = false
      }
    }
    
    const testDelete = async () => {
      if (!testId.value) {
        alert('请输入住宿记录ID')
        return
      }
      
      loading.value = true
      try {
        const response = await fetch(`http://localhost:8082/api/diagnostic/test-delete/${testId.value}`, {
          method: 'POST'
        })
        const result = await response.json()
        
        if (result.success) {
          testResult.value = result.data
        } else {
          alert('测试失败: ' + result.message)
        }
      } catch (error) {
        console.error('测试失败:', error)
        alert('测试失败: ' + error.message)
      } finally {
        loading.value = false
      }
    }
    
    const formatDateTime = (dateTime) => {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
    
    return {
      loading,
      diagnosticResult,
      testId,
      testResult,
      runDiagnostic,
      testDelete,
      formatDateTime
    }
  }
}
</script>

<style scoped>
.delete-diagnostic {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-header h2 {
  color: #333;
  margin-bottom: 10px;
}

.description {
  color: #666;
  font-size: 16px;
}

.diagnostic-sections {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.section-header h3 {
  margin: 0;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.analysis-section {
  margin-bottom: 20px;
}

.analysis-list {
  list-style: none;
  padding: 0;
}

.analysis-list li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.analysis-list li:before {
  content: '✓';
  color: #28a745;
  margin-right: 8px;
}

.diagnostic-table {
  width: 100%;
  border-collapse: collapse;
}

.diagnostic-table th,
.diagnostic-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.diagnostic-table th {
  background: #f8f9fa;
  font-weight: 600;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.deleted {
  background: #dc3545;
  color: white;
}

.status-badge.active {
  background: #28a745;
  color: white;
}

.test-form {
  display: flex;
  gap: 15px;
  align-items: end;
  margin-bottom: 20px;
}

.form-group {
  flex: 1;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.test-details {
  display: grid;
  gap: 15px;
}

.detail-section {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
}

.detail-section h5 {
  margin: 0 0 10px 0;
  color: #333;
}

.record-info p {
  margin: 5px 0;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #f5c6cb;
}

.solutions {
  display: grid;
  gap: 20px;
}

.solution-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 6px;
  border-left: 4px solid #007bff;
}

.solution-card h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.solution-card ul {
  margin: 10px 0;
}

.solution-card code {
  background: #e9ecef;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: monospace;
}
</style>