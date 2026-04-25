import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlaceholderPage } from '@/components';

@Component({
  selector: 'app-dashboard',
  imports: [PlaceholderPage],
  templateUrl: './dashboard.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Dashboard {}
