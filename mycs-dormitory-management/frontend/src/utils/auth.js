const TokenKey = 'mycs_token'

export function getToken() {
  return localStorage.getItem(TokenKey)
}

export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return localStorage.removeItem(TokenKey)
}

// 检查是否有token
export function hasToken() {
  return !!getToken()
}

// 解析JWT token（简单解析，不验证签名）
export function parseToken(token) {
  if (!token) return null
  
  try {
    const payload = token.split('.')[1]
    const decoded = JSON.parse(atob(payload))
    return decoded
  } catch (error) {
    console.error('Token解析失败:', error)
    return null
  }
}

// 检查token是否过期
export function isTokenExpired(token) {
  const decoded = parseToken(token)
  if (!decoded || !decoded.exp) return true
  
  const currentTime = Math.floor(Date.now() / 1000)
  return decoded.exp < currentTime
}