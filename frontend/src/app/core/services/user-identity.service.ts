import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

const COOKIE_KEY = 'User-External-Id';

@Injectable({ providedIn: 'root' })
export class UserIdentityService {
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  get(): string | null {
    if (!this.isBrowser) return null;
    const match = document.cookie.split('; ').find((row) => row.startsWith(`${COOKIE_KEY}=`));
    return match ? match.split('=')[1] : null;
  }

  set(externalId: string): void {
    if (!this.isBrowser) return;
    document.cookie = `${COOKIE_KEY}=${externalId}; path=/; SameSite=Strict`;
  }
}
