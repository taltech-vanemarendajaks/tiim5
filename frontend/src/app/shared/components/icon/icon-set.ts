import {
  lucideBookOpen,
  lucideCalendarDays,
  lucideChartColumn,
  lucideGauge,
  lucideSettings,
} from '@ng-icons/lucide';

export const ICON_SET = {
  dashboard: lucideGauge,
  studies: lucideBookOpen,
  curriculums: lucideCalendarDays,
  schedule: lucideChartColumn,
  settings: lucideSettings,
} as const;

export type IconName = keyof typeof ICON_SET;
