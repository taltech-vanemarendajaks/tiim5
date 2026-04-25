import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { provideIcons } from '@ng-icons/core';
import { ICON_SET, IconName } from '../icon/icon-set';
import { Icon } from '../icon/icon';
import { TPipe } from '@/pipes';

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
}
