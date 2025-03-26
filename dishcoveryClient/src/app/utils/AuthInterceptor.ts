
// auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Your interceptor logic here
  const jwtToken = localStorage.getItem('jwtToken');
  const fcmToken = localStorage.getItem('fcmToken');
  console.log('Interceptor running for URL:', req.url);
  console.log('Token exists:', !!jwtToken);
  console.info("Found ur fcmToken, ", fcmToken)
  
  let authReq = req;

  // if (jwtToken) {
  //   const authReq = req.clone({
  //     setHeaders: {
  //       Authorization: `Bearer ${jwtToken}`
  //     }
  //   });
  //   return next(authReq);
  // }

  // if (fcmToken) {
  //   authReq = authReq.clone({
  //     setHeaders: {
  //       'FCM-Token': fcmToken
  //     }
  //   });
  // }
  
  // return next(req);

  if (jwtToken) {
    authReq = authReq.clone({
      setHeaders: {
        Authorization: `Bearer ${jwtToken}`
      }
    });
  }
  
  if (fcmToken) {
    authReq = authReq.clone({
      setHeaders: {
        'FCM-Token': fcmToken
      }
    });
  }
  
  return next(authReq);


};
