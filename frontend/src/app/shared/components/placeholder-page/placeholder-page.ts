import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { TPipe } from '@/pipes';

@Component({
  selector: 'app-placeholder-page',
  imports: [TPipe],
  templateUrl: './placeholder-page.html',
  styleUrl: './placeholder-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlaceholderPage {
  readonly titleKey = input.required<string>();
}
