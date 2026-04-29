import { ChangeDetectionStrategy, Component, inject, Signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { provideIcons } from '@ng-icons/core';
import { ICON_SET, IconName } from '../icon/icon-set';
import { Icon } from '../icon/icon';
import { TPipe } from '@/pipes';
import { UserResponse, UserService } from '@/client';
import { toSignal } from '@angular/core/rxjs-interop';
import { UserIdentityService } from '@/services';
import { catchError, of } from 'rxjs';

interface NavItem {
  path: string;
  labelKey: string;
  icon: IconName;
}

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, Icon, TPipe],
  providers: [provideIcons(ICON_SET)],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {
  protected readonly mainNav: readonly NavItem[] = [
    { path: '/dashboard', labelKey: 'Nav.Dashboard', icon: 'dashboard' },
    { path: '/studies', labelKey: 'Nav.Studies', icon: 'studies' },
    { path: '/curriculums', labelKey: 'Nav.Curriculums', icon: 'curriculums' },
    { path: '/schedule', labelKey: 'Nav.Schedule', icon: 'schedule' },
  ];

  protected readonly settingsNav: NavItem = {
    path: '/settings',
    labelKey: 'Nav.Settings',
    icon: 'settings',
  };

  protected readonly registerNav: NavItem = {
    path: '/register',
    labelKey: 'Nav.Register',
    icon: 'register',
  };

  private readonly userService = inject(UserService);
  private readonly userIdentityService = inject(UserIdentityService);

  readonly user: Signal<UserResponse | null> = toSignal(
    this.userIdentityService.get()
      ? this.userService.getCurrentUser().pipe(catchError(() => of(null)))
      : of(null),
    {
      initialValue: null,
    },
  );

  logout(): void {
    this.userIdentityService.remove();
    window.location.href = '/dashboard';
  }
}
