import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PlaceholderPage } from '../../shared/components/placeholder-page/placeholder-page';
import { TPipe } from '../../pipes/t.pipe';

@Component({
  selector: 'app-curriculums',
  imports: [PlaceholderPage, RouterLink, TPipe],
  templateUrl: './curriculums.html',
  styleUrl: './curriculums.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Curriculums {}
