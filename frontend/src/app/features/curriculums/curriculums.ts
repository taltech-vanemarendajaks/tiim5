import { ChangeDetectionStrategy, Component, inject, Signal } from '@angular/core';
import { Notification, PageLayout } from '@/components';
import { TPipe } from '@/pipes';
import { StudyPlanResponse, StudyPlanService, UserResponse, UserService } from '@/client';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { mapStudyLevel } from '@/utils';
import { filter, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-curriculums',
  imports: [TPipe, PageLayout, RouterLink, Notification],
  templateUrl: './curriculums.html',
  styleUrl: './curriculums.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Curriculums {
  private readonly studyPlanService = inject(StudyPlanService);
  private readonly userService = inject(UserService);

  readonly user: Signal<UserResponse | null> = toSignal(
    this.userService.getAllUsers().pipe(map((users) => users[0] ?? null)),
    { initialValue: null },
  );

  readonly studyPlans: Signal<StudyPlanResponse[]> = toSignal(
    toObservable(this.user).pipe(
      filter((user) => user !== null),
      switchMap((user) => this.studyPlanService.getStudyPlans(user.externalId)),
    ),
    { initialValue: [] },
  );
  protected readonly mapStudyLevel = mapStudyLevel;
}
