import request from '@/utils/request'

export function getCodeByDmbh(params) {
  return request({
    url: '/commonapi/code',
    method: 'get',
    params:{
      "dmbh": params
    }
  })
}
