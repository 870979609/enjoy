import request from '@/utils/request'


/**
 *
 axios({
    url: '连接地址 path参数直接放里面',
    method: 'post 默认是 get',
    params: '必须是一个无格式对象 query参数',
    data: '是作为请求主体被发送的数据 body参数',
    timeout: 1000,
    headers: 'object 发送的自定义请求头'
})
 * @param params
 */
export function listAll(params) {
  return request({
    url: '/articles',
    method: 'get'
  })
}

export function fetchArticle(params) {
  return request({
    url: '/article/'+ params,
    method: 'get'
  })
}

export function createArticle(data) {
  return request({
    url: '/article',
    method: 'post',
    data: data
  })
}

export function getMyCategorysAndTags(params) {
  return request({
    url: '/categorys',
    method: 'get'
  })
}

