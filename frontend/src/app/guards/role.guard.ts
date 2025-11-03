import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const expected = route.data?.['role'] as string | undefined;
  const current = (localStorage.getItem('role') || '').toUpperCase();

  if (auth.isAuthenticated() && (!expected || current === expected.toUpperCase())) {
    return true;
  }
  router.navigate(['/login']);
  return false;
};
