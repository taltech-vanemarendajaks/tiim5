import {
  lucideBookOpen,
  lucideCalendarDays,
  lucideChartColumn,
  lucideGauge,
  lucideSettings,
  lucideScanFace,
  lucideLogOut,
  lucideSearch,
  lucidePlus,
  lucideChevronDown,
  lucideChevronUp,
} from '@ng-icons/lucide';

export const ICON_SET = {
  dashboard: lucideGauge,
  studies: lucideBookOpen,
  curriculums: lucideCalendarDays,
  schedule: lucideChartColumn,
  settings: lucideSettings,
  register: lucideScanFace,
  logout: lucideLogOut,
  search: lucideSearch,
  plus: lucidePlus,
  up: lucideChevronUp,
  down: lucideChevronDown,
} as const;

export type IconName = keyof typeof ICON_SET;
