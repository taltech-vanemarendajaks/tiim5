import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TPipe } from '@/pipes';

@Component({
  selector: 'app-page-layout',
  imports: [TPipe],
  templateUrl: './page-layout.html',
  styleUrl: './page-layout.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PageLayout {
  readonly titleKey = input.required<string>();
}
