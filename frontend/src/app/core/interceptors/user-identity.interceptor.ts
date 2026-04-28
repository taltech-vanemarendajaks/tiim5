import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserIdentityService } from '../services/user-identity.service';

export const userIdentityInterceptor: HttpInterceptorFn = (req, next) => {
  const id = inject(UserIdentityService).get();
  if (!id) return next(req);

  return next(req.clone({ headers: req.headers.set('User-External-Id', id) }));
};
