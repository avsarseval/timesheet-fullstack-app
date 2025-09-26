import {HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('access_token');
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token.replace(/['"]+/g, '')}`
      }
    });
  }
  return next(req).pipe(
    catchError((err) => {
      if (err.status === 403) {
        console.log(err.message);

      }
      const error = (err.error && err.error.message) || err.statusText;
      return throwError(() => new Error(error));
    })
  );
};

