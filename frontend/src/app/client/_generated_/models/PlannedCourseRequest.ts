/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type PlannedCourseRequest = {
  semesterExternalId: string;
  courseVersionExternalId: string;
  courseExternalId: string;
  status?: PlannedCourseRequest.status;
};
export namespace PlannedCourseRequest {
  export enum status {
    PLANNED = 'PLANNED',
    COMPLETED = 'COMPLETED',
  }
}
