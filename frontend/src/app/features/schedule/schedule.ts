import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlaceholderPage } from '@/components';

@Component({
  selector: 'app-schedule',
  imports: [PlaceholderPage],
  templateUrl: './schedule.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Schedule {}
