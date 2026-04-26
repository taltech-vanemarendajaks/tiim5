import { ChangeDetectionStrategy, Component, inject, Signal } from '@angular/core';
import { Notification, PageLayout } from '@/components';
import { TPipe } from '@/pipes';
import { StudyPlanResponse, StudyPlanService } from '@/client';
import { toSignal } from '@angular/core/rxjs-interop';
import { UserStore } from '@/services';
import { RouterLink } from '@angular/router';
import { mapStudyLevel } from '@/utils';

@Component({
  selector: 'app-curriculums',
  imports: [TPipe, PageLayout, RouterLink, Notification],
  templateUrl: './curriculums.html',
  styleUrl: './curriculums.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Curriculums {
  private readonly studyPlanService = inject(StudyPlanService);
  private readonly userStore = inject(UserStore);
  private readonly userExternalId = this.userStore.user()?.externalId;

  readonly studyPlans: Signal<StudyPlanResponse[]> = toSignal(
    this.studyPlanService.getStudyPlans(this.userExternalId),
    {
      initialValue: [],
    },
  );
  protected readonly mapStudyLevel = mapStudyLevel;
}
