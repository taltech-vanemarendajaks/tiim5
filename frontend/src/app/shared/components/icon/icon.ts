import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { NgIcon } from '@ng-icons/core';
import { IconName } from './icon-set';

@Component({
  selector: 'app-icon',
  imports: [NgIcon],
  templateUrl: './icon.html',
  styleUrl: './icon.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Icon {
  readonly name = input.required<IconName>();
}
