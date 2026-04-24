import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlaceholderPage } from '@/components';

@Component({
  selector: 'app-studies',
  imports: [PlaceholderPage],
  templateUrl: './studies.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Studies {}
