/* eslint-disable no-unused-vars */
/* eslint-disable camelcase */

// import http from './/src/http.js'
// import services from './/src/conf/services.js'

import http from '../../../../http.js'
import services from '../../../../conf/services.js'

// auto generated
export default {
  request_uesHmData: function (
    serviceRegon,
    startTime, endTime, rat, serviceType, deviceType, P1, P2, P3, P4,
    onSuccess, onError) {
    http[serviceRegon]
      .post(services.thermodynamicChart.UES_HM_DATA, {
        startTime: startTime,
        endTime: endTime,
        rat: rat,
        serviceType: serviceType,
        deviceType: deviceType,
        P1: P1,
        P2: P2,
        P3: P3,
        P4: P4
      })
      .then(({ data }) => {
        console.log(data)
        if (data.resultCode === 0) {
          console.log(data)
          onSuccess(data)
        }
      })
      .catch(e => {
        console.log(e)
      })
  }
}