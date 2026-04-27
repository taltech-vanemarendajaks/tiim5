/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CurriculumResponse } from './CurriculumResponse';
export type StudyPlanResponse = {
  externalId: string;
  name?: string;
  completedCredits: number;
  startDate: string;
  curriculum: CurriculumResponse;
  creationDate: string;
};
