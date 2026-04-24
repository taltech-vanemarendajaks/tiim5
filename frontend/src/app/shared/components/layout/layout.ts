import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faBookOpen,
  faCalendarDays,
  faChartColumn,
  faGauge,
  faGear,
} from '@fortawesome/free-solid-svg-icons';
import { TPipe } from '@/pipes';

interface NavItem {
  path: string;
  labelKey: string;
  icon: IconDefinition;
}

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, FaIconComponent, TPipe],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {
  protected readonly mainNav: readonly NavItem[] = [
    { path: '/dashboard', labelKey: 'Nav.Dashboard', icon: faGauge },
    { path: '/studies', labelKey: 'Nav.Studies', icon: faBookOpen },
    { path: '/curriculums', labelKey: 'Nav.Curriculum', icon: faCalendarDays },
    { path: '/schedule', labelKey: 'Nav.Schedule', icon: faChartColumn },
  ];

  protected readonly settingsNav: NavItem = {
    path: '/settings',
    labelKey: 'Nav.Settings',
    icon: faGear,
  };
}
