/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CourseResponse } from './CourseResponse';
import type { ModuleResponse } from './ModuleResponse';
export type PlannedCourseResponse = {
  externalId: string;
  course: CourseResponse;
  module: ModuleResponse;
  courseStatus: PlannedCourseResponse.courseStatus;
};
export namespace PlannedCourseResponse {
  export enum courseStatus {
    PLANNED = 'PLANNED',
    COMPLETED = 'COMPLETED',
  }
}
