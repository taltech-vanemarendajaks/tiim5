import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PlaceholderPage } from '../../shared/components/placeholder-page/placeholder-page';

@Component({
  selector: 'app-studies',
  imports: [PlaceholderPage],
  templateUrl: './studies.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Studies {}
