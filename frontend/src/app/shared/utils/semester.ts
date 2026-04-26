import { PlannedCourseResponse, SemesterResponse } from '@/client';

export function getSemesterKey(semesterType: string | undefined): string {
  if (!semesterType) return '';

  const keys: Record<string, string> = {
    AUTUMN: 'Common.Autumn',
    SPRING: 'Common.Spring',
  };

  return keys[semesterType] ?? semesterType;
}

export function calculateSemesterCredits(plannedCourses: PlannedCourseResponse[]): number {
  return plannedCourses.reduce((total, pc) => total + (pc.course.credits ?? 0), 0);
}

export function findCurrentSemester(semesters: SemesterResponse[]): string | undefined {
  const now = new Date();
  const month = now.getMonth() + 1;
  const year = now.getFullYear();

  const semesterType = month >= 9 || month <= 1 ? 'AUTUMN' : 'SPRING';

  return semesters.find((s) => s.semesterType === semesterType && s.year === year)?.externalId;
}
