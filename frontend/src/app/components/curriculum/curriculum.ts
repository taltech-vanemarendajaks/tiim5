import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PlaceholderPage } from '../../shared/components/placeholder-page/placeholder-page';
import { TPipe } from '../../pipes/t.pipe';

@Component({
  selector: 'app-curriculum',
  imports: [PlaceholderPage, RouterLink, TPipe],
  templateUrl: './curriculum.html',
  styleUrl: './curriculum.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Curriculum {}
