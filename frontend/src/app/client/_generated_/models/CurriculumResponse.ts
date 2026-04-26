/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CurriculumResponse = {
  externalId: string;
  title: string;
  studyLevel: CurriculumResponse.studyLevel;
  credits: number;
  creationDate: string;
};
export namespace CurriculumResponse {
  export enum studyLevel {
    BACHELOR = 'BACHELOR',
    MASTER = 'MASTER',
    INTEGRATED = 'INTEGRATED',
    DOCTOR = 'DOCTOR',
  }
}
