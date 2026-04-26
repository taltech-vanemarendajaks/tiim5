/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PlannedCourseResponse } from './PlannedCourseResponse';
export type SemesterResponse = {
  externalId: string;
  year: number;
  finished: boolean;
  plannedCourses?: Array<PlannedCourseResponse>;
  creationDate: string;
};
