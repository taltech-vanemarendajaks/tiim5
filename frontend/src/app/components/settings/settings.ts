import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlaceholderPage } from '@/components';

@Component({
  selector: 'app-settings',
  imports: [PlaceholderPage],
  templateUrl: './settings.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Settings {}
