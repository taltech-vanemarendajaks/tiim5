import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TPipe } from '../../../shared/pipes/t.pipe';

@Component({
  selector: 'app-curriculum-planning',
  imports: [TPipe],
  templateUrl: './curriculum-planning.html',
  styleUrl: './curriculum-planning.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurriculumPlanning {}
